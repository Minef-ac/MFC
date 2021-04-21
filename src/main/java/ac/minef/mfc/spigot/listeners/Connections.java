package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Connections implements Listener {

    ItemStack hubItem;
    ItemStack menuItem;

    public Connections() {
        hubItem = new ItemStack(Material.COMPASS);
        ItemMeta hubMeta = hubItem.getItemMeta();
        hubMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.ITALIC + "Server Selector");
        hubItem.setItemMeta(hubMeta);

        menuItem = new ItemStack(Material.REDSTONE_COMPARATOR);
        ItemMeta menuMeta = menuItem.getItemMeta();
        menuMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.ITALIC + "Server Menu");
        menuItem.setItemMeta(menuMeta);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        if (MFC.getInstance().getServer().getServerName().equalsIgnoreCase("hub")
                || MFC.getInstance().getServer().getName().equalsIgnoreCase("hub")) {
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setItem(0, hubItem);
            e.getPlayer().getInventory().setItem(8, menuItem);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

}