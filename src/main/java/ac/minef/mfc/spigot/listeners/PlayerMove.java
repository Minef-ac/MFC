package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (MFC.getInstance().getServer().getServerName().equalsIgnoreCase("hub")) {
            Location l = e.getPlayer().getLocation();
            if (l.getBlockY() <= 20) {
                e.getPlayer().performCommand("spawn");
            }
        }
    }

}