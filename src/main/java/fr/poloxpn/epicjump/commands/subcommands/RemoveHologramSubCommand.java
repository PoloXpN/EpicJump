package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import fr.poloxpn.epicjump.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RemoveHologramSubCommand extends SubCommand {

        private final EpicJump plugin;

        public RemoveHologramSubCommand(EpicJump plugin) {
            super("/epicjump removehologram <ParkourName> <id>", "Remove a hologram from a parkour.", "epicjump.admin", "removehologram");
            this.plugin = plugin;
        }

        @Override
        protected void onExePlayer(Player player, String... args) {
            if(args.length < 3) {
                showUsage(player);
                return;
            }
            if(!plugin.getCourseManager().courseExists(args[1])) {
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_NOT_FOUND));
                return;
            }
            switch (args[2]) {
                case "start":
                case "end":
                    if (plugin.getHologramManager().getHologram(args[1], args[2], 0) == null) {
                        player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.HOLOGRAM_NOT_FOUND));
                        return;
                    }
                    plugin.getHologramManager().removeHologram(args[1], args[2], 0);
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.REMOVE_HOLOGRAM).replaceAll("%course%", ChatColor.AQUA + args[1]));
                    break;
                case "checkpoint":
                case "leaderboard":
                    if (args.length < 4 || !Utils.isInteger(args[3])) {
                        player.sendMessage(Message.PREFIX + ChatColor.RED + "Usage: /epicjump removehologram <ParkourName> <checkpoint/leaderboard> <id>");
                        return;
                    } else if (plugin.getHologramManager().getHologram(args[1], args[2], Integer.parseInt(args[3])) == null) {
                        player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.HOLOGRAM_NOT_FOUND));
                        return;
                    }
                    plugin.getHologramManager().removeHologram(args[1], args[2], Integer.parseInt(args[3]));
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.REMOVE_HOLOGRAM).replaceAll("%course%", args[1]));
                    break;
            }
        }

        @Override
        protected void onExeConsole(ConsoleCommandSender player, String... args) {
            notAllowed(player);
        }
}
