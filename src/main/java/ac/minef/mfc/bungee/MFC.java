package ac.minef.mfc.bungee;

import ac.minef.mfc.bungee.commands.Duels;
import ac.minef.mfc.bungee.commands.Hub;
import ac.minef.mfc.bungee.commands.MFCB;
import ac.minef.mfc.bungee.commands.Minefactions;
import ac.minef.mfc.bungee.listeners.*;
import com.google.common.io.ByteStreams;
import com.tjplaysnow.discord.object.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MFC extends Plugin {

    private static MFC instance;
    public final String PREFIX = "!";
    public Bot bot;
    public TextChannel textChannel;
    public Map<String, Long> serversChannelIDs = new HashMap<>();
    public Map<String, Integer> serversPastCount = new HashMap<>();
    public Map<String, Boolean> playerInfo = new HashMap<>();
    public boolean isEnabled = true;
    public boolean isSetUp = false;
    Guild guild;
    TextChannel commandChannel;
    boolean isUsingVanishOnSpigot = false;
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

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Duels());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Minefactions());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MFCB(this));

        GetDefaultConfig();
        LoadConfig(config);
        bot = new Bot("ODE0NjcyMjYxODM0NDczNTMy.YDhQqw.YyltuP1kCiKhZBu42kqY_UuKOvU", "!");
        bot.addEvent(event -> {
            if (event instanceof MessageReceivedEvent) {
                MessageReceivedEvent e = (MessageReceivedEvent) event;
                if (textChannel != null && guild != null) {
                    if (e.getChannel().getIdLong() == Saves.textChannelID) {
                        String nickname = Objects.requireNonNull(guild.getMember(e.getAuthor())).getNickname();
                        String name = (Saves.useDiscordNicknames && nickname != null && !nickname.isEmpty()) ? nickname : e.getAuthor().getName();
                        SendMessageToMinecraft(name, "", e.getMessage().getContentDisplay());
                    }
                }
            } else if (event instanceof StatusChangeEvent) {
                StatusChangeEvent e = (StatusChangeEvent) event;
                if (e.getNewStatus().name().equals("CONNECTED")) {
                    getLogger().info("READY!");
                    if (!isSetUp) {
                        UpdateStatus();
                    }
                }
            }
            return false;
        });
        bot.setBotThread(new ThreadBungee(this));
        UpdatePlayerCount();
        bungeeBroadcast();

        MFC.getInstance().SendMessageToDiscord(":white_check_mark: **Minef.ac network online**");
    }

    @Override
    public void onDisable() {
        MFC.getInstance().SendMessageToDiscord(":octagonal_sign: **Minef.ac network offline**");
    }

    private void bungeeBroadcast() {
        bcMsg = 1;
        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
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
                    /*if (bcMsg == 4) {
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
                    }*/
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
            }
        }, 1, 120, TimeUnit.SECONDS);
    }

    public String getRank(String p) {
        return Objects.requireNonNull(LuckPermsProvider.get().getGroupManager().getGroup
                (Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(p)).getPrimaryGroup())).getDisplayName();
    }

    /*public long getBans() {
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

     */

    public void SendMessageToDiscord(String message) {
        /* 400 */
        if (isEnabled && isSetUp && textChannel != null) {
            /* 401 */
            textChannel.sendMessage(message).queue();
            /*     */
        }
        /*     */
    }

    public void SendEventMessageToDiscord(String name, String structure) {
        /* 324 */
        if (isEnabled && isSetUp && textChannel != null) {
            /* 325 */
            String toSend = structure.replaceAll("<name>", name);
            /* 326 */
            if (Saves.useBuilder) {
                /* 327 */
                EmbedBuilder builder = new EmbedBuilder();
                /* 328 */
                builder.setTitle(toSend).setColor(Saves.color);
                /* 329 */
                textChannel.sendMessage(builder.build()).complete();
                /*     */
            } else {
                /* 331 */
                textChannel.sendMessage(toSend).queue();
                /*     */
            }
            /*     */
        }
        if (!isEnabled) getLogger().severe("NOT ENABLED");
        if (!isSetUp) getLogger().severe("NOT SETUP");
        if (textChannel == null) getLogger().severe("TEXT CHANNEL NOT SETUP");
        /*     */
    }

    /*     */
    /*     */
    public void SendEventMessageToDiscord(String name, String structure, long channelID) {
        if (isEnabled && isSetUp) {
            String toSend = structure.replaceAll("<name>", name);
            if (Saves.useBuilder) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(toSend).setColor(Saves.color);
                Objects.requireNonNull(bot.getBot().getTextChannelById(channelID)).sendMessage(builder.build()).complete();
            } else {
                Objects.requireNonNull(bot.getBot().getTextChannelById(channelID)).sendMessage(toSend).queue();
            }
        }
    }

    public void SendEventMessageToDiscord(String name, String structure, String color) {
        if (isEnabled && isSetUp && textChannel != null) {
            String toSend = structure.replaceAll("<name>", name);
            if (Saves.useBuilder) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(toSend).setColor(Color.decode(color));
                textChannel.sendMessage(builder.build()).complete();
            } else {
                textChannel.sendMessage(toSend).queue();
            }
        }
        if (!isEnabled) getLogger().severe("NOT ENABLED");
        if (!isSetUp) getLogger().severe("NOT SETUP");
        if (textChannel == null) getLogger().severe("TEXT CHANNEL NOT SETUP");
    }

    public void UpdatePlayerCount() {
        if (isEnabled) {
            if (Saves.showPlayersOnline) {
                ProxyServer.getInstance().getScheduler().schedule(this, () -> {
                    int size = GetPlayerCount();
                    if (Saves.botPlayingText != null && !Saves.botPlayingText.equals("")) {
                        String botText = Saves.botPlayingText.replaceAll("<number>", "" + size);
                    } else {
                        String botText = size + ((size == 1) ? " player" : " players") + " on the server.";
                    }
                }, 1L, TimeUnit.SECONDS);
            }
        }
    }

    public int GetPlayerCount() {
        /* 504 */
        int size = ProxyServer.getInstance().getPlayers().size();
        /* 505 */
        if (!Saves.onlyBungeecord) {
            /* 506 */
            size = playerInfo.size();
            /* 507 */
            for (Map.Entry<String, Boolean> entry : playerInfo.entrySet()) {
                /* 508 */
                if (entry.getValue()) {
                    /* 509 */
                    size--;
                    /*     */
                }
                /*     */
            }
            /*     */
        }
        /* 513 */
        return size;
        /*     */
    }

    private void UpdateStatus() {
        /* 160 */
        isEnabled = true;
        /* 161 */
        isSetUp = false;
        /* 162 */
        guild = bot.getBot().getGuildById(Saves.guildID);
        /* 163 */
        if (guild != null) {
            /* 170 */
            textChannel = guild.getTextChannelById(Saves.textChannelID);
            /* 171 */
            if (textChannel == null) {
                /* 172 */
                isEnabled = false;
                /* 173 */
                getLogger().info("The text channel ID is not set up properly!");
                /*     */
            }
            /*     */
        } else {
            /*     */
            /* 178 */
            isEnabled = false;
            /* 179 */
            getLogger().info("The guild ID is not set up properly!");
            /*     */
        }
        /* 181 */
        if (isEnabled) {
            /* 182 */
            getLogger().info("Everything set up correctly!");
            /*     */
        }
        /* 184 */
        isSetUp = true;
        /*     */
    }

    public void reloadConfig() {
        /* 152 */
        GetDefaultConfig();
        /* 153 */
        LoadConfig(config);
        /* 154 */
        UpdateStatus();
        /* 155 */
        UpdatePlayerCount();
        /* 156 */
        getLogger().info("Reloaded.");
        /*     */
    }

    private void LoadConfig(Configuration conf) {
        Saves.guildID = Long.valueOf("814671813032411156");
        /* 236 */
        Saves.textChannelID = Long.valueOf("814671928249286676");
        /* 237 */
        Saves.showPlayersOnline = conf.getBoolean("show_players_online");
        /* 238 */
        Saves.inGameChatStyle = conf.getString("minecraft_chat");
        /* 239 */
        Saves.discordChatStyle = conf.getString("discord_chat");
        /* 240 */
        Saves.discordJoinedMessageEnabled = conf.getBoolean("discord_joined_message_enabled");
        /* 241 */
        Saves.discordLeftMessageEnabled = conf.getBoolean("discord_left_message_enabled");
        /* 242 */
        Saves.useDiscordNicknames = conf.getBoolean("use_discord_nicknames");
        /* 243 */
        Saves.joinDiscordStyle = conf.getString("discord_joined_message");
        /* 244 */
        Saves.leaveDiscordStyle = conf.getString("discord_left_message");
        /* 245 */
        Saves.canUseColorCodes = conf.getBoolean("colorcodes_enabled");
        /* 246 */
        Saves.useBuilder = conf.getBoolean("use_fancy_border");
        /* 247 */
        Saves.color = Color.decode("#" + config.getString("builder_color"));
        /* 248 */
        Saves.botPlayingText = conf.getString("bot_playing_text");
        /* 249 */
        /* 250 */
        Saves.useMinecraftNicknames = conf.getBoolean("use_minecraft_nicknames");
        /* 251 */
        /* 252 */
        Saves.onlyBungeecord = conf.getBoolean("use_only_bungeecord");
    }


    private void SendMessageToMinecraft(String name, String role, String message) {
        /* 420 */
        if (!role.toLowerCase().contains("bot") && !message.isEmpty()
                && !role.isEmpty() && !name.toLowerCase().contains("bot")) {
            if (!Saves.canUseColorCodes) {
                /* 421 */
                message = message.replaceAll("§", "");
                /*     */
            }
            /* 423 */
            String toSend = Saves.inGameChatStyle.replaceAll("&", "§");
            /* 424 */
            toSend = toSend.replaceAll("<name>", name);
            /* 425 */
            toSend = toSend.replaceAll("<message>", message);
            /* 426 */
            toSend = toSend.replaceAll("<role>", role);
            /* 427 */
            toSend = FixColors(toSend);
            /* 428 */
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                /* 429 */
                p.sendMessage(new TextComponent(toSend));
                /*     */
            }
        }
        /*     */
    }

    private String FixColors(String message) {
        /* 434 */
        String color = "0123456789abcdef";
        /* 435 */
        String exception = "klmnor";
        /*     */
        /* 437 */
        String newMessage = "";
        /* 438 */
        String cPrifix = "";
        /* 439 */
        boolean isPrifix = false;
        /* 440 */
        char[] seq = message.toCharArray();
        /*     */
        /* 442 */
        for (int i = 0; i < seq.length; i++) {
            /* 443 */
            char c = seq[i];
            /* 444 */
            if (isPrifix) {
                /* 445 */
                char lc = Character.toLowerCase(seq[i]);
                /* 446 */
                if (color.indexOf(lc) > 0) {
                    /* 447 */
                    cPrifix = "§" + lc;
                    /* 448 */
                } else if (exception.indexOf(lc) > 0) {
                    /* 449 */
                    cPrifix = cPrifix + "§" + lc;
                    /*     */
                }
                /*     */
            }
            /* 452 */
            isPrifix = (c == '§');
            /* 453 */
            if (c == ' ') {
                /* 454 */
                newMessage = newMessage + " " + cPrifix;
                /*     */
            } else {
                /* 456 */
                newMessage = newMessage + c;
                /*     */
            }
            /*     */
        }
        /* 459 */
        return newMessage;
        /*     */
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