package fr.poloxpn.epicjump.listeners;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.parkour.PlayerSession;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemUseListener implements Listener {

    private final EpicJump plugin;
    public ItemUseListener(EpicJump plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        PlayerSession playerSession = plugin.getSessionManager().getPlayerSession(event.getPlayer());
        if(playerSession == null) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getItem() != null) {
            event.setCancelled(true);
            if (plugin.getCheckpointItem() != Material.AIR && event.getItem().getType() == plugin.getCheckpointItem()) {
                plugin.getSessionManager().backToLastCheckpoint(event.getPlayer());
            } else if (plugin.getResetItem() != Material.AIR && event.getItem().getType() == plugin.getResetItem()) {
                event.getPlayer().teleport(playerSession.getSpawnLocation());
            } else if (plugin.getLeaveItem() != Material.AIR && event.getItem().getType() == plugin.getLeaveItem()) {
                plugin.getSessionManager().endCourse(event.getPlayer());
            }
        }
    }
}
