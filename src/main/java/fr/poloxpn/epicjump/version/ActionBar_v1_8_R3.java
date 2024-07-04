package fr.poloxpn.epicjump.version;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class ActionBar_v1_8_R3 implements IActionBar {
    @Override
    public void sendActionBar(Player player, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(icbc, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
