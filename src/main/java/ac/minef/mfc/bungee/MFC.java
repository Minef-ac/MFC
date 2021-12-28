package ac.minef.mfc.bungee;

import ac.minef.mfc.bungee.commands.*;
import ac.minef.mfc.bungee.listeners.*;
import com.google.common.io.ByteStreams;
import com.tjplaysnow.discord.object.Bot;
import litebans.api.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.awt.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MFC extends Plugin {
    private static final String avatarURL = "https://crafatar.com/avatars/<uuid>?overlay&size=64";

    private static MFC instance;
    // public final String PREFIX = "!";
    public Bot bot;
    public Map<String, Boolean> playerInfo = new HashMap<>();
    public boolean isEnabled = true;
    public boolean isSetUp = false;
    // TextChannel commandChannel;
    // boolean isUsingVanishOnSpigot = false;
    Configuration config;
    int bcMsg;

    public static MFC getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        ProxyServer.getInstance().getPluginManager().registerListener(this, new Chat());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Connect());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessage());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Quit());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerSwitch());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new TabComplete());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Discord());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Duels());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MFCB(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Minefactions());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Ping());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Vote());

        bungeeBroadcast();

        reloadConfig();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(":white_check_mark:  **Minef.ac network online**").setColor(Color.decode("#FFFF00"));
        ProxyServer.getInstance().getScheduler().schedule(this, () -> {

            getTextChannel().sendMessageEmbeds(builder.build()).queue();
        }, 1L, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(":octagonal_sign:  **Minef.ac network offline**").setColor(Color.decode("#FFFF00"));
        getTextChannel().sendMessageEmbeds(builder.build()).queue();
        bot.getBot().shutdownNow();
    }

    private Guild getGuild() {
        Guild guild = bot.getBot().getGuildById(Saves.guildID);
        if (guild != null) {
            return guild;
        }
        getLogger().severe("Guild does not exist in the config! " + Saves.guildID);
        return null;
    }

    private TextChannel getTextChannel() {
        TextChannel tc = bot.getBot().getTextChannelById(Saves.textChannelID);
        if (tc != null) {
            return tc;
        }
        getLogger().severe("Text channel does not exist in the config! " + Saves.textChannelID);
        return null;
    }

    public void reloadConfig() {
        if (bot != null) {
            bot.getBot().shutdownNow();
        }
        GetDefaultConfig();
        LoadConfig(config);
        startBot();
        UpdatePlayerCount();
        getLogger().info("Reloaded.");
    }

    private void startBot() {
        bot = new Bot("ODE0NjcyMjYxODM0NDczNTMy.YDhQqw.YyltuP1kCiKhZBu42kqY_UuKOvU", "!");
        bot.addEvent(event -> {
            if (event instanceof MessageReceivedEvent) {
                MessageReceivedEvent e = (MessageReceivedEvent) event;
                if (getTextChannel() != null && getGuild() != null) {
                    if (e.getChannel().getIdLong() == Saves.textChannelID) {
                        String nickname = e.getMember().getNickname();
                        String name = (Saves.useDiscordNicknames && nickname != null && !nickname.isEmpty()) ? nickname : e.getAuthor().getName();
                        Role role = null;
                        for (Role r : Objects.requireNonNull(e.getMember()).getRoles()) {
                            role = r;
                        }
                        if (!Objects.requireNonNull(role).getName().toLowerCase().contains("bot"))
                            SendMessageToMinecraft(name, role.getName(), e.getMessage().getContentDisplay());
                    }
                }
            } else if (event instanceof StatusChangeEvent) {
                StatusChangeEvent e = (StatusChangeEvent) event;
                if (e.getNewStatus().name().equals("CONNECTED")) {
                    getLogger().info("READY!");
                    if (!isSetUp) {
                        isEnabled = true;
                        isSetUp = true;
                    }
                }
            }
            return false;
        });
        bot.setBotThread(new ThreadBungee(this));
    }

    private void bungeeBroadcast() {
        bcMsg = 1;
        getProxy().getScheduler().schedule(this, () -> {
            for (ProxiedPlayer p : getProxy().getPlayers()) {
                if (bcMsg == 1) {
                    String msg = ChatColor.DARK_GRAY + "["
                            + ChatColor.RED + ChatColor.BOLD
                            + "Minef.ac" + ChatColor.DARK_GRAY + "] "
                            + ChatColor.GRAY + "You "
                            + ChatColor.GRAY + "can "
                            + ChatColor.GRAY + "use"
                            + ChatColor.GOLD + " /vote " + ChatColor.GRAY
                            + "to " + ChatColor.GRAY + "earn "
                            + ChatColor.GRAY + "rewards "
                            + ChatColor.GRAY + "and " + ChatColor.GRAY
                            + "help " + ChatColor.GRAY + "the "
                            + ChatColor.GRAY + "network "
                            + ChatColor.GRAY + "grow!";
                    p.sendMessage(new TextComponent(msg));
                }
                if (bcMsg == 2) {
                    String msg = ChatColor.DARK_GRAY + "[" +
                            ChatColor.RED + ChatColor.BOLD
                            + "Minef.ac" + ChatColor.DARK_GRAY
                            + "] " + ChatColor.GRAY + "Purchase "
                            + ChatColor.GRAY + "ranks "
                            + ChatColor.GRAY + "& "
                            + ChatColor.GRAY + "others "
                            + ChatColor.GRAY + "from"
                            + ChatColor.GOLD + " /store " + ChatColor.GRAY
                            + "to " + ChatColor.GRAY + "support "
                            + ChatColor.GRAY + "the "
                            + ChatColor.GRAY + "server!";
                    p.sendMessage(new TextComponent(msg));
                }
                if (bcMsg == 3) {
                    String msg = ChatColor.DARK_GRAY + "["
                            + ChatColor.RED + ChatColor.BOLD
                            + "Minef.ac" + ChatColor.DARK_GRAY + "] "
                            + ChatColor.GRAY + "Use"
                            + ChatColor.GOLD + " /help " + ChatColor.GRAY
                            + "if " + ChatColor.GRAY + "you" + ChatColor.GRAY
                            + " aren't" + ChatColor.GRAY + " familiar "
                            + ChatColor.GRAY + "with "
                            + ChatColor.GRAY + "commands";
                    p.sendMessage(new TextComponent(msg));
                }
                if (bcMsg == 4) {
                    String msg = ChatColor.DARK_GRAY + "["
                            + ChatColor.RED + ChatColor.BOLD
                            + "Minef.ac" + ChatColor.DARK_GRAY + "] "
                            + ChatColor.GRAY + "Our "
                            + ChatColor.GRAY + "anticheat "
                            + ChatColor.GRAY + "has "
                            + ChatColor.GRAY + "banned "
                            + ChatColor.GOLD + getBans()
                            + ChatColor.GRAY + " total "
                            + ChatColor.GRAY + "players!";
                    p.sendMessage(new TextComponent(msg));
                }
                if (bcMsg == 4) {
                    String msg = ChatColor.DARK_GRAY + "["
                            + ChatColor.RED + ChatColor.BOLD
                            + "Minef.ac" + ChatColor.DARK_GRAY
                            + "] " + ChatColor.GRAY + "Our "
                            + ChatColor.GRAY + "Discord "
                            + ChatColor.GRAY + "can "
                            + ChatColor.GRAY + "be "
                            + ChatColor.GRAY + "accessed "
                            + ChatColor.GRAY + "by "
                            + ChatColor.GRAY + "using "
                            + ChatColor.GOLD + " /discord";
                    p.sendMessage(new TextComponent(msg));
                }
                if (bcMsg == 5) {
                    String msg = ChatColor.DARK_GRAY + "["
                            + ChatColor.RED + ChatColor.BOLD
                            + "Minef.ac" + ChatColor.DARK_GRAY + "] "
                            + ChatColor.GRAY + "Your "
                            + ChatColor.GRAY + "rank "
                            + ChatColor.GRAY + "is " + ChatColor.GOLD
                            + getRank(p.getName()) + "\n" + ChatColor.GRAY + "Use"
                            + ChatColor.GOLD + " /ranks " + ChatColor.GRAY
                            + "and" + ChatColor.GOLD + " /rankup "
                            + ChatColor.GRAY + "for "
                            + ChatColor.GRAY + "ranking "
                            + ChatColor.GRAY + "up";
                    p.sendMessage(new TextComponent(msg));
                    bcMsg = 0;
                }
                bcMsg = bcMsg + 1;
            }
        }, 1, 120, TimeUnit.SECONDS);
    }

    public String getRank(String p) {
        return Objects.requireNonNull(LuckPermsProvider.get().getGroupManager().getGroup
                (Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(p)).getPrimaryGroup())).getDisplayName();
    }

    public long getBans() {
        String query = "SELECT COUNT(*) FROM {bans}";
        try (PreparedStatement st = Database.get().prepareStatement(query)) {
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public void SendMessageToDiscord(String message) {
        /* 400 */
        if (isEnabled && isSetUp && getTextChannel() != null) {
            /* 401 */
            getTextChannel().sendMessage(message).queue();
            /*     */
        }
        /*     */
    }

    public void SendEventMessageToDiscord(ProxiedPlayer player, String structure) {
        if (isEnabled && isSetUp && getTextChannel() != null) {
            String toSend = structure.replaceAll("<name>", player.getName());
            if (Saves.useBuilder) {
                // EmbedBuilder builder = new EmbedBuilder();
                // builder.setTitle(toSend).setColor(Saves.color);
                // builder.setImage(avatarURL.replace("<uuid>", player.getUniqueId().toString()));
                // getTextChannel().sendMessage(builder.build()).queue();
                getTextChannel().sendMessage(":octagonal_sign:  **Minef.ac network offline**");
            } else {
                getTextChannel().sendMessage(toSend).queue();
            }
        }
        if (!isEnabled) getLogger().severe("NOT ENABLED");
        if (!isSetUp) getLogger().severe("NOT SETUP");
        if (getTextChannel() == null) getLogger().severe("TEXT CHANNEL NOT SETUP");
    }

    /*public void SendEventMessageToDiscord(ProxiedPlayer player, String structure, long channelID) {
        if (isEnabled && isSetUp) {
            String toSend = structure.replaceAll("<name>", player.getName());
            if (Saves.useBuilder) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(toSend).setColor(Saves.color);
                builder.setImage(avatarURL.replace("<uuid>", player.getUniqueId().toString()));
                Objects.requireNonNull(bot.getBot().getTextChannelById(channelID)).sendMessage(builder.build()).queue();
            } else {
                Objects.requireNonNull(bot.getBot().getTextChannelById(channelID)).sendMessage(toSend).queue();
            }
        }
    }*/

    public void SendEventMessageToDiscord(ProxiedPlayer player, String structure, String color) {
        if (isEnabled && isSetUp && getTextChannel() != null) {
            String toSend = structure.replaceAll("<name>", player.getName());
            if (Saves.useBuilder) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(toSend).setColor(Color.decode(color));
                builder.setImage(avatarURL.replace("<uuid>", player.getUniqueId().toString()));
                getTextChannel().sendMessageEmbeds(builder.build()).queue();
            } else {
                getTextChannel().sendMessage(toSend).queue();
            }
        }
        if (!isEnabled) getLogger().severe("NOT ENABLED");
        if (!isSetUp) getLogger().severe("NOT SETUP");
        if (getTextChannel() == null) getLogger().severe("TEXT CHANNEL NOT SETUP");
    }

    public void UpdatePlayerCount() {
        if (isEnabled) {
            if (Saves.showPlayersOnline) {
                ProxyServer.getInstance().getScheduler().schedule(this, () -> {
                    int size = GetPlayerCount();
                    if (Saves.botPlayingText != null && !Saves.botPlayingText.equals("")) {
                        String botText = Saves.botPlayingText.replaceAll("<number>", "" + size);
                        bot.getBot().getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing(botText));
                    }
                }, 1L, TimeUnit.SECONDS);
            }
        }
    }

    public int GetPlayerCount() {
        return getProxy().getPlayers().size();
    }

    private void LoadConfig(Configuration conf) {
        Saves.guildID = 814671813032411156L;
        Saves.textChannelID = 814671928249286676L;
        Saves.showPlayersOnline = conf.getBoolean("show_players_online");
        Saves.inGameChatStyle = conf.getString("minecraft_chat");
        Saves.discordChatStyle = conf.getString("discord_chat");
        Saves.discordJoinedMessageEnabled = conf.getBoolean("discord_joined_message_enabled");
        Saves.discordLeftMessageEnabled = conf.getBoolean("discord_left_message_enabled");
        Saves.useDiscordNicknames = conf.getBoolean("use_discord_nicknames");
        Saves.joinDiscordStyle = conf.getString("discord_joined_message");
        Saves.leaveDiscordStyle = conf.getString("discord_left_message");
        Saves.canUseColorCodes = conf.getBoolean("colorcodes_enabled");
        Saves.useBuilder = conf.getBoolean("use_fancy_border");
        Saves.color = Color.decode("#" + config.getString("builder_color"));
        Saves.botPlayingText = conf.getString("bot_playing_text");
        Saves.useMinecraftNicknames = conf.getBoolean("use_minecraft_nicknames");
        Saves.onlyBungeecord = conf.getBoolean("use_only_bungeecord");
    }


    private void SendMessageToMinecraft(String name, String role, String message) {
        if (!Saves.canUseColorCodes) {
            message = message.replaceAll("§", "");
        }
        String toSend = Saves.inGameChatStyle.replaceAll("&", "§");
        toSend = toSend.replaceAll("<name>", name);
        toSend = toSend.replaceAll("<message>", message);
        toSend = toSend.replaceAll("<role>", role);
        toSend = FixColors(toSend);
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            p.sendMessage(new TextComponent(toSend));
        }
    }

    private String FixColors(String message) {
        String color = "0123456789abcdef";
        String exception = "klmnor";
        StringBuilder newMessage = new StringBuilder();
        String cPrifix = "";
        boolean isPrifix = false;
        char[] seq = message.toCharArray();
        for (char c : seq) {
            if (isPrifix) {
                char lc = Character.toLowerCase(c);
                if (color.indexOf(lc) > 0) {
                    cPrifix = "§" + lc;
                } else if (exception.indexOf(lc) > 0) {
                    cPrifix = cPrifix + "§" + lc;
                }
            }
            isPrifix = (c == '§');
            if (c == ' ') {
                newMessage.append(" ").append(cPrifix);
            } else {
                newMessage.append(c);
            }
        }
        return newMessage.toString();
    }

    void GetDefaultConfig() {
        File file = new File(getDataFolder(), "config.yml");
        try {
            if (!file.exists()) {
                if (!getDataFolder().exists()) {
                    getDataFolder().mkdir();
                }
                file.createNewFile();
                InputStream is = getResourceAsStream("config_bungee.yml");
                Throwable localThrowable6 = null;
                try {
                    OutputStream os = new FileOutputStream(file);

                    Throwable localThrowable7 = null;
                    try {
                        ByteStreams.copy(is, os);
                    } catch (Throwable localThrowable1) {
                        localThrowable7 = localThrowable1;
                        throw localThrowable1;
                    }

                } catch (Throwable localThrowable4) {
                    localThrowable6 = localThrowable4;
                    throw localThrowable4;
                } finally {

                    if (is != null) {
                        if (localThrowable6 != null) {
                            try {
                                is.close();
                            } catch (Throwable localThrowable5) {
                                localThrowable6.addSuppressed(localThrowable5);
                            }
                        } else {
                            is.close();
                        }
                    }
                }
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}