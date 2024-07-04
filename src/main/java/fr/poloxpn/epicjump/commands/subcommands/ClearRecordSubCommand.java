package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ClearRecordSubCommand extends SubCommand {

    private final EpicJump plugin;

    public ClearRecordSubCommand(EpicJump plugin) {
        super("/epicjump clearrecord <ParkourName> <PlayerName>", "Clear the record of a player from a specific parkour.", "epicjump.admin","clearrecord");
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
        if(!plugin.getLeaderboardManager().hasRecord(args[1], args[2])) {
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.RECORD_NOT_FOUND));
            return;
        }
        plugin.getLeaderboardManager().removeRecord(args[1], args[2]);
        player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.CLEAR_RECORD).replaceAll("%course%", args[1]).replaceAll("%player%", args[2]));
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender sender, String... args) {
        notAllowed(sender);
    }
}
