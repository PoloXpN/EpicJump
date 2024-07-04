package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DeleteCourseSubCommand extends SubCommand {

    private final EpicJump plugin;

    public DeleteCourseSubCommand(EpicJump plugin) {
        super("/epicjump deletecourse <ParkourName>", "Delete a parkour.", "epicjump.admin", "deletecourse");
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
            // Force quit the course for every player in it
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (plugin.getSessionManager().getPlayerSession(p) != null && plugin.getSessionManager().getPlayerSession(p).getCourse().getName().equals(args[1])) {
                    plugin.getSessionManager().endCourse(p);
                    p.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_DELETED_BY_ADMIN));
                }
            }

            plugin.getCourseManager().deleteCourse(args[1]);
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_DELETED).replaceAll("%course%", args[1]));
        }
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender sender, String... args) {
        notAllowed(sender);
    }
}
