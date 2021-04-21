package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (!e.getPlayer().isOp()) {
            if (MFC.getInstance().getServer().getServerName().equalsIgnoreCase("hub")
                    || MFC.getInstance().getServer().getName().equalsIgnoreCase("hub")) {
                e.setCancelled(true);
            }
        }
    }

}