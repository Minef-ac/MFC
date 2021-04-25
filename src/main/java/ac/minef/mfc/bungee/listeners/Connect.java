package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import ac.minef.mfc.bungee.Saves;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Connect implements Listener {

    @EventHandler
    /*     */ public void onConnect(final PostLoginEvent e) {
        /* 271 */
        MFC.getInstance().UpdatePlayerCount();
        /* 272 */
        MFC.getInstance().SendEventMessageToDiscord(e.getPlayer(), Saves.joinDiscordStyle, "#26ce16");
        /* 273 */
        /*     */
    }

}
