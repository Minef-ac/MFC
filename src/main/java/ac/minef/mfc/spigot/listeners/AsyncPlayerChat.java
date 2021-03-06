package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import ac.minef.mfc.spigot.Saves;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class AsyncPlayerChat implements Listener {

    public Set<Player> chatTimer = new HashSet<>();

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        if (!chatTimer.contains(e.getPlayer())) {
            String m = e.getMessage().toLowerCase();
            if (!e.getPlayer().hasPermission("group.donor")) {
                chatTimer.add(e.getPlayer());
                if (MFC.getInstance().isProfanity(e.getPlayer(), m)) {
                    TextComponent msg = new TextComponent("\n        §8[§c!§8]§e "
                            + e.getPlayer().getName() + " §cfailed chat§7: §e" + e.getMessage() + "\n");
                    msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("§8[§eClick to warn§8]").create()));
                    msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                            "/warn " + e.getPlayer().getName() + " Inappropriate Language -s"));

                    e.setCancelled(true);

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("group.moderator"))
                            p.spigot().sendMessage(msg);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            chatTimer.remove(e.getPlayer());
                        }
                    }.runTaskLater(MFC.getInstance(), 50);
                    return;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        chatTimer.remove(e.getPlayer());
                    }
                }.runTaskLater(MFC.getInstance(), 50);
            }
            tagCheck(e.getPlayer(), m);
            return;
        }
        e.setCancelled(true);
        e.getPlayer().sendMessage(ChatColor.RED + "Please wait before using chat again");
    }

    public void tagCheck(Player sender, String m) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.equals(sender)) {
                if (m.contains(p.getName().toLowerCase())) {
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 2, 2);
                    // make this an action bar   p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC
                    //        + sender.getName() + " tagged you");
                    sender.playSound(p.getLocation(), Sound.CLICK, 2, 2);
                }
            }
        }
    }

}