package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ClearLeaderboardSubCommand extends SubCommand {

    private final EpicJump plugin;

    public ClearLeaderboardSubCommand(EpicJump plugin) {
        super("/epicjump clearleaderboard <ParkourName>", "Clear the leaderboard of a parkour.", "epicjump.admin", "clearleaderboard");
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
            return;
        }
        plugin.getLeaderboardManager().clearLeaderboard(args[1]);
        player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.CLEAR_LEADERBOARD).replaceAll("%course%", args[1]));
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender sender, String... args) {
        notAllowed(sender);
    }
}
