package ac.minef.mfc.spigot.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().getWorld().getName().equalsIgnoreCase("world")) return;
        if (e.getPlayer().getLocation().getBlockX() < 50 &&
                e.getPlayer().getLocation().getBlockZ() < 50) {
            if (e.getClickedBlock() == null || e.getClickedBlock().getType() == null) return;
            if (e.getClickedBlock().getType() == Material.ANVIL) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                p.performCommand("blacksmith");
                return;
            }
            if (e.getClickedBlock().getType() == Material.HOPPER) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                p.performCommand("tinkerer");
            }
        }
    }

}