package fr.poloxpn.epicjump.message;

import fr.poloxpn.epicjump.EpicJump;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Message {

    private final static Map<String, String> messages = new HashMap<>();
    private final String defaultLanguage = "xx_XX";
    private final EpicJump plugin;
    public static String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8[&6EpicJump&8] &r");

    public Message(EpicJump plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    public static String getMessage(Messages msg) {
        return ChatColor.translateAlternateColorCodes('&',messages.get(msg.name()));
    }

    private void loadMessages() {
        createDefaultLanguageFile();
        FileConfiguration config = plugin.getConfig();
        String lang = config.getString("language", defaultLanguage);
        File langFile = new File(plugin.getDataFolder() + File.separator + "languages", lang + ".yml");
        if(!langFile.exists()) {
            plugin.getLogger().warning("Language file not found. Using default language.");
            langFile = new File(plugin.getDataFolder() + File.separator + "languages", defaultLanguage + ".yml");
        }
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        // Load all messages
        for(Messages message : Messages.values()) {
            String msg = message.name();
            String localizedMessage = langConfig.getString(msg);
            if(localizedMessage == null) {
                localizedMessage = message.getMessage();
            }
            // Add message to the map
            messages.put(msg, localizedMessage);
        }
    }

    private void createDefaultLanguageFile() {

        File languageDir = new File(plugin.getDataFolder(), "languages");
        File defaultLang = new File(languageDir, "xx_XX.yml");

        try {
            // Ensure the languages directory exists
            if (!languageDir.exists()) {
                languageDir.mkdirs();
            }

            // Create the default language file if it does not exist
            if (!defaultLang.exists()) {
                defaultLang.createNewFile();
            }
            //plugin.saveResource("languages/xx_XX.yml", true);
            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(defaultLang);
            for (Messages message : Messages.values()) {
                String msg = message.name();
                String localizedMessage = message.getMessage();
                langConfig.set(msg, localizedMessage);
                langConfig.save(defaultLang);
            }


        } catch (Exception e) {
            plugin.getLogger().severe("Could not create default language file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public enum Messages {
        ADD_CHECKPOINT("&7Checkpoint added for the parkour &b'%course%'&7."),
        ADD_CHECKPOINT_PRESSURE_PLATE("&7Checkpoint added for the parkour &b'%course%' &7with pressure plate."),
        ADD_END_PRESSURE_PLATE("&7Endpoint set for the parkour &b'%course%' &7with pressure plate. "),
        ADD_END("&7Endpoint set for the parkour &b'%course%'&7."),
        ADD_HOLOGRAM("&7Hologram added for the parkour &b'%course%'&7."),
        ADD_SPAWN("&7Spawn point set for the parkour &b'%course%'&7."),
        ADD_START( "&7Starting point set for the parkour &b'%course%'&7."),
        ADD_START_PRESSURE_PLATE("&7Starting point set for the parkour &b'%course%' &7with pressure plate."),
        CHECKPOINT_REACHED("&7New Checkpoint reached."),
        CLEAR_LEADERBOARD("&7Leaderboard cleared for the parkour &b'%course%'&7."),
        CLEAR_RECORD("&7Record cleared for the player %player% from parkour &b'%course%'&7."),
        COURSE_ALREADY_EXISTS("&7Course already exists with this name."),
        COURSE_CREATED("&7Course &b%course% &7created successfully."),
        COURSE_DELETED("&7Course &b%course% &7deleted successfully."),
        COURSE_DELETED_BY_ADMIN("&cCourse deleted by admin."),
        COURSE_ENDED("&7Course ended."),
        COURSE_FINISHED("&aCourse finished in &6%time%&a."),
        COURSE_FAILED("&cYou have failed the course."),
        COURSE_FINISHED_BROADCAST("&e%player% &afinished the course in &6%time%&a."),
        COURSE_NOT_FOUND("&cParkour not found."),

        COURSE_STARTED("&7Course started."),
        COURSE_RESTARTED("&7Your time for course &b%course% &7has been reset."),
        HOLOGRAM_ALREADY_EXISTS("&cHologram already exists for this parkour."),
        HOLOGRAM_NOT_FOUND("&cHologram not found for this parkour."),
        HOLOGRAM_START_LINE_1("&e&lParkour %course%"),
        HOLOGRAM_START_LINE_2("&a&lStart"),
        HOLOGRAM_END_LINE_1("&e&lParkour %course%"),
        HOLOGRAM_END_LINE_2("&c&lEnd"),
        HOLOGRAM_CHECKPOINT_LINE_1("&e&lCheckpoint"),
        HOLOGRAM_CHECKPOINT_LINE_2("&b&l#%id%"),

        LAST_CHANCE("&7You now have &c1 &7life remaining. &6Be careful!"),
        LIVES_ADDED("&bGods &7have given you &c%lives_to_add% &7more life. You now have &c%lives% &7lives."),
        LIVES_ADDED_PLURAL("&bGods &7have given you &c%lives_to_add% &7more lives. You now have &c%lives% lives."),
        LIVES_REMAINING("&7You have &c%lives% &7lives remaining."),
        INVALID_MATERIAL("&cInvalid material &7specified for start pressure plate in config. \nYou need to place pressure plate manually."),
        INVALID_HOLOGRAM_TYPE("&cInvalid hologram type. Valid types are: start, end, checkpoint, leaderboard."),
        PREFIX("&8[&6EpicJump&8] &r"),
        RECORD_NOT_FOUND("&cRecord not found."),
        REMOVE_CHECKPOINT("&7Checkpoint removed for the parkour &b'%course%'&7."),
        REMOVE_HOLOGRAM("&7Hologram removed for the parkour &b'%course%'&7."),
        STARTING("&7Starting the course..."),
        TIME_ELAPSED("&7Time elapsed: &6%time%&7.");

        private String msg;
        private Messages(String msg) {
            this.msg = msg;
        }

        public String getMessage() {
            return this.msg;
        }
    }
}
