package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RemoveCheckpointSubCommand extends SubCommand {

    private final EpicJump plugin;

    public RemoveCheckpointSubCommand(EpicJump plugin) {
        super("/epicjump removecheckpoint <ParkourName> <this/all>", "Remove a checkpoint from a parkour.", "epicjump.admin","removecheckpoint");
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
        } else if (!plugin.getCourseManager().hasCheckpoints(args[1])) {
            player.sendMessage("This parkour does not have any checkpoints.");
        } else if (args.length > 2 && args[2].equals("all")) {
            plugin.getCourseManager().removeAllCheckpoints(args[1]);
            player.sendMessage("All checkpoints removed for the parkour '" + args[1] + "'.");
        } else {
            // Check if the player is on a checkpoint
            if (!plugin.getCourseManager().isCheckpoint(args[1], player.getLocation().getBlock().getLocation())) {
                player.sendMessage("There is no lo, please move to remove it.");
                return;
            }
            plugin.getCourseManager().removeCheckpoint(args[1], player.getLocation().getBlock().getLocation());
            player.sendMessage("Checkpoint removed for the parkour '" + args[1] + "'.");
        }
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender player, String... args) {
        notAllowed(player);
    }
}
