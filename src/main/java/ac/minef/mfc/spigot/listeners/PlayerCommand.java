package ac.minef.mfc.spigot.listeners;

import ac.minef.mfc.spigot.MFC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommand implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String m = e.getMessage().toLowerCase();
        if (!e.getPlayer().hasPermission("group.moderator")) {
            if (m.contains("nigg") || m.contains("niqq")
                    || m.contains("n1g") || m.contains("n3gro")
                    || m.contains("negro") || m.contains("kys")
                    || m.contains("fuck") || m.contains("shit") || m.contains("bitch")
                    || m.contains("btch") || m.contains("fck")
                    || m.contains("cunt") || m.contains("penis")
                    || m.contains("peenis") || m.contains("pen15")
                    || m.contains("fag") || m.contains("phag")
                    || m.contains("gay") || m.contains("queer")
                    || m.contains("blackie") || m.equalsIgnoreCase("nig")
                    || m.contains("blacky") || m.contains("marlowe")
                    || m.contains("dick") || m.contains("cock")
                    || m.contains("nigger") || m.contains("aggo")
                    || m.contains("tits") || m.contains("puss")
                    || m.contains("0ck") || m.contains("aqg")
                    || m.contains("sex")) {

                TextComponent msg = new TextComponent("\n        §8[§c!§8]§e "
                        + e.getPlayer().getName() + " §cfailed cmd§7: §e" + e.getMessage() + "\n");
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§8[§eClick to warn§8]").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        "/warn " + e.getPlayer().getName() + " Inappropriate Language -s"));

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("group.moderator"))
                        p.sendMessage(String.valueOf(msg));
                }

                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Inappropriate language detected.");
            }
        }
    }

}