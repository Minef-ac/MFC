package ac.minef.mfc.spigot;

import ac.minef.mfc.spigot.commands.MFCCommand;
import ac.minef.mfc.spigot.listeners.*;
import litebans.api.Database;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        getServer().getPluginManager().registerEvents(new PlayerCommand(), this);
        getServer().getPluginManager().registerEvents(new SparkyPunish(), this);

        getCommand("mfc").setExecutor(new MFCCommand());

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }

        Reload();

        getServer().getScheduler().runTaskTimer(getInstance(), new Runnable() {
            @Override
            public void run() {
                if (getServer().getOnlinePlayers().size() != 0) {
                    getServer().dispatchCommand(getServer().getConsoleSender(), "cc giveall physical Vote 4");
                    getServer().dispatchCommand(getServer().getConsoleSender(), "cc giveall physical Rare 2");
                    getServer().dispatchCommand(getServer().getConsoleSender(), "cc giveall physical Epic 1");
                }
            }
        },20, 20 * 60 * 100);
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
        getLogger().info("Reloaded.");
    }

    public void loadConfig(Configuration conf) {
        Saves.pluginVersion = getDescription().getVersion();
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
}