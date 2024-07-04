package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetStartSubCommand extends SubCommand {

    private final EpicJump plugin;
        public SetStartSubCommand(EpicJump plugin) {
            super("/epicjump setstart <ParkourName>", "Set the start of a parkour.", "epicjump.admin","setstart");
            this.plugin = plugin;
        }

        @Override
        protected void onExePlayer(Player player, String... args) {
            if(args.length < 2) {
                showUsage(player);
                return;
            }
            if(!plugin.getCourseManager().courseExists(args[1])) {
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_NOT_FOUND));
            } else {
                plugin.getCourseManager().setStart(args[1], player.getLocation().getBlock().getLocation());
                Material startPlateMaterial = plugin.getMaterialFromConfig("start-pressure-plate");
                if (plugin.isPressurePlate(startPlateMaterial)) {
                    player.getLocation().getBlock().setType(startPlateMaterial);
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_START_PRESSURE_PLATE).replaceAll("%course%", args[1]));
                } else {
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_START).replaceAll("%course%", args[1]));
                    player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.INVALID_MATERIAL));
                }
            }
        }

        @Override
        protected void onExeConsole(ConsoleCommandSender sender, String... args) {
            notAllowed(sender);
        }
}
