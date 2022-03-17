package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Armor implements Listener {

    public Armor() {
        if (Bukkit.getServerName().equalsIgnoreCase("factions")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(MFC.getInstance(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getInventory().getHelmet() != null && p.getInventory().getChestplate() != null
                            && p.getInventory().getLeggings() != null && p.getInventory().getBoots() != null) {
                        if (p.getInventory().getHelmet().getItemMeta().getDisplayName() != null
                                && p.getInventory().getChestplate().getItemMeta().getDisplayName() != null
                                && p.getInventory().getLeggings().getItemMeta().getDisplayName() != null
                                && p.getInventory().getBoots().getItemMeta().getDisplayName() != null) {
                            if (p.getInventory().getHelmet().getItemMeta().getDisplayName().contains("Weak")
                                    && p.getInventory().getChestplate().getItemMeta().getDisplayName().contains("Weak")
                                    && p.getInventory().getLeggings().getItemMeta().getDisplayName().contains("Weak")
                                    && p.getInventory().getBoots().getItemMeta().getDisplayName().contains("Weak")) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0));
                            }
                        }
                    }
                }
            }, 100, 0);
        }
    }

}