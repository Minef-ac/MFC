package ac.minef.mfc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Vote extends Command {

    public Vote() {
        super("Vote", "group.default");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
            sender.sendMessage(new TextComponent("§§r §8§l┏§8━ §6§lVOTING LINKS\n" +
                    "    §r §8§l┃ §7§o \"Vote for the server and receive \n" +
                    "    §r §8§l┃ §7§o rewards in return, such as keys § money!\"\n" +
                    "    §r §8§l┃\n" +
                    "    §r §8§l┃ §e§lVOTE§8- §fminef.ac/vote\n" +
                    "    §r §8§l┃\n" +
                    "    §r §8§l┗§8━"));
    }
}