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
    public void onChat(ChatEvent e) {
        if (Saves.onlyBungeecord && !e.isCommand()
                && e.getSender() instanceof ProxiedPlayer) {
            MFC.getInstance().getLogger().severe("BUNGEE CHAT CALL");
            ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            String name = Saves.useMinecraftNicknames ? p.getDisplayName() : p.getName();
            String message = e.getMessage();
            message = message.replaceAll("@", "@ ");
            String toSend = Saves.discordChatStyle.replaceAll("<message>", message);
            toSend = toSend.replaceAll("<name>", name);
            toSend = ChatColor.stripColor(toSend);
            // MFC.getInstance().SendMessageToDiscord(toSend);
        }
    }

}