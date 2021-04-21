package ac.minef.mfc.bungee.listeners;

import ac.minef.mfc.bungee.MFC;
import ac.minef.mfc.bungee.Saves;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerSwitch implements Listener {

    @EventHandler
    /*     */ public void onChangeServer(final ServerSwitchEvent e) {

        if (e.getPlayer().getServer() != null) {
            ProxiedPlayer target = e.getPlayer();
            ServerInfo server = target.getServer().getInfo();

            String join = ChatColor.DARK_GRAY + "[" +
                    ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.GREEN + target
                    + ChatColor.DARK_GRAY + " [" + ChatColor.GOLD +
                    server.getName() + ChatColor.DARK_GRAY + "]";

            for (ProxiedPlayer p : MFC.getInstance().getProxy().getPlayers()) {
                if (!p.equals(target)) {
                    p.sendMessage(new TextComponent(join));
                }
            }
        }

        /* 351 */
        if (Saves.separateServerCount) {
            /* 352 */
            ProxyServer.getInstance().getScheduler().schedule(MFC.getInstance(), new Runnable()
                    /*     */ {
                /*     */
                public void run() {
                    /* 355 */
                    for (Map.Entry<String, ServerInfo> serverInfo : ProxyServer.getInstance().getServers().entrySet()) {
                        /* 356 */
                        if (MFC.getInstance().serversChannelIDs.containsKey(serverInfo.getKey())) {
                            /* 357 */
                            int pastNumber = MFC.getInstance().serversPastCount.get(serverInfo.getKey());
                            /* 358 */
                            int numberNow = serverInfo.getValue().getPlayers().size();
                            /* 359 */
                            if (pastNumber != numberNow) {
                                /* 360 */
                                MFC.getInstance().serversPastCount.put(serverInfo.getKey(), Integer.valueOf(numberNow));
                                /* 361 */
                                MFC.getInstance().UpdatePlayerCountOnServer(serverInfo.getValue());
                                /*     */
                            }
                            /* 363 */
                            if (pastNumber < numberNow) {
                                /* 364 */
                                if (Saves.discordJoinedMessageEnabled) {
                                    /* 365 */
                                    String playerName = e.getPlayer().getName();
                                    /* 366 */
                                    if (Saves.onlyBungeecord) {
                                        /* 367 */
                                        MFC.getInstance().SendEventMessageToDiscord(playerName, Saves.joinDiscordStyle, MFC.getInstance().serversChannelIDs.get(serverInfo.getKey()).longValue());
                                        continue;
                                        /*     */
                                    }
                                    /* 369 */
                                    if (MFC.getInstance().playerInfo.containsKey(playerName) && !MFC.getInstance().playerInfo.get(playerName).booleanValue())
                                        /* 370 */
                                        MFC.getInstance().SendEventMessageToDiscord(playerName, Saves.joinDiscordStyle, MFC.getInstance().serversChannelIDs.get(serverInfo.getKey()).longValue());
                                    /*     */
                                }
                                /*     */
                                continue;
                                /*     */
                            }
                            /* 374 */
                            if (pastNumber > numberNow &&
                                    /* 375 */                     Saves.discordLeftMessageEnabled) {
                                /* 376 */
                                String playerName = e.getPlayer().getName();
                                /* 377 */
                                if (Saves.onlyBungeecord) {
                                    /* 378 */
                                    MFC.getInstance().SendEventMessageToDiscord(playerName, Saves.joinDiscordStyle, MFC.getInstance().serversChannelIDs.get(serverInfo.getKey()).longValue());
                                    continue;
                                    /*     */
                                }
                                /* 380 */
                                if (MFC.getInstance().playerInfo.containsKey(playerName) && !MFC.getInstance().playerInfo.get(playerName).booleanValue()) {
                                    /* 381 */
                                    MFC.getInstance().SendEventMessageToDiscord(e.getPlayer().getName(), Saves.leaveDiscordStyle, MFC.getInstance().serversChannelIDs.get(serverInfo.getKey()).longValue());
                                    /*     */
                                }
                                /*     */
                            }
                            /*     */
                            /*     */
                        }
                        /*     */
                        /*     */
                    }
                    /*     */
                }
                /* 389 */
            }, 1L, TimeUnit.SECONDS);
            /*     */
        }
        /*     */
    }

}