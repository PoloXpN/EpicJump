package fr.poloxpn.epicjump.parkour;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.manager.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rewards {

    private Map<String, List<String>> commands = new HashMap<>();
    private final EpicJump plugin;
    private final ConfigManager configManager;

    public Rewards(EpicJump plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public void executeCommands(String courseName, Player player) {
        List<String> courseCommands = commands.get(courseName);
        if (courseCommands != null) {
            for (String command : commands.get(courseName)) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%player%", player.getName()));
            }
        } else {
            System.out.println("No commands found for course: " + courseName);
        }
    }

    public void loadCommands() {

        FileConfiguration rewardsConfig = configManager.loadConfig("rewards.yml");
        plugin.saveResource("rewards.yml", false);

        if (rewardsConfig.contains("courses")) {
            ConfigurationSection coursesSection = rewardsConfig.getConfigurationSection("courses");
            if (coursesSection != null) {
                for (String key : coursesSection.getKeys(false)) {
                    List<String> commandList = rewardsConfig.getStringList("courses." + key);
                    if (commandList != null) {
                        commands.put(key, commandList);
                        System.out.println("Loaded commands for course: " + key);
                    } else {
                        System.out.println("No commands found for course: " + key);
                    }
                }
            } else {
                System.out.println("No 'courses' section found in rewards.yml");
            }
        } else {
            System.out.println("No 'courses' key found in rewards.yml");
        }
    }



}
