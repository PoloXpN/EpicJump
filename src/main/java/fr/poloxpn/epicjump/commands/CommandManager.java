package fr.poloxpn.epicjump.commands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.subcommands.*;

import fr.poloxpn.epicjump.data.Record;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private List<SubCommand> commands = new ArrayList<>();
    private final EpicJump plugin;

    public CommandManager(EpicJump plugin) {
        this.plugin = plugin;
        final Server server = this.plugin.getServer();
        server.getPluginCommand("epicjump").setExecutor(this);
        registerCommands(plugin);
    }

    public void registerCommand(SubCommand command) {
        commands.add(command);
    }

    public void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e<==========&8|&6EpicJump commands&8|&e==========>"));
        // Check if the sender has admin permission
        if(sender.hasPermission("epicjump.admin")) {
            for (SubCommand subCommand : commands) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7- &e" + subCommand.getUsage() + " : &7" + subCommand.getDescription()));
            }
        }
        else {
            // Display only the commands that the sender can use
            for (SubCommand subCommand : commands) {
                if(!subCommand.getPermission().equals("epicjump.admin")) {
                    sender.sendMessage(" - " + subCommand.getUsage() + " : " + subCommand.getDescription());
                }
            }
        }
    }

    public void showlist(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e<==========&8|&6EpicJump courses&8|&e==========>"));
        for (String course : plugin.getCourseManager().getCourseNames()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7- &e" + course));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {

        if(args == null || args.length == 0) {
            sender.sendMessage(Message.PREFIX +"Usage: /epicjump <subcommand> <args>");
            sender.sendMessage(Message.PREFIX +"or /epicjump help for help.");
            return true;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return true;
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
            showlist(sender);
            return true;
        }
        else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if(sender.hasPermission("epicjump.admin")) {
                plugin.reloadPlugin();
                sender.sendMessage(Message.PREFIX + ChatColor.GRAY + "EpicJump configuration reloaded.");
            }
            else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            }
            return true;
        }
        for (SubCommand subCommand : commands) {
            if (subCommand.is(args[0])) {
                if (subCommand.isAllowed(sender, true)) {
                    if (sender instanceof Player) {
                        subCommand.onExePlayer((Player) sender, args);
                    } else {
                        subCommand.onExeConsole((ConsoleCommandSender) sender, args);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (SubCommand subCommand : commands) {
                if (subCommand.getPermission().equals("epicjump.admin") && sender.hasPermission("epicjump.admin")) {
                    completions.add(subCommand.getAliases()[0]);
                } else if (!subCommand.getPermission().equals("epicjump.admin")) {
                    completions.add(subCommand.getAliases()[0]);
                }
            }
        }

        if(args.length == 2) {
            for (String course : plugin.getCourseManager().getCourseNames()) {
                if (course.startsWith(args[1])) {
                    completions.add(course);
                }
            }
        }
        if (args.length == 3) {
            if(args[0].equalsIgnoreCase("removecheckpoint")) {
                completions.add("this");
                completions.add("all");
            }
            if(args[0].equalsIgnoreCase("removehologram")) {
                completions.add("start");
                completions.add("end");
                completions.add("checkpoint");
                completions.add("leaderboard");
            }
            if(args[0].equalsIgnoreCase("addhologram")) {
                completions.add("start");
                completions.add("end");
                completions.add("checkpoint");
                completions.add("leaderboard");
            }
            if(args[0].equalsIgnoreCase("clearrecord")) {
                for(Record record : plugin.getLeaderboardManager().getLeaderboard(args[1])) {
                    completions.add(record.getPlayerName());
                }
            }
        }

        return completions;
    }

    public void registerCommands(EpicJump plugin) {
        registerCommand(new AddCheckpointSubCommand(plugin));
        registerCommand(new AddHologramSubCommand(plugin));
        registerCommand(new ClearLeaderboardSubCommand(plugin));
        registerCommand(new ClearRecordSubCommand(plugin));
        registerCommand(new CreateCourseSubCommand(plugin));
        registerCommand(new DeleteCourseSubCommand(plugin));
        registerCommand(new JoinParkourSubCommand(plugin));
        registerCommand(new RemoveCheckpointSubCommand(plugin));
        registerCommand(new RemoveHologramSubCommand(plugin));
        registerCommand(new SetEndSubCommand(plugin));
        registerCommand(new SetSpawnSubCommand(plugin));
        registerCommand(new StartCourseSubCommand(plugin));
        registerCommand(new SetStartSubCommand(plugin));
    }
}
