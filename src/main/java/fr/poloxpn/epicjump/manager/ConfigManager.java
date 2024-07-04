package fr.poloxpn.epicjump.manager;

import fr.poloxpn.epicjump.EpicJump;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final EpicJump plugin;

    public ConfigManager(EpicJump plugin) {
        this.plugin = plugin;
    }

    // Deserialization linéaire de l'objet Location
    public Location deserializeLocation(String locationString) {
        // Ex: "world, x, y, z, yaw, pitch"
        String[] location = locationString.split(", ");
        return new Location(plugin.getServer().getWorld(location[0]),
                Double.parseDouble(location[1]),
                Double.parseDouble(location[2]),
                Double.parseDouble(location[3]),
                Float.parseFloat(location[4]),
                Float.parseFloat(location[5]));
    }

    // Serialization linéaire de l'objet Location
    public String serializeLocation(Location location) {
        if (location != null) {
            return location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getYaw() + ", " + location.getPitch();
        }
        return null;
    }

    public FileConfiguration loadConfig(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Could not create " + fileName + " file.");
            }
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Could not load " + fileName + " file.");
            plugin.onDisable();
        }
        return config;
    }

    public void saveConfig(FileConfiguration config, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Could not save " + fileName + " file.");
        }
    }

}
