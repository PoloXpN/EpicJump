package fr.poloxpn.epicjump.commands;

import fr.poloxpn.epicjump.message.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public abstract class SubCommand {

    private final String[] aliases;
    private final String usage, description, permission;

    public SubCommand(String usage, String description, String permission, String... aliases) {
        this.aliases = aliases;
        this.usage = usage;
        this.description = description;
        this.permission = permission;
    }

    public boolean is(String arg) {
        return Arrays.asList(aliases).contains(arg.toLowerCase());
    }

    public String getUsage() {
        return usage;
    }
    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    protected abstract void onExePlayer(Player sender, String... args);

    protected abstract void onExeConsole(ConsoleCommandSender sender, String... args);

    protected void notAllowed(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            commandSender.sendMessage(Message.PREFIX +ChatColor.RED + "This command is not allowed from minecraft client.");
        } else {
            commandSender.sendMessage(Message.PREFIX +ChatColor.RED + "This command is not allowed from console.");
        }
    }

    public boolean isAllowed(CommandSender sender) {
        return isAllowed(sender, false);
    }

    public boolean isAllowed(CommandSender sender, boolean warn) {
        if (sender.hasPermission(permission)) {
            return true;
        } else {
            if (warn) {
                sender.sendMessage(Message.PREFIX + ChatColor.RED + "You don't have the permission to do this.");
            }
            return false;
        }
    }

    public void showUsage(CommandSender sender) {
        sender.sendMessage(Message.PREFIX + ChatColor.RED + "Usage: " + usage);
    }

}
