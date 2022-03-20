package ac.minef.mfc.spigot.utils;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
    private final MFC plugin;

    public ItemUtils(MFC plugin) {
        this.plugin = plugin;
    }

    public ItemStack getLapis() {
        ItemStack item;
        item = new ItemStack(Material.INK_SACK, 64, (short) 4);
        return item;
    }
}