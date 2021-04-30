package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.HashSet;
import java.util.Set;

public class CreatureSpawn implements Listener {

    public static Set<LivingEntity> artificiallySpawned = new HashSet<>();

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (MFC.getInstance().getServer().getServerName().equalsIgnoreCase("factions")) {
            if (e.getEntity().getType() == EntityType.CREEPER
                    || e.getEntity().getType() == EntityType.VILLAGER) {
                if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
                        || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
                        || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
                    artificiallySpawned.add(e.getEntity());
                }
            }
        }
    }

}