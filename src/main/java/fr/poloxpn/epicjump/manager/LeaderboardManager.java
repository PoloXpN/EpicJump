package fr.poloxpn.epicjump.manager;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.data.Record;
import fr.poloxpn.epicjump.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardManager {
    private final Map<String, List<Record>> leaderBoards = new HashMap<>();
    private final EpicJump plugin;
    private final ConfigManager configManager;

    public LeaderboardManager(EpicJump plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public boolean hasRecord(String course, String player) {
        return leaderBoards.containsKey(course) && leaderBoards.get(course).stream().anyMatch(record -> record.getPlayerName().equals(player));
    }

    public void addRecord(Record record) {
        String course = record.getCourse();
        if (leaderBoards.containsKey(course)) {
            leaderBoards.get(course).add(record);
        } else {
            List<Record> recordList = new ArrayList<>();
            recordList.add(record);
            leaderBoards.put(course, recordList);
        }
        saveRecord(record);
    }

    public void removeRecord(Record record) {
        String course = record.getCourse();
        String player = record.getPlayerName();

        // Reload Leaderboard's holograms if the record is in the top 10
        if (leaderBoards.get(course).indexOf(record) < 10) {
            leaderBoards.get(course).removeIf(r -> r.getPlayerName().equals(record.getPlayerName()));
            plugin.getHologramManager().reloadLeaderboardHolograms(course);
        } else {
            leaderBoards.get(course).removeIf(r -> r.getPlayerName().equals(record.getPlayerName()));
        }

        FileConfiguration leaderboard = configManager.loadConfig("leaderboard.yml");
        leaderboard.set(course + "." + player, null);
        configManager.saveConfig(leaderboard, "leaderboard.yml");
    }

    public void removeRecord(String course, String player) {
        removeRecord(getRecord(course, player));
    }

    public Record getRecord(String course, String player) {
        if (leaderBoards.containsKey(course)) {
            for (Record record : leaderBoards.get(course)) {
                if (record.getPlayerName().equals(player)) {
                    return record;
                }
            }
        }
        return null;
    }

    public void clearLeaderboard(String course) {
        if (leaderBoards.isEmpty() || leaderBoards.get(course).isEmpty()) return;

        List<Record> recordsToRemove = new ArrayList<>(leaderBoards.get(course));
        for (Record record : recordsToRemove) {
            removeRecord(record);
        }
    }

    public List<Record> getLeaderboard(String course) {
        return leaderBoards.get(course);
    }

    public String[] getLeaderboardLines(String course) {

        // --123--45--67890--123456789012345678901--234567890
        // &e========&8|&6EpicJump leaderboard&8|&e==========
        // &e#1 &7- &bLongPlayerName &8-------- &e00h:00m:00s
        // &e#2 &7- &bPlayerName &8------------ &e00h:00m:00s
        // &e#3 &7- &bOtherName &8------------- &e00h:00m:00s
        // &e#4 &7- &bAnotherName &8----------- &e00h:00m:00s
        // &e#5 &7- &bName &8------------------ &e00h:00m:00s
        // --123--45--67890--1234567890123456789--01234567890

        List<String> top = new ArrayList<>();

        int maxWidth = 40;

        // <--- Title --->
        String leaderboardTitle = getTitleString(course, maxWidth);
        top.add(leaderboardTitle);

        // <--- Lines --->
        int separatorWidth = maxWidth - 18;
        String defaultSeparator = "----------------------";
        List<Record> record = leaderBoards.get(course);
        if (record != null) record.sort((o1, o2) -> (int) (o1.getTime() - o2.getTime()));
        for(int i = 0; i < 10; i++) {
            if (record == null || record.size() <= i || record.get(i) == null) {
                top.add(ChatColor.YELLOW + "#" + (i+1) + ChatColor.GRAY + " - " + ChatColor.DARK_GRAY + defaultSeparator + ChatColor.YELLOW + " 00h:00m:00s");
            } else {
                String separator = defaultSeparator.substring(0, separatorWidth - record.get(i).getPlayerName().length());
                top.add(ChatColor.YELLOW + "#" + (i+1) + ChatColor.GRAY + " - " + ChatColor.AQUA + record.get(i).getPlayerName() + ChatColor.DARK_GRAY + " " + separator + " " + ChatColor.YELLOW + Utils.getTimeFormated(record.get(i).getTime()));
            }
        }
        return top.toArray(new String[0]);
    }

    private static String getTitleString(String course, int maxWidth) {
        int courseNameLength = course.length();
        String leaderboardTitleString = course.length()%2 == 0 ? " Leaderboard: " + course + " " :" Leaderboard : " + course + " ";
        int paddingLength = (maxWidth - leaderboardTitleString.length()) / 2;
        leaderboardTitleString = course.length()%2 == 0 ? ChatColor.GREEN + " Leaderboard: " + ChatColor.AQUA + course + " " :
                                                          ChatColor.GREEN + " Leaderboard : " + ChatColor.AQUA + course + " ";
        String padding = "";
        for (int i = 0; i < paddingLength; i++) {
            padding += "=";
        }
        String leaderboardTitle = ChatColor.YELLOW + padding + leaderboardTitleString + ChatColor.YELLOW + padding;
        return leaderboardTitle;
    }

    public void loadLeaderboards() {

        FileConfiguration leaderboard = configManager.loadConfig("leaderboard.yml");

        //course:
        //  player:
        //      time: <time>
        //      lives: <lives>

        for (String course : leaderboard.getKeys(false)) {
            List<Record> record = new ArrayList<>();
            for (String player : leaderboard.getConfigurationSection(course).getKeys(false)) {
                record.add(new Record(
                        course,
                        player,
                        Utils.timeFormatedToLong(leaderboard.getString(course + "." + player + ".time")),
                        leaderboard.getInt(course + "." + player + ".lives")));
            }
            leaderBoards.put(course, record);
        }
    }

    public void saveRecord(Record record) {

        FileConfiguration leaderboard = configManager.loadConfig("leaderboard.yml");

        String timeFormatted = Utils.getTimeFormated(record.getTime()).replace("h", "").replace("m", "").replace("s", "");

        leaderboard.set(record.getCourse() + "." + record.getPlayerName() + ".time", timeFormatted);
        leaderboard.set(record.getCourse() + "." + record.getPlayerName() + ".lives", record.getLives());

        if(leaderboard.getConfigurationSection(record.getCourse()).getKeys(false).isEmpty()) {
            leaderboard.set(record.getCourse(), null);
        }

        configManager.saveConfig(leaderboard, "leaderboard.yml");
    }
}
