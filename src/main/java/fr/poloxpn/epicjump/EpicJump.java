package fr.poloxpn.epicjump;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.xml.XmlEscapers;
import fr.poloxpn.epicjump.commands.CommandManager;
import fr.poloxpn.epicjump.listeners.ItemUseListener;
import fr.poloxpn.epicjump.listeners.ParkoursListener;
import fr.poloxpn.epicjump.listeners.PlayerDisconnectListener;
import fr.poloxpn.epicjump.listeners.PlayerDropListener;
import fr.poloxpn.epicjump.manager.*;
import fr.poloxpn.epicjump.message.Message;
import fr.poloxpn.epicjump.parkour.Rewards;
import fr.poloxpn.epicjump.data.PlayerData;
import fr.poloxpn.epicjump.version.ActionBarManager;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class EpicJump extends JavaPlugin {

    private Material startPlateMaterial;
    private Material endPlateMaterial;
    private Material checkpointPlateMaterial;

    private Material checkpointItem;
    private Material resetItem;
    private Material leaveItem;

    private CommandManager commandManager;
    private ActionBarManager actionBarManager;
    private CourseManager courseManager;
    private ConfigManager configManager;
    private SessionManager playerSessionManager;
    private LeaderboardManager leaderboardManager;
    private HologramManager hologramManager;
    private Rewards rewards;
    private PlayerData playerData;
    private Message message;

    @Override
    public void onEnable() {
        actionBarManager = new ActionBarManager();
        configManager = new ConfigManager(this);
        courseManager = new CourseManager(this);
        commandManager = new CommandManager(this);
        rewards = new Rewards(this);
        playerSessionManager = new SessionManager(this);
        playerData = new PlayerData(this);
        leaderboardManager = new LeaderboardManager(this);
        hologramManager = new HologramManager(this);
        message = new Message(this);

        reloadPlugin();

        startPlateMaterial = getMaterialFromConfig("start-pressure-plate");
        endPlateMaterial = getMaterialFromConfig("end-pressure-plate");
        checkpointPlateMaterial = getMaterialFromConfig("checkpoint-pressure-plate");

        checkpointItem = getMaterialFromConfig("checkpoint-item");
        resetItem = getMaterialFromConfig("reset-item");
        leaveItem = getMaterialFromConfig("leave-item");

        getServer().getPluginManager().registerEvents(new ParkoursListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDropListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnectListener(this), this);


        String enablingMessage = "\n§8[]====================[ §6EpicJump §8]====================[]\n"
                + "§8| §9EpicJump enabled!\n"
                + "§8[]====================================================[]";
        getServer().getConsoleSender().sendMessage(enablingMessage);

    }

    @Override
    public void onDisable() {
        hologramManager.despawnHolograms();
        String disablingMessage = "\n§8[]====================[ §6EpicJump §8]====================[]\n"
                + "§8| §9EpicJump disabled!\n"
                + "§8[]====================================================[]";
        getServer().getConsoleSender().sendMessage(disablingMessage);
    }
    public ActionBarManager getActionBarManager() {
        return actionBarManager;
    }
    public CourseManager getCourseManager() {
        return courseManager;
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
    public SessionManager getSessionManager() {
        return playerSessionManager;
    }
    public Rewards getRewards() {
        return rewards;
    }
    public PlayerData getPlayerData() {
        return playerData;
    }

    public Material getStartPlateMaterial() {
        return startPlateMaterial;
    }
    public Material getEndPlateMaterial() {
        return endPlateMaterial;
    }
    public Material getCheckpointPlateMaterial() {
        return checkpointPlateMaterial;
    }
    public Material getCheckpointItem() {
        return checkpointItem;
    }
    public Material getResetItem() {
        return resetItem;
    }
    public Material getLeaveItem() {
        return leaveItem;
    }

    public Material getMaterialFromConfig(String configKey) {
        String materialName = getConfig().getString(configKey);
        return XMaterial.matchXMaterial(materialName).orElse(XMaterial.AIR).parseMaterial();
    }
    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }
    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public boolean isPressurePlate(Material material) {
        if(material == null) return false;
        return material == XMaterial.matchXMaterial("STONE_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("LIGHT_WEIGHTED_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("HEAVY_WEIGHTED_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("ACACIA_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("BIRCH_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("CRIMSON_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("DARK_OAK_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("JUNGLE_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("OAK_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("POLISHED_BLACKSTONE_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("SPRUCE_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial()
                || material == XMaterial.matchXMaterial("WARPED_PRESSURE_PLATE").orElse(XMaterial.AIR).parseMaterial();
    }

    public void reloadPlugin() {
        this.reloadConfig();

        loadConfig();
        loadOtherComponents();
    }

    private void loadConfig() {
        saveDefaultConfig();
    }

    private void loadOtherComponents() {
        // Load or reload other necessary components, e.g., managers, handlers
        message.loadMessages();
        hologramManager.despawnHolograms();
        courseManager.loadCourses();
        leaderboardManager.loadLeaderboards();
        hologramManager.loadHolograms();
        rewards.loadCommands();
    }
}
