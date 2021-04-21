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
                if (channel.equals("DiscordDeath")) {
                    /* 552 */
                    String input = in.readUTF();
                    /* 553 */
                    if (MFC.getInstance().isEnabled) {
                        /* 561 */
                        MFC.getInstance().SendMessageToDiscord(input);
                        /*     */
                    }
                    /* 564 */
                } else if (channel.equals("DiscordChat")) {
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
                } else if (channel.equals("DiscordJoin")) {
                    /* 585 */
                    String input = in.readUTF();
                    /* 586 */
                    final String[] splitInput = input.split("-:-");
                    /* 587 */
                    final String playerName = splitInput[0];
                    /* 588 */
                    ProxyServer.getInstance().getScheduler().runAsync(MFC.getInstance(), new Runnable()
                            /*     */ {
                        /*     */
                        public void run() {
                            /* 591 */
                            if (!MFC.getInstance().playerInfo.containsKey(playerName)) {
                                System.out.print(1);
                                /* 592 */
                                boolean isVanished = splitInput[1].equals("true");
                                /* 593 */
                                MFC.getInstance().playerInfo.put(playerName, Boolean.valueOf(isVanished));
                                /* 594 */
                                if (!isVanished) {

                                    System.out.print(2);
                                    /* 595 */
                                    MFC.getInstance().UpdatePlayerCount();
                                    /* 596 */
                                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
                                    /* 597 */
                                    if (Saves.separateServerCount) {
                                        /* 598 */
                                        MFC.getInstance().UpdatePlayerCountOnServer(player.getServer().getInfo());
                                        /* 599 */
                                        if (Saves.discordJoinedMessageEnabled) {
                                            /* 600 */
                                            MFC.getInstance().SendEventMessageToDiscord(playerName, Saves.joinDiscordStyle, MFC.getInstance().serversChannelIDs.get(player.getServer().getInfo().getName()).longValue());
                                            /*     */
                                        }
                                        /*     */
                                    }
                                    /* 603 */
                                    if (Saves.discordJoinedMessageEnabled) {
                                        /* 604 */
                                        MFC.getInstance().SendEventMessageToDiscord(playerName, Saves.joinDiscordStyle);
                                        /*     */
                                    }
                                    /*     */
                                }
                                /*     */
                            }
                            /*     */
                        }
                        /*     */
                    });
                    /* 610 */
                } else if (channel.equals("DiscordVanish")) {
                    /* 611 */
                    String input = in.readUTF();
                    /* 612 */
                    if (MFC.getInstance().playerInfo.containsKey(input)) {
                        /* 613 */
                        MFC.getInstance().playerInfo.put(input, Boolean.valueOf(true));
                        /*     */
                    }
                    /* 615 */
                    MFC.getInstance().UpdatePlayerCount();
                    /* 616 */
                    MFC.getInstance().SendEventMessageToDiscord(input, Saves.leaveDiscordStyle);
                    /* 617 */
                    if (Saves.separateServerCount) {
                        /* 618 */
                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(input);
                        /* 619 */
                        MFC.getInstance().UpdatePlayerCountOnServer(player.getServer().getInfo());
                        /*     */
                    }
                    /* 621 */
                } else if (channel.equals("DiscordUnVanish")) {
                    /* 622 */
                    String input = in.readUTF();
                    /* 623 */
                    if (MFC.getInstance().playerInfo.containsKey(input)) {
                        /* 624 */
                        MFC.getInstance().playerInfo.put(input, Boolean.valueOf(false));
                        /*     */
                    }
                    /* 626 */
                    MFC.getInstance().UpdatePlayerCount();
                    /* 627 */
                    MFC.getInstance().SendEventMessageToDiscord(input, Saves.joinDiscordStyle);
                    /* 628 */
                    if (Saves.separateServerCount) {
                        /* 629 */
                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(input);
                        /* 630 */
                        MFC.getInstance().UpdatePlayerCountOnServer(player.getServer().getInfo());
                        /*     */
                    }
                    /*     */
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