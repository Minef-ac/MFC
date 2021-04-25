package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import ac.minef.mfc.bungee.Saves;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessage implements Listener {

    @EventHandler
    /*     */ public void onPluginMessage(PluginMessageEvent e) {
        /* 547 */
        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            /* 548 */
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            /*     */
            try {
                /* 550 */
                String channel = in.readUTF();
                /* 551 */
                switch (channel) {
                    case "DiscordDeath": {
                        /* 552 */
                        String input = in.readUTF();
                        /* 553 */
                        if (MFC.getInstance().isEnabled) {
                            /* 561 */
                            MFC.getInstance().SendMessageToDiscord(input);
                            /*     */
                        }
                        /* 564 */
                        break;
                    }
                    case "DiscordChat": {
                        /* 565 */
                        String input = in.readUTF();
                        /* 566 */
                        if (MFC.getInstance().isEnabled) {
                            /* 568 */
                            String[] inputs = input.split("-:-");
                            /* 569 */
                            String name = inputs[0];
                            /* 570 */
                            String message = inputs[1];
                            /* 571 */
                            message = message.replaceAll("@", "@ ");
                            /* 572 */
                            String toSend = Saves.discordChatStyle.replaceAll("<message>", message);
                            /* 573 */
                            toSend = toSend.replaceAll("<name>", name);
                            /* 574 */
                            toSend = ChatColor.stripColor(toSend);
                            MFC.getInstance().SendMessageToDiscord(toSend);
                            /*     */
                        }
                        /* 584 */
                        break;
                    }
                    case "DiscordJoin": {
                        /* 585 */
                        String input = in.readUTF();
                        /* 586 */
                        final String[] splitInput = input.split("-:-");
                        /* 587 */
                        final String playerName = splitInput[0];
                        /* 588 */
                        /*     *//*     *//*     */
                        ProxyServer.getInstance().getScheduler().runAsync(MFC.getInstance(), () -> {
                            /* 591 */
                            if (!MFC.getInstance().playerInfo.containsKey(playerName)) {
                                /* 592 */
                                boolean isVanished = splitInput[1].equals("true");
                                /* 593 */
                                MFC.getInstance().playerInfo.put(playerName, isVanished);
                                /* 594 */
                                if (!isVanished) {
                                    System.out.print(2);
                                    /* 595 */
                                    MFC.getInstance().UpdatePlayerCount();
                                    /* 596 */
                                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
                                    if (Saves.discordJoinedMessageEnabled) {
                                        /* 604 */
                                        MFC.getInstance().SendEventMessageToDiscord(player, Saves.joinDiscordStyle);
                                        /*     */
                                    }
                                    /*     */
                                }
                                /*     */
                            }
                            /*     */
                        });
                        /* 610 */
                        break;
                    }
                    case "DiscordVanish": {
                        /* 611 */
                        String input = in.readUTF();
                        final String[] splitInput = input.split("-:-");
                        final String playerName = splitInput[0];
                        /* 612 */
                        if (MFC.getInstance().playerInfo.containsKey(input)) {
                            /* 613 */
                            MFC.getInstance().playerInfo.put(input, Boolean.TRUE);
                            /*     */
                        }
                        /* 615 */
                        MFC.getInstance().UpdatePlayerCount();
                        /* 616 */
                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
                        MFC.getInstance().SendEventMessageToDiscord(player, Saves.leaveDiscordStyle);
                        break;
                    }
                    case "DiscordUnVanish": {
                        /* 622 */
                        String input = in.readUTF();
                        final String[] splitInput = input.split("-:-");
                        final String playerName = splitInput[0];
                        /* 623 */
                        if (MFC.getInstance().playerInfo.containsKey(input)) {
                            /* 624 */
                            MFC.getInstance().playerInfo.put(input, Boolean.FALSE);
                            /*     */
                        }
                        /* 626 */
                        MFC.getInstance().UpdatePlayerCount();
                        /* 627 */
                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
                        MFC.getInstance().SendEventMessageToDiscord(player, Saves.joinDiscordStyle);
                        break;
                    }
                }
                /* 633 */
            } catch (IOException e1) {
                /* 634 */
                e1.printStackTrace();
                /*     */
            }
            /*     */
        }
        /*     */
    }

}