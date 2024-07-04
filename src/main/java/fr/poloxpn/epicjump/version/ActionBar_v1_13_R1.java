package fr.poloxpn.epicjump.version;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;


public class ActionBar_v1_13_R1 implements IActionBar {
    @Override
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
