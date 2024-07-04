package fr.poloxpn.epicjump.listeners;

import fr.poloxpn.epicjump.EpicJump;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectListener implements Listener {

    private final EpicJump plugin;

    public PlayerDisconnectListener(EpicJump plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {

        if(plugin.getSessionManager().getPlayerSession(event.getPlayer()) != null) {
            plugin.getSessionManager().endCourse(event.getPlayer());
        }
    }
}
