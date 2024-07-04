package fr.poloxpn.epicjump.listeners;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.parkour.PlayerSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {

    private final EpicJump plugin;

    public PlayerDropListener(EpicJump plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {

        Player p = event.getPlayer();
        PlayerSession playerSession = plugin.getSessionManager().getPlayerSession(p);
        int slot = event.getPlayer().getInventory().getHeldItemSlot();

        if(playerSession != null) {
            if(slot == 3 || slot == 4 || slot == 5) {
                event.setCancelled(true);
            }
        }
    }
}
