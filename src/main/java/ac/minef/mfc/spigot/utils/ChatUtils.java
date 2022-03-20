package ac.minef.mfc.spigot.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String replaceColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}