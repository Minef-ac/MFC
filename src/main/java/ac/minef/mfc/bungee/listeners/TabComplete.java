package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabComplete implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        if (!event.isCancelled()) {
            String partialName = (event.getCursor().toLowerCase().lastIndexOf(' ') >= 0) ? event.getCursor().toLowerCase().substring(event.getCursor().toLowerCase().lastIndexOf(' ') + 1) : event.getCursor().toLowerCase();
            for (ProxiedPlayer player : MFC.getInstance().getProxy().getPlayers()) {
                if (player.getName().toLowerCase().startsWith(partialName) && !event.getSuggestions().contains(player.getName()))
                    event.getSuggestions().add(player.getName());
            }
        }
    }

}