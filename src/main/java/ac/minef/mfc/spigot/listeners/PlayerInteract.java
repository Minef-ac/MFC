package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (MFC.getInstance().getServer().getServerName().equalsIgnoreCase("hub")) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getPlayer().getInventory().getItemInHand().getType() == Material.COMPASS) {
                    e.setCancelled(true);
                    MFC.getInstance().executeCommand("dm open selector " + e.getPlayer().getName());
                }
                if (e.getPlayer().getInventory().getItemInHand().getType() == Material.REDSTONE_COMPARATOR) {
                    e.setCancelled(true);
                    MFC.getInstance().executeCommand("dm open selector " + e.getPlayer().getName());
                }
            }
        }
    }

}