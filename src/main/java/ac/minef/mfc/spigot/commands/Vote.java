package ac.minef.mfc.spigot.commands;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vote")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                TextComponent msg = new TextComponent("§ahttps://minef.ac/vote "
                        + "§8[" + "" + "§8]");
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§8[§eClick to open link§8]").create()));
                p.sendMessage(String.valueOf(msg));
            }
            sender.sendMessage("§ahttps://minef.ac/vote");
        }
        return false;
    }
}