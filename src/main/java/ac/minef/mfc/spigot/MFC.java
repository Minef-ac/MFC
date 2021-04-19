package ac.minef.mfc.spigot;

import ac.minef.mfc.spigot.commands.MFCCommand;
import ac.minef.mfc.spigot.listeners.AsyncPlayerChat;
import ac.minef.mfc.spigot.listeners.SparkyPunish;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class MFC extends JavaPlugin {

    private static MFC instance;
    public boolean isEnabled = true;
    public boolean isSetUp = false;
    FileConfiguration config;

    public static MFC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new SparkyPunish(), this);
        /*  44 */
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        getCommand("mfc").setExecutor(new MFCCommand());

        loadFile();
        loadConfig(this.config);

        this.isEnabled = false;
        /*  60 */
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        /*     */
        /*  62 */
        getLogger().info("MFC BungeeCord mode activated!");
        getLogger().info("MFC enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MFC disabled!");
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

    public void loadConfig(Configuration conf) {
        /*  84 */
        Saves.guildID = conf.getLong("guild_id");
        /*  85 */
        Saves.textChannelID = conf.getLong("discord_channel_id");
        /*  86 */
        Saves.showPlayersOnline = conf.getBoolean("show_players_online");
        /*  87 */
        Saves.inGameChatStyle = conf.getString("minecraft_chat");
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