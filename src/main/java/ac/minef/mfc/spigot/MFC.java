package ac.minef.mfc.spigot;

import ac.minef.mfc.spigot.commands.MFCCommand;
import ac.minef.mfc.spigot.listeners.*;
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
    FileConfiguration config;
    LuckPerms api;

    public static MFC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new Armor(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new Connections(), this);
        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommand(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        getServer().getPluginManager().registerEvents(new SparkyPunish(), this);
        getServer().getPluginManager().registerEvents(new CreatureSpawn(), this);

        getCommand("mfc").setExecutor(new MFCCommand());

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }

        Reload();

        this.isEnabled = false;
        /*  60 */
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        /*     */
        /*  62 */
        getLogger().info("MFC BungeeCord mode activated!");
        getLogger().info("MFC enabled!");

        // SendEventMessageToDiscord(getServer().getServerName(), Saves.serverStartStyle, "#15ea0e");
    }

    @Override
    public void onDisable() {
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            config.save(configFile);
        } catch (IOException e) {
            getLogger().severe("Config failed to save!");
            e.printStackTrace();
        }
        getLogger().info("MFC disabled!");
        // SendEventMessageToDiscord(getServer().getServerName(), Saves.serverStopStyle, "#ee1212");
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
        }.runTaskLater(this, 1);   // Your plugin instance, the time to be delayed.
    }

    public void Reload() {
        loadFile();
        loadConfig(config);
        // UpdateStatus();
        getLogger().info("Reloaded.");
    }

    public void loadConfig(Configuration conf) {
        Saves.guildID = conf.getLong("guild_id");
        Saves.backpacks = conf.getLong("backpacks");
        Saves.textChannelID = conf.getLong("discord_channel_id");
        Saves.showPlayersOnline = conf.getBoolean("show_players_online");
        Saves.inGameChatStyle = conf.getString("minecraft_chat");
        Saves.serverStartStyle = conf.getString("server_start");
        Saves.serverStopStyle = conf.getString("server_stop");
        Saves.discordChatStyle = conf.getString("discord_chat");
        Saves.discordJoinedMessageEnabled = conf.getBoolean("discord_joined_message_enabled");
        Saves.discordLeftMessageEnabled = conf.getBoolean("discord_left_message_enabled");
        Saves.useDiscordNicknames = conf.getBoolean("use_discord_nicknames");
        Saves.joinDiscordStyle = conf.getString("discord_joined_message");
        Saves.leaveDiscordStyle = conf.getString("discord_left_message");
        Saves.canUseColorCodes = conf.getBoolean("colorcodes_enabled");
        Saves.useBuilder = conf.getBoolean("use_fancy_border");
        Saves.color = Color.decode("#" + this.config.getString("builder_color"));
        Saves.botPlayingText = conf.getString("bot_playing_text");
        Saves.useMinecraftNicknames = conf.getBoolean("use_minecraft_nicknames");
        Saves.bannedPrefixes = conf.getStringList("list_of_banned_prefixes");
        Saves.bannedWords = conf.getStringList("list_of_banned_words");
        Saves.isBungeeCord = conf.getBoolean("bungeecord");
        Saves.bungeeDeathMessageEnabled = conf.getBoolean("bungee_death_message_enabled");
        Saves.bannedFWords = conf.getStringList("list_of_banned_format_words");
        Saves.bannedFPrefixes = conf.getStringList("list_of_banned_format_prefixes");
        Saves.debug = conf.getBoolean("debug_mode");
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