package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Random;

public class FoodLevelChange implements Listener {

    Random r;

    public FoodLevelChange(MFC mfc) {
        r = new Random();
    }

    int next() {
        if (r.nextBoolean()) {
            return 1;
        } else {
            return 2;
        }
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent e) {
        if (e.getEntityType() == EntityType.PLAYER && !e.isCancelled()) {
            Player p = (Player) e.getEntity();
            if (p.isSprinting() && next() == 1) {
                e.setCancelled(true);
                return;
            }
            if (p.isSprinting() && next() == 1) {
                e.setCancelled(true);
            }
        }
    }

}