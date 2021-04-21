package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitch implements Listener {

    @EventHandler
    /*     */ public void onChangeServer(final ServerSwitchEvent e) {

        if (e.getPlayer().getServer() != null) {
            ProxiedPlayer target = e.getPlayer();
            ServerInfo server = target.getServer().getInfo();

            String join = ChatColor.DARK_GRAY + "[" +
                    ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.GREEN + target
                    + ChatColor.DARK_GRAY + " [" + ChatColor.GOLD +
                    server.getName() + ChatColor.DARK_GRAY + "]";

            for (ProxiedPlayer p : MFC.getInstance().getProxy().getPlayers()) {
                if (!p.equals(target)) {
                    p.sendMessage(new TextComponent(join));
                }
            }
        }
    }

}