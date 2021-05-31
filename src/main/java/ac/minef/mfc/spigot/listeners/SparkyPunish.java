package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import ac.sparky.api.events.SparkyPunishEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SparkyPunish implements Listener {

    @EventHandler
    public void onSparkyPunish(SparkyPunishEvent e) {
        e.setCancelled(true);
        MFC.getInstance().getServer().broadcastMessage("\n§6✘ §cMinef.ac §ehas removed§c "
                + e.getPlayer().getName() + "§e for cheating §6✘\n");
        if (!e.getPlayer().hasPermission("group.administrator")) {
            MFC.getInstance().executeCommand("ipban -s " + e.getPlayer().getName()
                    + " 30d Blacklisted Modifications\n[" + e.getCheckName() + ","
                    + e.getCheckType().charAt(0) + e.getCategory().charAt(0) + "#" + MFC.getInstance().getBans() + "]");
        }
    }

}