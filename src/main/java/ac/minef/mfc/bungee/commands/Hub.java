package ac.minef.mfc.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Hub extends Command {


    public Hub() {
        super("Hub", "group.default", "lobby");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            p.connect(ProxyServer.getInstance().getServerInfo("hub"));
            if (!p.getServer().getInfo().equals(ProxyServer.getInstance().getServerInfo("hub"))) {
                p.sendMessage(new ComponentBuilder("Connecting to hub server...").color(ChatColor.GREEN).create());
            }
        }
    }
}