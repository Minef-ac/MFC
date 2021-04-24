package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (MFC.getInstance().getServer().getServerName().equalsIgnoreCase("factions")) {
            if (e.getEntity() != null && e.getEntity().getType().equals(EntityType.VILLAGER)) {
                int i = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                MFC.getInstance().getLogger().severe(i + " EMERALDS");
                e.getDrops().add(new ItemStack(Material.EMERALD, i));
            }
        }
    }

}