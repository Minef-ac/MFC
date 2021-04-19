package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import ac.minef.mfc.bungee.Saves;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class Quit implements Listener {

    @EventHandler
    /*     */ public void onQuit(PlayerDisconnectEvent e) {
        /* 296 */
        if (Saves.onlyBungeecord) {
            /* 297 */
            MFC.getInstance().SendEventMessageToDiscord(e.getPlayer().getName(), Saves.leaveDiscordStyle);
            /*     */
        } else {
            /* 299 */
            if (MFC.getInstance().playerInfo.containsKey(e.getPlayer().getName()) && !MFC.getInstance().playerInfo.get(e.getPlayer().getName()).booleanValue() &&
                    /* 300 */         Saves.discordLeftMessageEnabled) {
                /* 301 */
                MFC.getInstance().SendEventMessageToDiscord(e.getPlayer().getName(), Saves.leaveDiscordStyle);
                /*     */
            }
            /*     */
            /* 304 */
            MFC.getInstance().playerInfo.remove(e.getPlayer().getName());
            /*     */
        }
        /* 306 */
        MFC.getInstance().UpdatePlayerCount();
        /* 307 */
        if (Saves.areSeparateServer) {
            /* 308 */
            final ServerInfo serveri = e.getPlayer().getServer().getInfo();
            /* 309 */
            if (Saves.discordLeftMessageEnabled) {
                /* 310 */
                MFC.getInstance().SendEventMessageToDiscord(e.getPlayer().getName(), Saves.leaveDiscordStyle, MFC.getInstance().serversChannelIDs.get(serveri.getName()).longValue());
                /*     */
            }
            /* 312 */
            ProxyServer.getInstance().getScheduler().schedule(MFC.getInstance(), new Runnable()
                    /*     */ {
                /*     */
                public void run() {
                    /* 315 */
                    if (Saves.separateServerCount) {
                        /* 316 */
                        MFC.getInstance().UpdatePlayerCountOnServer(serveri);
                        /*     */
                    }
                    /*     */
                }
                /* 319 */
            }, 1L, TimeUnit.SECONDS);
            /*     */
        }
        /*     */
    }

}