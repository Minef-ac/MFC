package ac.minef.mfc.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vote")) {
            sender.sendMessage("\n" + "" + "\n" + "   §8§l┏§8━ §6§lVOTING LINKS\n" +
                    "  §r §8§l┃ " + "" + " §7§o Vote for the server and receive\n" +
                    "  §r §8§l┃ " + "" + " §7§o rewards in return, such as keys money!\n" +
                    "  §r §8§l┃ " + "" + " \n" +
                    "  §r §8§l┃ " + "" + " §e§lVOTE§8-§f https://minef.ac/vote" + "\n" +
                    "  §r §8§l┃ " + "\n" +
                    "  §r §8§l┗§8━");
        }
        return false;
    }
}
