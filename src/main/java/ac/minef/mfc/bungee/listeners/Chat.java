package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import ac.minef.mfc.bungee.Saves;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Chat implements Listener {

    @EventHandler
    /*     */ public void onChat(ChatEvent e) {
        /* 518 */
        if (Saves.onlyBungeecord &&
                /* 519 */       e.getSender() instanceof ProxiedPlayer &&
                /* 520 */       !e.isCommand()) {
            /* 521 */
            if (Saves.areSeparateServer) {
                /* 522 */
                ProxiedPlayer p = (ProxiedPlayer) e.getSender();
                /* 523 */
                String name = Saves.useMinecraftNicknames ? p.getDisplayName() : p.getName();
                /* 524 */
                String message = e.getMessage();
                /* 525 */
                message = message.replaceAll("@", "@ ");
                /* 526 */
                String toSend = Saves.discordChatStyle.replaceAll("<message>", message);
                /* 527 */
                toSend = toSend.replaceAll("<name>", name);
                /* 528 */
                toSend = ChatColor.stripColor(toSend);
                /* 529 */
                MFC.getInstance().SendMessageToDiscordChannel(toSend, MFC.getInstance().serversChannelIDs.get(p.getServer().getInfo().getName()));
                /*     */
            } else {
                /* 531 */
                ProxiedPlayer p = (ProxiedPlayer) e.getSender();
                /* 532 */
                String name = Saves.useMinecraftNicknames ? p.getDisplayName() : p.getName();
                /* 533 */
                String message = e.getMessage();
                /* 534 */
                message = message.replaceAll("@", "@ ");
                /* 535 */
                String toSend = Saves.discordChatStyle.replaceAll("<message>", message);
                /* 536 */
                toSend = toSend.replaceAll("<name>", name);
                /* 537 */
                toSend = ChatColor.stripColor(toSend);
                /* 538 */
                MFC.getInstance().SendMessageToDiscord(toSend);
                /*     */
            }
            /*     */
        }
        /*     */
    }

}