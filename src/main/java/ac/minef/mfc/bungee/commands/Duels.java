package ac.minef.mfc.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Duels extends Command {

    public Duels() {
        super("duels");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            p.connect(ProxyServer.getInstance().getServerInfo("duels"));
            if (!p.getServer().getInfo().equals(ProxyServer.getInstance().getServerInfo("duels"))) {
                p.sendMessage(new ComponentBuilder("Connecting to duels server...").color(ChatColor.GREEN).create());
            }
        }
    }

}
