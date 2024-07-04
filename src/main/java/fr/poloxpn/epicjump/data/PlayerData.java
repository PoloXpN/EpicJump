package fr.poloxpn.epicjump.data;

import com.cryptomorin.xseries.XMaterial;
import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class PlayerData {

    private final Map<String, ItemStack> playerItems = new HashMap<>();
    private final Map<Player, PermissionAttachment> playerAttachments = new HashMap<>();
    private final Map<Player, Map<String,Boolean>> originalPermissions = new HashMap<>();
    private final EpicJump plugin;

    public PlayerData(EpicJump plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Collections.singletonList(name));
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack getCheckpointItem() {
        Material m = plugin.getMaterialFromConfig("checkpoint-item");
        if(m != null && m != Material.AIR) {
            return createItem(m, ChatColor.WHITE + "" + ChatColor.BOLD + "Checkpoint");
        }
        return createItem(XMaterial.matchXMaterial(Material.HEAVY_WEIGHTED_PRESSURE_PLATE).parseMaterial(), Message.getMessage(Message.Messages.INVALID_MATERIAL));
    }

    public ItemStack getResetItem() {
        Material m = plugin.getMaterialFromConfig("reset-item");
        if (m != null && m != Material.AIR) {
            return createItem(m, ChatColor.GOLD + "" + ChatColor.BOLD + "Reset");
        }
        return createItem(Material.BARRIER, Message.getMessage(Message.Messages.INVALID_MATERIAL));
    }

    public ItemStack getLeaveItem() {
        Material m = plugin.getMaterialFromConfig("leave-item");
        if (m != null && m != Material.AIR) {
            return createItem(m, ChatColor.RED + "" + ChatColor.BOLD + "Leave");
        }
        return createItem(XMaterial.matchXMaterial(Material.OAK_DOOR).parseMaterial(), Message.getMessage(Message.Messages.INVALID_MATERIAL));
    }

    public void giveItems(Player p) {
        p.getInventory().setItem(3, getCheckpointItem());
        p.getInventory().setItem(4, getResetItem());
        p.getInventory().setItem(5, getLeaveItem());
    }
    public void storeItems(Player p) {
        playerItems.clear();
        // Store the player's items from slot 4 to 6
        for (int i = 3; i < 6; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null) {
                playerItems.put(String.valueOf(i),item);
            }
        }
    }
    public void retrieveItems(Player p) {
        p.getInventory().clear(3);
        p.getInventory().clear(4);
        p.getInventory().clear(5);
        for (Map.Entry<String, ItemStack> entry : playerItems.entrySet()) {
            p.getInventory().setItem(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        playerItems.clear();
    }

    public void storePermissions(Player p) {
        PermissionAttachment attachment = p.addAttachment(plugin);
        Map<String, Boolean> playerOriginalPermissions = new HashMap<>();
        String[] permissionsToRemove = {"epicjump.fly", "infinitejump.use"};

        for (String permission : permissionsToRemove) {
            boolean hasPermission = p.hasPermission(permission);
            playerOriginalPermissions.put(permission, hasPermission);
            attachment.setPermission(permission, false);
        }

        originalPermissions.put(p, playerOriginalPermissions);
        playerAttachments.put(p, attachment);
    }

    public void retrievePermissions(Player p) {
        PermissionAttachment attachment = playerAttachments.get(p);
        Map<String, Boolean> playerOriginalPermissions = originalPermissions.get(p);

        if(attachment == null || playerOriginalPermissions == null) return;

        for (Map.Entry<String, Boolean> entry : playerOriginalPermissions.entrySet()) {
            attachment.setPermission(entry.getKey(), entry.getValue());
        }

        playerAttachments.remove(p);
        originalPermissions.remove(p);
    }

    public void toggleDoubleJump(Player p, boolean state) {
        String command = "infinitejump toggle %state% %player%".replaceAll("%state%", state ? "on" : "off").replaceAll("%player%", p.getName());
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
    }
}
