package ac.minef.mfc.spigot;

import ac.minef.mfc.spigot.commands.MFCCommand;
import ac.minef.mfc.spigot.commands.Vote;
import ac.minef.mfc.spigot.listeners.*;
import com.tjplaysnow.discord.object.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MFC extends JavaPlugin {

    private static MFC instance;
    public boolean isEnabled = true;
    public TextChannel textChannel;
    FileConfiguration config;
    LuckPerms api;
    Bot bot;
    Guild guild;

    public static MFC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Armor(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Connections(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommand(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new SparkyPunish(), this);
        getCommand("mfc").setExecutor(new MFCCommand());
        getCommand("vote").setExecutor(new Vote());

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }

        UpdateStatus();

        Reload();

        this.isEnabled = false;
        /*  60 */
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        /*     */
        /*  62 */
        getLogger().info("MFC BungeeCord mode activated!");
        getLogger().info("MFC enabled!");

        SendEventMessageToDiscord(getServer().getServerName(), Saves.serverStartStyle, "#15ea0e");
    }

    @Override
    public void onDisable() {
        getLogger().info("MFC disabled!");
        SendEventMessageToDiscord(getServer().getServerName(), Saves.serverStopStyle, "#ee1212");
    }

    public String getRank(String p) {
        return Objects.requireNonNull(api.getGroupManager().getGroup
                (Objects.requireNonNull(api.getUserManager().getUser(p)).getPrimaryGroup())).getDisplayName();
    }

    public void executeCommand(String cmd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
            }
        }.runTaskLater(this, 5);   // Your plugin instance, the time to be delayed.
    }

    public void SendEventMessageToDiscord(String server, String structure, String color) {
        /* 325 */
        String toSend = structure.replaceAll("<server>", server);
        /* 326 */
        if (ac.minef.mfc.spigot.Saves.useBuilder) {
            /* 327 */
            EmbedBuilder builder = new EmbedBuilder();
            /* 328 */
            builder.setTitle(toSend).setColor(Color.decode(color));
            textChannel.sendMessage(builder.build()).complete();
        } else {
            textChannel.sendMessage(toSend).queue();
        }
    }

    public void Reload() {
        /* 110 */
        loadFile();
        /* 111 */
        loadConfig(config);
        /* 116 */
        getLogger().info("Reloaded.");
        /*     */
    }

    private void UpdateStatus() {
        /* 160 */
        isEnabled = true;
        /* 161 *
        /* 162 */
        guild = bot.getBot().getGuildById(ac.minef.mfc.spigot.Saves.guildID);
        /* 163 */
        if (guild != null) {
            /* 170 */
            textChannel = guild.getTextChannelById(ac.minef.mfc.spigot.Saves.textChannelID);
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
            getLogger().info("Everything set up correctly!");
        }
    }

    public void loadConfig(Configuration conf) {
        /*  84 */
        Saves.guildID = conf.getLong("guild_id");
        /*  85 */
        Saves.textChannelID = conf.getLong("discord_channel_id");
        /*  86 */
        Saves.showPlayersOnline = conf.getBoolean("show_players_online");
        /*  87 */
        Saves.inGameChatStyle = conf.getString("minecraft_chat");
        Saves.serverStartStyle = conf.getString("server_start");
        Saves.serverStopStyle = conf.getString("server_stop");
        /*  88 */
        Saves.discordChatStyle = conf.getString("discord_chat");
        /*  89 */
        Saves.discordJoinedMessageEnabled = conf.getBoolean("discord_joined_message_enabled");
        /*  90 */
        Saves.discordLeftMessageEnabled = conf.getBoolean("discord_left_message_enabled");
        /*  91 */
        Saves.useDiscordNicknames = conf.getBoolean("use_discord_nicknames");
        /*  92 */
        Saves.joinDiscordStyle = conf.getString("discord_joined_message");
        /*  93 */
        Saves.leaveDiscordStyle = conf.getString("discord_left_message");
        /*  94 */
        Saves.canUseColorCodes = conf.getBoolean("colorcodes_enabled");
        /*  95 */
        Saves.useBuilder = conf.getBoolean("use_fancy_border");
        /*  96 */
        Saves.color = Color.decode("#" + this.config.getString("builder_color"));
        /*  97 */
        Saves.botPlayingText = conf.getString("bot_playing_text");
        /*  99 */
        Saves.useMinecraftNicknames = conf.getBoolean("use_minecraft_nicknames");
        /* 100 */
        Saves.bannedPrefixes = conf.getStringList("list_of_banned_prefixes");
        /* 101 */
        Saves.bannedWords = conf.getStringList("list_of_banned_words");
        /* 103 */
        Saves.isBungeeCord = conf.getBoolean("bungeecord");
        /* 104 */
        Saves.bungeeDeathMessageEnabled = conf.getBoolean("bungee_death_message_enabled");
        /* 105 */
        Saves.bannedFWords = conf.getStringList("list_of_banned_format_words");
        /* 106 */
        Saves.bannedFPrefixes = conf.getStringList("list_of_banned_format_prefixes");
        /* 107 */
        Saves.debug = conf.getBoolean("debug_mode");
        /*     */
    }

    public void loadFile() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config_spigot.yml", false);
            File tempFile = new File(getDataFolder(), "config_spigot.yml");
            tempFile.renameTo(new File(getDataFolder(), "config.yml"));
        }
        this.config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Failed to load config!");
            e.printStackTrace();
        }
    }

    public void sendToBungeeCord(Player p, String channel, String sub) {
        /* 211 */
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        /* 212 */
        DataOutputStream out = new DataOutputStream(b);
        /*     */
        try {
            /* 214 */
            out.writeUTF(channel);
            /* 215 */
            out.writeUTF(sub);
            /* 216 */
        } catch (IOException e) {
            /* 217 */
            e.printStackTrace();
            /*     */
        }
        /* 219 */
        p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
        /*     */
    }

}