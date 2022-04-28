package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.utils.ItemUtils;
import ac.minef.mfc.spigot.MFC;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Inventories implements Listener {

    private MFC plugin;

    private final ItemUtils itemUtils;

    public Inventories(MFC plugin) {
        this.plugin = plugin;
        this.itemUtils = new ItemUtils(this.plugin);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();
        if (inv != null && inv.getType().equals(InventoryType.ENCHANTING)) {
            e.setCancelled(true);
            new BukkitRunnable() {
                @Override public void run() {
                    MFC.getInstance().getServer().dispatchCommand(MFC.getInstance().getServer().getConsoleSender(),
                            "dm open enchant " + e.getPlayer().getName());
                }
            }.runTaskLater(MFC.getInstance(), 2);
        }
        return;
    }

}