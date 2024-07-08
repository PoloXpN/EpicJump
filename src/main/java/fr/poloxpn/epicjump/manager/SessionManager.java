package fr.poloxpn.epicjump.manager;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.data.Record;
import fr.poloxpn.epicjump.message.Message;
import fr.poloxpn.epicjump.parkour.Checkpoint;
import fr.poloxpn.epicjump.parkour.Course;
import fr.poloxpn.epicjump.parkour.PlayerSession;
import fr.poloxpn.epicjump.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private final Map<UUID, PlayerSession> playerSession = new HashMap<>();
    private final Map<Player, LocalTime> startTimes = new HashMap<>();
    private final EpicJump plugin;

    public SessionManager(EpicJump plugin) {
        this.plugin = plugin;
    }

    public PlayerSession getPlayerSession(Player player) {
        return playerSession.get(player.getUniqueId());
    }

    public void startCourse(Player player, Course course) {
        if(playerSession.containsKey(player.getUniqueId())) {
            playerSession.get(player.getUniqueId()).setStartTime();
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_RESTARTED).replaceAll("%course%", course.getName()));
        } else {
            playerSession.put(player.getUniqueId(), new PlayerSession(player, course));
            plugin.getPlayerData().storePermissions(player);
            plugin.getPlayerData().storeItems(player);
            plugin.getPlayerData().giveItems(player);
            plugin.getPlayerData().toggleDoubleJump(player, false);
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_STARTED).replaceAll("%course%", course.getName()));
        }
        startTimes.put(player, LocalTime.now());
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!startTimes.containsKey(player)) {
                    this.cancel();
                    return;
                }

                LocalTime startTime = startTimes.get(player);
                Duration duration = Duration.between(startTime, LocalTime.now());
                String formattedTime = Utils.getTimeFormated(duration.toMillis());

                plugin.getActionBarManager().sendActionBar(player, Message.getMessage(Message.Messages.TIME_ELAPSED).replaceAll("%time%", formattedTime));
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void FinishCourse(Player player) {
        String timeTaken = Utils.getTimeFormated(System.currentTimeMillis() - playerSession.get(player.getUniqueId()).getStartTime());

        // Send messages
        player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_FINISHED).replaceAll("%time%", timeTaken));
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if(p != player) {
                p.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_FINISHED_BROADCAST).replaceAll("%player%", player.getName()).replaceAll("%time%", timeTaken));
            }
        }

        String courseName = playerSession.get(player.getUniqueId()).getCourse().getName();

        // Leaderboard
        String playerName = player.getName();
        long time = System.currentTimeMillis() - playerSession.get(player.getUniqueId()).getStartTime();
        int lives = playerSession.get(player.getUniqueId()).getLives();

        // Check if the player is already in the leaderboard
        if (!plugin.getLeaderboardManager().hasRecord(courseName, playerName)) {
            plugin.getLeaderboardManager().addRecord(new Record(courseName, playerName, time, lives));
        }

        for (Record record : plugin.getLeaderboardManager().getLeaderboard(courseName)) {
            if (record.getPlayerName().equals(playerName)) {
                if (record.getTime() > time) {
                    record.setTime(time);
                }
                if (record.getLives() < lives) {
                    record.setLives(lives);

                }
                plugin.getLeaderboardManager().saveRecord(record);
                plugin.getHologramManager().reloadLeaderboardHolograms(courseName);
                break;
            }
        }
        endCourse(player);

        //Read and execute all commands listed in the course's config
        plugin.getRewards().executeCommands(courseName, player);

    }

    public void endCourse(Player p, boolean sendMsg) {
        endCourse(p);
        if(sendMsg) {
            p.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_ENDED));
        }
    }

    public void endCourse(Player p) {
        startTimes.remove(p);
        plugin.getPlayerData().retrievePermissions(p);
        plugin.getPlayerData().retrieveItems(p);
        plugin.getPlayerData().toggleDoubleJump(p, true);
        playerSession.remove(p.getUniqueId());
    }

    public void reachCheckpoint(Player player, Checkpoint checkpoint) {
        UUID playerId = player.getUniqueId();
        playerSession.get(playerId).setLastCheckpoint(checkpoint.getLocation());
        player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.CHECKPOINT_REACHED));

        // Set the checkpoint as reached
        Checkpoint c = new Checkpoint(checkpoint.getLocation(), true);
        playerSession.get(playerId).getCheckpoints().set(playerSession.get(playerId).getCheckpoints().indexOf(checkpoint), c);

        if (!playerSession.get(playerId).hasUnlimitedLives()) {
            String courseName = playerSession.get(playerId).getCourse().getName();
            int lives = playerSession.get(playerId).getLives();
            int addedLives = plugin.getCourseManager().getCourse(courseName).getLivesPerCheckpoint();
            lives += addedLives;

            playerSession.get(playerId).setLives(lives);

            if (addedLives == 1) {
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.LIVES_ADDED).replaceAll("%lives%", String.valueOf(lives)).replaceAll("%lives_to_add%", String.valueOf(addedLives)));
            } else {
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.LIVES_ADDED_PLURAL).replaceAll("%lives%", String.valueOf(lives)).replaceAll("%lives_to_add%", String.valueOf(addedLives)));
            }
        }
    }

    public void backToLastCheckpoint(Player player) {

        UUID playerId = player.getUniqueId();
        Location lastCheckpoint = playerSession.get(playerId).getLastCheckpoint();

        if(!playerSession.get(playerId).hasUnlimitedLives()) {

            if (lastCheckpoint != playerSession.get(playerId).getSpawnLocation()) {
                int lives = playerSession.get(playerId).getLives() - 1;
                playerSession.get(playerId).setLives(lives);
                if (lives < 1) {
                    player.teleport(playerSession.get(playerId).getSpawnLocation());
                    endCourse(player);
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_FAILED));
                    return;
                }
                if (lives < 2) {
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.LAST_CHANCE));
                } else {
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.LIVES_REMAINING).replaceAll("%lives%", String.valueOf(lives)));
                }
            }
        }
        player.teleport(playerSession.get(playerId).getLastCheckpoint());
    }
}
