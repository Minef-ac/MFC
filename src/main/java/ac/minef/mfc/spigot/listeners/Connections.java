package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;

public class Connections implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (Player p: MFC.getInstance().getServer().getOnlinePlayers()) {
            MFC.getInstance().executeCommand("mfc sound " + p.getDisplayName() + " NOTE.PLING");
        }
        if (e.getPlayer().hasPermission("deluxetags.tag.Owner")) {
            MFC.getInstance().executeCommand("tags set " + e.getPlayer().getDisplayName() + " Owner");
            return;
        }
        if (e.getPlayer().hasPermission("deluxetags.tag.Administrator")) {
            MFC.getInstance().executeCommand("tags set " + e.getPlayer().getDisplayName() + " Administrator");
            return;
        }
        if (e.getPlayer().hasPermission("deluxetags.tag.Moderator")) {
            MFC.getInstance().executeCommand("tags set " + e.getPlayer().getDisplayName() + " Moderator");
            return;
        }
        if (!e.getPlayer().hasPlayedBefore()) {
            if (e.getPlayer().hasPermission("deluxetags.tag.Premium"))
            {
                MFC.getInstance().executeCommand("tags set " + e.getPlayer().getDisplayName() + " Premium");
            } else {
                MFC.getInstance().executeCommand("tags set " + e.getPlayer().getDisplayName() + " A");
            }
        }
        Player target = e.getPlayer();
        if (isVanished(target)) {
            e.setJoinMessage(null);
            return;
        }
        String join = ChatColor.DARK_GRAY + "[" +
                ChatColor.GREEN + ChatColor.BOLD + "+" + ChatColor.DARK_GRAY + "] "
                + ChatColor.GREEN + target.getName();
        e.setJoinMessage(join);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player target = e.getPlayer();
        if (isVanished(target)) {
            e.setQuitMessage(null);
            return;
        }
        String quit = ChatColor.DARK_GRAY + "[" +
                ChatColor.RED + ChatColor.BOLD + "-" + ChatColor.DARK_GRAY + "] "
                + ChatColor.GREEN + target.getName();
        e.setQuitMessage(quit);
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

}