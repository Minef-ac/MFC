package ac.minef.mfc.bungee.commands;

import ac.minef.mfc.bungee.MFC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class MFCB
        extends Command {
    MFC m;

    public MFCB(MFC m) {
        super("mfcb");
        this.m = m;
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("group.administrator")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("bc") && args.length > 1) {
                    StringBuilder msg = new StringBuilder();
                    for (String arg : args) msg.append(arg).append(" ");
                    for (ProxiedPlayer p : MFC.getInstance().getProxy().getPlayers()) {

                        p.sendMessage(new TextComponent(ChatColor.DARK_GRAY + "["
                                + ChatColor.RED + ChatColor.BOLD
                                + "Minef.ac" + ChatColor.DARK_GRAY + "] "
                                + msg.toString().replaceAll("&", "§")));
                    }
                    MFC.getInstance().getLogger().info(msg.toString());
                    return;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender instanceof ProxiedPlayer) {
                        sender.sendMessage(new TextComponent(ChatColor.GREEN + "Reloaded!"));
                    }
                    this.m.reloadConfig();
                    return;
                }
            }
            sender.sendMessage(new TextComponent(ChatColor.RED + "Usage:\n/mfc bc <string>" +
                    "\n/mfc reload"));
        }

    }
}