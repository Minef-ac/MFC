package ac.minef.mfc.listeners;

import ac.minef.mfc.MFC;
import ac.sparky.api.events.SparkyPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SparkyPunish implements Listener {

    @EventHandler
    public void onSparkyPunish(SparkyPunishEvent e) {
        e.setCancelled(true);

        // DISABLE LATER

        MFC.getInstance().getLogger().warning("Sparky punishment for "
                + e.getPlayer().getName() + " cancelled!");
    }

}