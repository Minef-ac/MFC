package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import ac.minef.mfc.spigot.Saves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    /*     */ public void onChat(AsyncPlayerChatEvent e) {
        /* 171 */
        String format = e.getFormat();
        /* 172 */
        if (Saves.debug) {
            /* 173 */
            MFC.getInstance().getLogger().info("D Sender Name: " + e.getPlayer().getName() + ", Format: " + format + ", Message: " + e.getMessage());
            /*     */
        }
        /* 175 */
        for (String prefix : Saves.bannedPrefixes) {
            /* 176 */
            if (e.getMessage().startsWith(prefix)) {
                /*     */
                return;
                /*     */
            }
            /*     */
        }
        /* 180 */
        for (String text : Saves.bannedWords) {
            /* 181 */
            if (e.getMessage().contains(text)) {
                /*     */
                return;
                /*     */
            }
            /*     */
        }
        /* 185 */
        for (String prefix : Saves.bannedFPrefixes) {
            /* 186 */
            if (format.startsWith(prefix)) {
                /*     */
                return;
                /*     */
            }
            /*     */
        }
        /* 190 */
        for (String text : Saves.bannedFWords) {
            /* 191 */
            if (format.contains(text)) {
                /*     */
                return;
                /*     */
            }
            /*     */
        }
        /* 195 */
        String name = Saves.useMinecraftNicknames ? e.getPlayer().getDisplayName() : e.getPlayer().getName();
        /* 196 */
        String message = e.getMessage();
        /* 197 */
        if (Saves.isBungeeCord) {
            /* 198 */
            MFC.getInstance().getLogger().info("Sending it: " + e.getFormat());
            /* 199 */
            MFC.getInstance().sendToBungeeCord(e.getPlayer(), "DiscordChat", name + "-:-" + message);
            /*     */
        }
        /*     */
    }

}