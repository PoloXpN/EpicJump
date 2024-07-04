package fr.poloxpn.epicjump.commands.subcommands;

import fr.poloxpn.epicjump.EpicJump;
import fr.poloxpn.epicjump.commands.SubCommand;
import fr.poloxpn.epicjump.message.Message;
import fr.poloxpn.epicjump.parkour.Hologram;
import fr.poloxpn.epicjump.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class AddHologramSubCommand extends SubCommand {

    private final EpicJump plugin;

    public AddHologramSubCommand(EpicJump plugin) {
        super("/epicjump addhologram <ParkourName> <HologramType> <id>", "Add a hologram to a parkour.", "epicjump.admin", "addhologram");
        this.plugin = plugin;
    }

    @Override
    protected void onExePlayer(Player player, String... args) {
        if(args.length < 3) {
            showUsage(player);
            return;
        }
        // Check if the course exists
        if(!plugin.getCourseManager().courseExists(args[1])) {
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.COURSE_NOT_FOUND));
            return;
        }
        // Check if the hologram already exists
        Hologram hologram = plugin.getHologramManager().getHologram(args[1],args[2], 0);
        if(args.length > 3 && Utils.isInteger(args[3])) {
            hologram = plugin.getHologramManager().getHologram(args[1],args[2], Integer.parseInt(args[3]));
        }
        if(hologram != null) {
            player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.HOLOGRAM_ALREADY_EXISTS));
            return;
        }

        // verify which type of hologram is arg[3] : checkpoint, start, end, leaderboard
        // /epicjoin addhologram <course> <type> <id>

        switch (args[2]) {
            case "start":
                plugin.getHologramManager().addHologram(new Hologram(args[1], args[2], 0, player.getLocation(), Message.getMessage(Message.Messages.HOLOGRAM_START_LINE_1).replaceAll("%course%", args[1]), Message.getMessage(Message.Messages.HOLOGRAM_START_LINE_2)));
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_HOLOGRAM).replaceAll("%course%", args[1]));
                break;
            case "end":
                plugin.getHologramManager().addHologram(new Hologram(args[1], args[2], 0, player.getLocation(), Message.getMessage(Message.Messages.HOLOGRAM_END_LINE_1).replaceAll("%course%", args[1]), Message.getMessage(Message.Messages.HOLOGRAM_END_LINE_2)));
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_HOLOGRAM).replaceAll("%course%", args[1]));
                break;
            case "checkpoint":
                if (args.length < 4 || !Utils.isInteger(args[3])) {
                    player.sendMessage(Message.PREFIX + ChatColor.RED + "Usage: /epicjump addhologram <ParkourName> <checkpoint> <id>");
                    return;
                }
                plugin.getHologramManager().addHologram(new Hologram(args[1], args[2], Integer.parseInt(args[3]), player.getLocation(), Message.getMessage(Message.Messages.HOLOGRAM_CHECKPOINT_LINE_1).replaceAll("%course%", args[1]), Message.getMessage(Message.Messages.HOLOGRAM_CHECKPOINT_LINE_2).replaceAll("%id%", args[3])));
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_HOLOGRAM).replaceAll("%course%", args[1]));
                break;
            case "leaderboard":
                if (args.length < 4 || !Utils.isInteger(args[3])) {
                    player.sendMessage(Message.PREFIX + ChatColor.RED + "Usage: /epicjump addhologram <ParkourName> <leaderboard> <id>");
                    return;
                }
                plugin.getHologramManager().addHologram(new Hologram(args[1], args[2], Integer.parseInt(args[3]), player.getLocation(), plugin.getLeaderboardManager().getLeaderboardLines(args[1])));
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.ADD_HOLOGRAM).replaceAll("%course%", args[1]));
                break;
            default:
                player.sendMessage(Message.PREFIX + Message.getMessage(Message.Messages.INVALID_HOLOGRAM_TYPE));
                showUsage(player);
                break;
        }
    }

    @Override
    protected void onExeConsole(ConsoleCommandSender player, String... args) {
        notAllowed(player);
    }
}
