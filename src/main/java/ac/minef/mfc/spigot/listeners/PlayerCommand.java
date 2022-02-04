package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommand implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (MFC.getInstance().isProfanity(e.getPlayer(), e.getMessage().toLowerCase())) {
            e.setCancelled(true);
        }
        if (e.getMessage().toLowerCase().contains("f wild")) {
            e.setCancelled(true);
            e.getPlayer().performCommand("rtp");
        }
    }

}