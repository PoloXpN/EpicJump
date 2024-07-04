package fr.poloxpn.epicjump.listeners;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.parkour.Checkpoint;
import fr.poloxpn.epicjump.parkour.Course;

import fr.poloxpn.epicjump.parkour.PlayerSession;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ParkoursListener implements Listener {
    private final EpicJump plugin;
    private final Set<UUID> activePlayersOnStart = new HashSet<>();

    public ParkoursListener(EpicJump plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.PHYSICAL)) return;
        if(activePlayersOnStart.contains(event.getPlayer().getUniqueId())) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(block != null) {
            if(!plugin.isPressurePlate(block.getType())) return;

            Location location = block.getLocation();
            PlayerSession playerSession = plugin.getSessionManager().getPlayerSession(player);

            //Start check
            if (block.getType() == plugin.getStartPlateMaterial()) {
                for (Course c : plugin.getCourseManager().getCourses().values()) {
                    if (location.equals(c.getStartLocation())) {
                        activePlayersOnStart.add(player.getUniqueId());
                        plugin.getSessionManager().startCourse(player, c);

                        // Start a task to check if the player leaves the pressure plate
                        // Start a task to check if the player leaves the pressure plate
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!player.isOnline() || !player.getLocation().getBlock().getLocation().equals(location)) {
                                    activePlayersOnStart.remove(player.getUniqueId());
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0L, 10L); // Check every 0.5 seconds (10 ticks)
                        return;
                    }
                }
            }

            // At this point, a playerSession is required to continue
            if (playerSession == null) return;

            //Endpoint check
            if (block.getType() == plugin.getEndPlateMaterial()) {
                if (location.equals(playerSession.getEndLocation())) {
                    plugin.getSessionManager().FinishCourse(player);
                }
            }

            //Checkpoint check
            else if (block.getType() == plugin.getCheckpointPlateMaterial()) {
                for (Checkpoint checkpoint : playerSession.getCheckpoints()) {
                    if (location.equals(checkpoint.getLocation())) {
                        if (!playerSession.getCheckpoint(checkpoint.getLocation()).isReached()) {
                            plugin.getSessionManager().reachCheckpoint(player, checkpoint);
                        }
                        return;
                    }
                }
            }
        }
    }
}
