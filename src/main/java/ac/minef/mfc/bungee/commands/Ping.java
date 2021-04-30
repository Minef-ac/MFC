package ac.minef.mfc.bungee.commands;

import ac.minef.mfc.bungee.MFC;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ping extends Command {

    public Ping() {
        super("Ping", "group.default");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer p = (ProxiedPlayer) sender;
                p.sendMessage(new TextComponent("§8[§c§lMinef.ac§8] "
                        + "§7Your ping is§e " + p.getPing() + "ms§7."));
                return;
            }
            sender.sendMessage(new TextComponent("§cConsole must specify <player>"));
            return;
        }
        if (MFC.getInstance().getProxy().getPlayer(args[0]) != null) {
            ProxiedPlayer p = MFC.getInstance().getProxy().getPlayer(args[0]);
            sender.sendMessage(new TextComponent("§8[§c§lMinef.ac§8]§7 "
                    + p.getName() + "'s ping is§e " +
                    +p.getPing() + "ms§7."));
            return;
        }
        sender.sendMessage(new TextComponent("§8[§c§lMinef.ac§8]§c "
                + args[0] + " not found"));
    }
}