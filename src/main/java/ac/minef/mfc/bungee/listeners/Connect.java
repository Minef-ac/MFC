package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import ac.minef.mfc.bungee.Saves;
import net.dv8tion.jda.api.EmbedBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class Connect implements Listener {

    @EventHandler
    /*     */ public void onConnect(final PostLoginEvent e) {
        /* 271 */
        MFC.getInstance().UpdatePlayerCount();
        /* 272 */
        MFC.getInstance().SendEventMessageToDiscord(e.getPlayer().getName(), Saves.joinDiscordStyle);
        /* 273 */
        if (Saves.areSeparateServer) {
            /* 274 */
            ProxyServer.getInstance().getScheduler().schedule(MFC.getInstance(), new Runnable()
                    /*     */ {
                /*     */
                public void run() {
                    /* 277 */
                    ServerInfo serveri = e.getPlayer().getServer().getInfo();
                    /* 278 */
                    if (serveri == null) {
                        /*     */
                        return;
                        /*     */
                    }
                    /* 281 */
                    if (Saves.discordJoinedMessageEnabled) {
                        /* 282 */
                        MFC.getInstance().SendEventMessageToDiscord(e.getPlayer().getName(), Saves.leaveDiscordStyle, MFC.getInstance().serversChannelIDs.get(serveri.getName()).longValue());
                        /*     */
                    }
                    /* 284 */
                    if (Saves.separateServerCount) {
                        /* 285 */
                        MFC.getInstance().UpdatePlayerCountOnServer(serveri);
                        /*     */
                    }
                    /*     */
                }
                /* 288 */
            }, 1L, TimeUnit.SECONDS);
            /*     */
        }
        /*     */
    }

    void SendEventMessageToDiscord(String name, String structure) {
        /* 324 */
        if (MFC.getInstance().isEnabled && MFC.getInstance().isSetUp && MFC.getInstance().textChannel != null) {
            /* 325 */
            String toSend = structure.replaceAll("<name>", name);
            /* 326 */
            if (Saves.useBuilder) {
                /* 327 */
                EmbedBuilder builder = new EmbedBuilder();
                /* 328 */
                builder.setTitle(toSend).setColor(Saves.color);
                /* 329 */
                MFC.getInstance().textChannel.sendMessage(builder.build()).complete();
                /*     */
            } else {
                /* 331 */
                MFC.getInstance().textChannel.sendMessage(toSend).queue();
                /*     */
            }
            /*     */
        }
        /*     */
    }

}
