package fr.poloxpn.epicjump.manager;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.parkour.Hologram;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private final List<Hologram> holograms = new ArrayList<>();

    private final EpicJump plugin;
    private final ConfigManager configManager;

    public HologramManager(EpicJump plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public void addHologram(Hologram hologram) {
        // Si le hologram a la meme course, le meme type et le meme id, on le remplace
        holograms.stream()
                .filter(h -> h.getCourseName().equals(hologram.getCourseName()) && h.getType().equals(hologram.getType()) && h.getId() == hologram.getId())
                .findFirst()
                .ifPresent(h -> holograms.remove(h));
        holograms.add(hologram);
        saveHologram(hologram);
        spawnHologram(hologram);
    }

    public void removeHologram(String courseName, String type, int id) {
        // Remove every hologram's armor stand
        // Hologram can have multiple lines and each line is an armor stand
        Hologram hologram = getHologram(courseName, type, id);
        holograms.removeIf(h -> h.getCourseName().equals(courseName) && h.getType().equals(type) && h.getId() == id);

        despawnHologram(hologram);
        saveHologram(hologram);
    }

    public void removeAllCourseHolograms(String course) {
        if (holograms.isEmpty()) return;

        holograms.stream().filter(hologram -> hologram.getCourseName().equals(course)).forEach(hologram -> {
            removeHologram(hologram.getCourseName(), hologram.getType(), hologram.getId());
        });
    }

    public List<Hologram> getHolograms() {
        return holograms;
    }

    public Hologram getHologram(String courseName, String type, int id) {
        return holograms.stream()
                .filter(hologram -> hologram.getCourseName().equals(courseName) && hologram.getType().equals(type) && hologram.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void setHologramLine(String courseName, String type,int id, int index, String line) {
        Hologram hologram = getHologram(courseName, type, id);
        if (hologram != null) {
            hologram.setLine(index, line);
        }
    }

    public void setHologramLines(String courseName, String type, int id, String... lines) {
        Hologram hologram = getHologram(courseName, type, id);
        if (hologram != null) {
            hologram.setLines(lines);
        }
    }

    public void spawnHologram(Hologram hologram) {
        despawnHologram(hologram);
        Location location = hologram.getLocation().clone();

        // spawn un armorstand pour chaque ligne de bas en haut
        int length = hologram.getLines().length;

        for (int i = 0; i < length; i++) {
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setBasePlate(false);
            armorStand.setMarker(false);
            armorStand.setCustomName(hologram.getLines()[length - 1 - i]);
            armorStand.setCustomNameVisible(true);
            armorStand.setVisible(false);
            armorStand.setSmall(true);
            location.add(0, 0.25, 0);
        }
    }

    public void spawnHolograms() {
        holograms.forEach(this::spawnHologram);
    }

    public void reloadLeaderboardHolograms(String course) {
        holograms.stream()
                .filter(hologram -> hologram.getCourseName().equals(course) && hologram.getType().equals("leaderboard"))
                .forEach(hologram -> {
                    hologram.setLines(plugin.getLeaderboardManager().getLeaderboardLines(hologram.getCourseName()));
                        saveHologram(hologram);
                        spawnHologram(hologram);
                });
    }

    public void despawnHologram(Hologram hologram) {
        Location location = hologram.getLocation();
        location.getWorld().getNearbyEntities(location, 1, hologram.getLines().length, 1).stream()
                .filter(entity -> entity instanceof ArmorStand)
                .forEach(entity -> entity.remove());
    }

    public void despawnHolograms() {
        holograms.forEach(this::despawnHologram);
    }

    // load holograms from config
    public void loadHolograms() {
        // load holograms from config
        FileConfiguration hologramsConfig = configManager.loadConfig("holograms.yml");

        //course:
        //  type:
        //      id: <id>
        //          location: <location>
        //          lines: <lines>

        for (String course : hologramsConfig.getKeys(false)) {
            for (String type : hologramsConfig.getConfigurationSection(course).getKeys(false)) {
                for (String id : hologramsConfig.getConfigurationSection(course + "." + type).getKeys(false)) {
                    Location location = plugin.getConfigManager().deserializeLocation(hologramsConfig.getString(course + "." + type + "." + id + ".location"));
                    String[] lines = hologramsConfig.getStringList(course + "." + type + "." + id + ".lines").toArray(new String[0]);
                    for(int i = 0; i < lines.length; i++) {
                        lines[i] = lines[i].replace("&", "§");
                    }
                    addHologram(new Hologram(course, type, Integer.parseInt(id), location, lines));
                }
            }
        }
    }

    // save holograms to config
    public void saveHologram(Hologram hologram) {
        // save holograms to config

        FileConfiguration hologramsConfig = configManager.loadConfig("holograms.yml");

        hologramsConfig.set(hologram.getCourseName() + "." + hologram.getType() + "." + hologram.getId() + ".location", plugin.getConfigManager().serializeLocation(hologram.getLocation()));
        hologramsConfig.set(hologram.getCourseName() + "." + hologram.getType() + "." + hologram.getId() + ".lines", hologram.getLines());

        if(hologramsConfig.getConfigurationSection(hologram.getCourseName()).getKeys(false).isEmpty()) {
            hologramsConfig.set(hologram.getCourseName(), null);
        }

        configManager.saveConfig(hologramsConfig, "holograms.yml");
    }
}
