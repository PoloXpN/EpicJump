package fr.poloxpn.epicjump.version;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarManager {
    private IActionBar IActionBar;

    public ActionBarManager() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_8_R3":
                IActionBar = new ActionBar_v1_8_R3();
                break;
            case "v1_13_R1":
            case "v1_13_R2":
            case "v1_14_R1":
            case "v1_15_R1":
            case "v1_16_R1":
            case "v1_16_R2":
            case "v1_16_R3":
            case "v1_17_R1":
            case "v1_18_R1":
            case "v1_18_R2":
            case "v1_19_R1":
            case "v1_19_R2":
            case "v1_20_R1":
                IActionBar = new ActionBar_v1_13_R1();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported server version: " + version);
        }
    }

    public void sendActionBar(Player player, String message) {
        IActionBar.sendActionBar(player, message);
    }
}
