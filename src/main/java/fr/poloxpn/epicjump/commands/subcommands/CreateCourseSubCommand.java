package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CreateCourseSubCommand extends SubCommand {

    private final EpicJump plugin;
    public CreateCourseSubCommand(EpicJump plugin) {
        super("/epicjump createcourse <ParkourName>", "Create a new parkour.", "epicjump.admin", "createcourse");
        this.plugin = plugin;
    }

    @Override
    protected void onExePlayer(Player player, String... args) {
        if(args.length < 2) {
            showUsage(player);
            return;
        }
        if(plugin.getCourseManager().courseExists(args[1])) {
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_ALREADY_EXISTS));
        } else {
            plugin.getCourseManager().createCourse(args[1]);
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_CREATED).replaceAll("%course%", args[1]));
        }
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender sender, String... args) {
        notAllowed(sender);
    }
}
