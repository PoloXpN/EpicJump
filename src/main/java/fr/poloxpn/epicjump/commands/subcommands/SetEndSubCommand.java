package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetEndSubCommand extends SubCommand {

    private final EpicJump plugin;
    public SetEndSubCommand(EpicJump plugin) {
        super("/epicjump setend <ParkourName>", "Set the end of a parkour.", "epicjump.admin","setend");
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
            plugin.getCourseManager().setEnd(args[1], player.getLocation().getBlock().getLocation());
            Material endPlateMaterial = plugin.getMaterialFromConfig("end-pressure-plate");
            if (plugin.isPressurePlate(endPlateMaterial)) {
                player.getLocation().getBlock().setType(endPlateMaterial);
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_END_PRESSURE_PLATE).replaceAll("%course%", args[1]));
            } else {
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_END).replaceAll("%course%", args[1]));
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.INVALID_MATERIAL));
            }
        }
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender sender, String... args) {
        notAllowed(sender);
    }
}
