package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommand implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (MFC.getInstance().isProfanity(e.getPlayer(), e.getMessage().toLowerCase())) {
            e.setCancelled(true);
        }
        if (e.getPlayer().hasPermission("group.administrator")) return;

        if (e.getMessage().toLowerCase().contains("/f wild")
                || e.getMessage().toLowerCase().contains("/wild")) {
            e.setCancelled(true);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                    "wild " + e.getPlayer().getDisplayName() +  " world");
        }
    }

}