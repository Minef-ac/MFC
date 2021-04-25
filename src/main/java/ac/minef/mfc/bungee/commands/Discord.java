package ac.minef.mfc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Discord extends Command {


    public Discord() {
        super("discord");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            TextComponent msg = new TextComponent("§ahttps://minef.ac/discord");
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("§8[§eClick to open link§8]").create()));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                    "https://minef.ac/discord"));
            p.sendMessage(msg);
        }
    }
}