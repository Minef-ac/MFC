package ac.minef.mfc.commands;

import ac.minef.mfc.MFC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MFCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mfc")) {
            if (sender.hasPermission("group.administrator")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        MFC.getInstance().loadFile();
                        sender.sendMessage(ChatColor.GREEN + "MFC configuration reloaded.");
                        return false;
                    }
                }
                sender.sendMessage(ChatColor.RED + "Usage:\n/mfc - reload");
                return false;
            }
            sender.sendMessage(ChatColor.RED + "No permission.");
        }
        return false;
    }

}
