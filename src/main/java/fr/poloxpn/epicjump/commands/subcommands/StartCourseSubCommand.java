package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StartCourseSubCommand extends SubCommand {

    private final EpicJump plugin;

    public StartCourseSubCommand(EpicJump plugin) {
        super("/epicjump startcourse <ParkourName>", "Start a parkour.", "epicjump.admin", "startcourse");
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
            plugin.getSessionManager().startCourse(player, plugin.getCourseManager().getCourse(args[1]));
        }
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender sender, String... args) {
        notAllowed(sender);
    }
}
