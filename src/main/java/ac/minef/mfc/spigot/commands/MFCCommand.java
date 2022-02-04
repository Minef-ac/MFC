package ac.minef.mfc.spigot.commands;

import ac.minef.mfc.spigot.MFC;
import ac.minef.mfc.spigot.Saves;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MFCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mfc")) {
            if (sender.hasPermission("group.administrator")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("sound")) {
                        if (args.length > 1 && MFC.getInstance().getServer().getPlayer(args[1]) != null) {
                            if (args.length > 2) {
                                Player p = MFC.getInstance().getServer().getPlayer(args[1]);
                                p.playSound(p.getLocation(), args[2], 1, 1);
                                return false;
                            }
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
                                return false;
                            }
                            sender.sendMessage(ChatColor.RED + "You must be a player for this!");
                            return false;
                        }
                        sender.sendMessage(ChatColor.RED + "Usage: sound <player> [sound]");
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("reward")) {
                        if (args.length > 1 && MFC.getInstance().getServer().getPlayer(args[1]) != null) {
                            if (args.length > 2) {
                                String rwcmd = args[2].replace("|", " ");
                                Player p = MFC.getInstance().getServer().getPlayer(args[1]);
                                p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1, 1);
                                MFC.getInstance().executeCommand(rwcmd);
                                p.sendMessage("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Mines " +
                                        ChatColor.DARK_GRAY + "» " + ChatColor.LIGHT_PURPLE + "You earned some Mine Loot!");
                                return false;
                            }
                        }
                        sender.sendMessage(ChatColor.RED + "Usage: reward <player> <cmd>");
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("reload")) {
                        MFC.getInstance().Reload();
                        sender.sendMessage(ChatColor.GREEN + "MFC configuration reloaded.");
                        return false;
                    }
                }
                sender.sendMessage(ChatColor.RED + "Usage:\n/mfc sound <player> [sound]\n/mfc reload\n/mfc reward <player> <cmd>");
                return false;
            }
            sender.sendMessage(ChatColor.RED + "No permission.");
        }
        return false;
    }

}