package ac.minef.mfc.spigot.commands;

import ac.minef.mfc.spigot.MFC;
import ac.minef.mfc.spigot.Saves;
import org.bukkit.ChatColor;
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
                    if (args[0].equalsIgnoreCase("bp")) {
                        Saves.backpacks = Saves.backpacks + 1;
                        MFC.getInstance().getConfig().set("backpacks", Saves.backpacks);
                        long bp = Saves.backpacks;
                        if (args.length > 1 && MFC.getInstance().getServer().getPlayer(args[1]) != null) {
                            if (args[2] != null) {
                                if (args[2].equalsIgnoreCase("old")) {
                                    MFC.getInstance().executeCommand("bp give " + args[1]
                                            + " 1 &a&oOld_Backpack"
                                            + "_&8[&c" + bp + "&8]");
                                    sender.sendMessage(ChatColor.GREEN + "Given " + args[1] + " default backpack");
                                    return false;
                                }
                                if (args[2].equalsIgnoreCase("used")) {
                                    MFC.getInstance().executeCommand("bp give " + args[1]
                                            + " 2 &9&oUsed_Backpack"
                                            + "_&8[&c" + bp + "&8]");
                                    return false;
                                }
                                if (args[2].equalsIgnoreCase("new")) {
                                    MFC.getInstance().executeCommand("bp give " + args[1]
                                            + " 4 &5&oNew_Backpack"
                                            + "_&8[&c" + bp + "&8]");
                                    return false;
                                }
                                if (args[2].equalsIgnoreCase("miner")) {
                                    MFC.getInstance().executeCommand("bp give " + args[1]
                                            + " 6 &6&oMiner_Backpack"
                                            + "_&8[&c" + bp + "&8]");
                                    return false;
                                }
                            }
                            MFC.getInstance().executeCommand("bp give " + args[1]
                                    + " 1 &a&oOld_Backpack"
                                    + "_&8[&c" + bp + "&8]");
                            sender.sendMessage(ChatColor.GREEN + "Given " + args[1] + " default backpack");
                            return false;
                        }
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            MFC.getInstance().executeCommand("bp give " + p.getName()
                                    + " 1 &a&oOld_Backpack"
                                    + "_&8[&c" + bp + "&8]");
                            p.sendMessage(ChatColor.GREEN + "Given self default backpack");
                            return false;
                        }
                    }
                    if (args[0].equalsIgnoreCase("reload")) {
                        MFC.getInstance().Reload();
                        sender.sendMessage(ChatColor.GREEN + "MFC configuration reloaded.");
                        return false;
                    }
                }
                sender.sendMessage(ChatColor.RED + "Usage:\n/mfc bp [player] [old/used/new/miner]\n/mfc reload");
                // miner npc has quest to give miner backpack ?
                return false;
            }
            sender.sendMessage(ChatColor.RED + "No permission.");
        }
        return false;
    }

}