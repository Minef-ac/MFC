package ac.minef.mfc.bungee;

import ac.minef.mfc.bungee.commands.MFCB;
import ac.minef.mfc.bungee.listeners.*;
import com.google.common.io.ByteStreams;
import com.tjplaysnow.discord.object.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
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
    public String TOKEN = "none";
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

    public static MFC getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        /*  74 */
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Chat());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Connect());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PluginMessage());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Quit());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerSwitch());
        /*     */
        /*  76 */
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MFCB(this));
        /*     */
        /*  81 */
        GetDefaultConfig();
        /*     */
        /*  83 */
        LoadConfig(this.config);
        /*     */
        /*  89 */
        if (!this.TOKEN.equals("none")) {
            /*  90 */
            this.bot = new Bot(this.TOKEN, "!");
            /*  92 */
            this.bot.addEvent(event -> {
                /*     */
                if (event instanceof MessageReceivedEvent) {
                    /*     */
                    MessageReceivedEvent e = (MessageReceivedEvent) event;
                    /*     */
                    if (this.isEnabled && this.isSetUp && !e.getAuthor().isBot()) {
                        /*     */
                    } else if (this.textChannel != null && this.guild != null) {
                        /*     */
                        if (e.getChannel().getIdLong() == Saves.textChannelID) {
                            /*     */
                            String nickname = Objects.requireNonNull(this.guild.getMember(e.getAuthor())).getNickname();
                            /* 116 */
                            String name = (Saves.useDiscordNicknames && nickname != null && !nickname.isEmpty()) ? nickname : e.getAuthor().getName();
                            /*     */
                            /*     */
                            /*     */
                            SendMessageToMinecraft(name, "", e.getMessage().getContentDisplay());
                            /*     */
                        } else {
                            /*     */
                            getLogger().info("Error: text channel or guild (discord server id) is not defined!");
                            /*     */
                        }
                        /*     */
                    }
                    /*     */
                } else if (event instanceof StatusChangeEvent) {
                    /*     */
                    StatusChangeEvent e = (StatusChangeEvent) event;
                    /*     */
                    if (e.getNewStatus().name().equals("CONNECTED")) {
                        /*     */
                        getLogger().info("READY!");
                        /*     */
                        if (!this.isSetUp) {
                            /*     */
                            UpdateStatus();
                            /*     */
                        }
                        /*     */
                    }
                    /*     */
                }
                /*     */
                return false;
                /*     */
            });
            /* 142 */
            this.bot.setBotThread(new ThreadBungee(this));
            /*     */
            /* 144 */
            UpdatePlayerCount();
            /*     */
        } else {
            /* 146 */
            this.isEnabled = false;
            /* 147 */
            getLogger().info("Please specify the bot's token.");
            /*     */
        }
    }

    public void SendMessageToDiscordChannel(String message, Long channelID) {
        /* 394 */
        getLogger().severe(message + " :: " + channelID);
        if (this.isEnabled && this.isSetUp) {
            getLogger().severe(message + " :: " + channelID);
            /* 395 */
            this.guild.getTextChannelById(channelID).sendMessage(message).queue();
            /*     */
        }
        /*     */
    }

    /*     */
    /*     */
    public void SendMessageToDiscord(String message) {
        /* 400 */
        getLogger().severe(message + " ::");
        if (this.isEnabled && this.isSetUp && this.textChannel != null) {
            /* 401 */
            getLogger().severe(message + " ::");
            this.textChannel.sendMessage(message).queue();
            /*     */
        }
        /*     */
    }

    public void SendEventMessageToDiscord(String name, String structure) {
        /* 324 */
        getLogger().severe(name + " :: " + structure);
        if (this.isEnabled && this.isSetUp && this.textChannel != null) {
            /* 325 */
            String toSend = structure.replaceAll("<name>", name);
            /* 326 */
            if (Saves.useBuilder) {
                /* 327 */
                EmbedBuilder builder = new EmbedBuilder();
                /* 328 */
                builder.setTitle(toSend).setColor(Saves.color);
                /* 329 */
                this.textChannel.sendMessage(builder.build()).complete();
                /*     */
            } else {
                /* 331 */
                this.textChannel.sendMessage(toSend).queue();
                /*     */
            }
            /*     */
        }
        /*     */
    }

    /*     */
    /*     */
    public void SendEventMessageToDiscord(String name, String structure, long channelID) {
        /* 337 */
        if (this.isEnabled && this.isSetUp) {
            /* 338 */
            String toSend = structure.replaceAll("<name>", name);
            /* 339 */
            if (Saves.useBuilder) {
                /* 340 */
                EmbedBuilder builder = new EmbedBuilder();
                /* 341 */
                builder.setTitle(toSend).setColor(Saves.color);
                /* 342 */
                Objects.requireNonNull(this.bot.getBot().getTextChannelById(channelID)).sendMessage(builder.build()).complete();
                /*     */
            } else {
                /* 344 */
                this.bot.getBot().getTextChannelById(channelID).sendMessage(toSend).queue();
                /*     */
            }
            /*     */
        }
        /*     */
    }
    /*     */

    public void UpdatePlayerCountOnServer(ServerInfo server) {
        /* 463 */
        int size = server.getPlayers().size();
        /* 464 */
        /*     */
    }

    private void SendMessageToMinecraftServer(String name, String role, String message, String serverName) {
        /* 406 */
        if (!Saves.canUseColorCodes) {
            /* 407 */
            message = message.replaceAll("§", "");
            /*     */
        }
        /* 409 */
        String toSend = Saves.inGameChatStyle.replaceAll("&", "§");
        /* 410 */
        toSend = toSend.replaceAll("<name>", name);
        /* 411 */
        toSend = toSend.replaceAll("<message>", message);
        /* 412 */
        toSend = toSend.replaceAll("<role>", role);
        /* 413 */
        toSend = FixColors(toSend);
        /* 414 */
        for (ProxiedPlayer p : ProxyServer.getInstance().getServerInfo(serverName).getPlayers()) {
            /* 415 */
            p.sendMessage(new TextComponent(toSend));
            /*     */
        }
        /*     */
    }

    public void UpdatePlayerCount() {
        /* 481 */
        if (this.isEnabled) {
            /* 485 */
            if (Saves.showPlayersOnline) {
                /* 486 */
                ProxyServer.getInstance().getScheduler().schedule(this, new Runnable()
                        /*     */ {
                    /*     */
                    public void run() {
                        /* 489 */
                        int size = MFC.this.GetPlayerCount();
                        /* 490 */
                        if (Saves.botPlayingText != null && !Saves.botPlayingText.equals("")) {
                            /* 491 */
                            String botText = Saves.botPlayingText.replaceAll("<number>", "" + size);
                            /*     */
                        } else {
                            /* 494 */
                            String botText = size + ((size == 1) ? " player" : " players") + " on the server.";
                            /*     */
                        }
                        /*     */
                    }
                    /* 498 */
                }, 1L, TimeUnit.SECONDS);
                /*     */
            }
            /*     */
        }
        /*     */
    }

    public int GetPlayerCount() {
        /* 504 */
        int size = ProxyServer.getInstance().getPlayers().size();
        /* 505 */
        if (!Saves.onlyBungeecord) {
            /* 506 */
            size = this.playerInfo.size();
            /* 507 */
            for (Map.Entry<String, Boolean> entry : this.playerInfo.entrySet()) {
                /* 508 */
                if (entry.getValue().booleanValue()) {
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
        this.isEnabled = true;
        /* 161 */
        this.isSetUp = false;
        /* 162 */
        this.guild = this.bot.getBot().getGuildById(Saves.guildID.longValue());
        /* 163 */
        if (this.guild != null) {
            /* 164 */
            if (this.guild != null) {
                /* 165 */
            } else {
                /* 170 */
                this.textChannel = this.guild.getTextChannelById(Saves.textChannelID.longValue());
                /* 171 */
                if (this.textChannel == null) {
                    /* 172 */
                    this.isEnabled = false;
                    /* 173 */
                    getLogger().info("The text channel ID is not set up properly!");
                    /*     */
                }
                /*     */
            }
            /*     */
        } else {
            /*     */
            /* 178 */
            this.isEnabled = false;
            /* 179 */
            getLogger().info("The guild ID is not set up properly!");
            /*     */
        }
        /* 181 */
        if (this.isEnabled) {
            /* 182 */
            getLogger().info("Everything set up correctly!");
            /*     */
        }
        /* 184 */
        this.isSetUp = true;
        /*     */
    }

    public void reloadConfig() {
        /* 152 */
        GetDefaultConfig();
        /* 153 */
        LoadConfig(this.config);
        /* 154 */
        UpdateStatus();
        /* 155 */
        UpdatePlayerCount();
        /* 156 */
        getLogger().info("Reloaded.");
        /*     */
    }

    private void LoadConfig(Configuration conf) {
        /* 234 */
        this.TOKEN = conf.getString("TOKEN");
        /* 235 */
        Saves.guildID = Long.valueOf(conf.getLong("guild_id"));
        /* 236 */
        Saves.textChannelID = Long.valueOf(conf.getLong("discord_channel_id"));
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
        Saves.color = Color.decode("#" + this.config.getString("builder_color"));
        /* 248 */
        Saves.botPlayingText = conf.getString("bot_playing_text");
        /* 249 */
        /* 250 */
        Saves.useMinecraftNicknames = conf.getBoolean("use_minecraft_nicknames");
        /* 251 */
        /* 252 */
        Saves.onlyBungeecord = conf.getBoolean("use_only_bungeecord");
        /* 255 */
        for (Map.Entry<String, ServerInfo> server : ProxyServer.getInstance().getServers().entrySet()) {
            /* 256 */
            Long id = Long.valueOf(conf.getLong("server_names." + server.getKey()));
            /* 257 */
            if (id != null && id.longValue() != 0L) {
                /* 258 */
                this.serversChannelIDs.put(server.getKey(), id);
                /* 259 */
                this.serversPastCount.put(server.getKey(), Integer.valueOf(0));
                /*     */
            }
            /*     */
        }
        /*     */
        /* 264 */
        /* 265 */
        /*     */
    }


    private void SendMessageToMinecraft(String name, String role, String message) {
        /* 420 */
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