package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Quit implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        if (e.getPlayer().getServer() != null) {
            ServerInfo server = e.getPlayer().getServer().getInfo();
            ProxiedPlayer target = e.getPlayer();
            String leave = ChatColor.DARK_GRAY + "[" +
                    ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.GREEN + target
                    + ChatColor.DARK_GRAY + " [" + ChatColor.GOLD +
                    server.getName() + ChatColor.DARK_GRAY + "]";
            for (ProxiedPlayer p : MFC.getInstance().getProxy().getPlayers()) {
                if (!p.equals(target)) {
                    p.sendMessage(new TextComponent(leave));
                }
            }
        }
    }
}