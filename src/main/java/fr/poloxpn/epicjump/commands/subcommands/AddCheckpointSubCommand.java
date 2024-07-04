package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class AddCheckpointSubCommand extends SubCommand {

    private final EpicJump plugin;

    public AddCheckpointSubCommand(EpicJump plugin) {
        super("/epicjump addcheckpoint <ParkourName>", "Add a checkpoint to your current location.", "epicjump.admin","addcheckpoint");
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
            plugin.getCourseManager().addCheckpoint(args[1], player.getLocation().getBlock().getLocation());
            Material checkpointPlateMaterial = plugin.getMaterialFromConfig("checkpoint-pressure-plate");
            if (plugin.isPressurePlate(checkpointPlateMaterial)) {
                player.getLocation().getBlock().setType(checkpointPlateMaterial);
                player.sendMessage(Message.PREFIX + Message.PREFIX + Message.getMessage(Message.Messages.ADD_CHECKPOINT).replaceAll("%course%", args[1]));
            } else {
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_CHECKPOINT).replaceAll("%course%", args[1]));
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.INVALID_MATERIAL));
            }
        }
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender sender, String... args) {
        notAllowed(sender);
    }
}
