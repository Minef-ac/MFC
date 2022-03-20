package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.utils.ItemUtils;
import ac.minef.mfc.spigot.MFC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.*;

public class Inventory implements Listener {

    private MFC plugin;

    private final ItemUtils itemUtils;

    public Inventory(MFC plugin) {
        this.plugin = plugin;
        this.itemUtils = new ItemUtils(this.plugin);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent inventoryOpenEvent) {
        org.bukkit.inventory.Inventory inventory = inventoryOpenEvent.getInventory();
        if (inventory != null && inventory.getType().equals(InventoryType.ENCHANTING)) {
            inventory.setItem(1, this.itemUtils.getLapis());
        }
        return;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent) {
        org.bukkit.inventory.Inventory inventory = inventoryCloseEvent.getInventory();
        if (inventory != null && inventory.getType().equals(InventoryType.ENCHANTING)) {
            inventory.setItem(1, null);
        }
    }

    @EventHandler
    public void onItemEnchantment(EnchantItemEvent enchantItemEvent) {
        org.bukkit.inventory.Inventory inventory = enchantItemEvent.getInventory();
        if (inventory != null && inventory.getType().equals(InventoryType.ENCHANTING)) {
            inventory.setItem(1, null);
            inventory.setItem(1, this.itemUtils.getLapis());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        org.bukkit.inventory.Inventory inventory = event.getClickedInventory();
        Player player = (Player)event.getWhoClicked();
        if (inventory != null && inventory.getType().equals(InventoryType.ENCHANTING)) {
            if (event.getSlot() == 1) {
                event.setCancelled(true);
                player.updateInventory();
            }
            // inventoryClickEvent.setCancelled(true);
            // player.updateInventory();
        }
    }

}