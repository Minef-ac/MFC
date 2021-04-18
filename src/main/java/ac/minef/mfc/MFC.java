package ac.minef.mfc;

import ac.minef.mfc.commands.MFCCommand;
import ac.minef.mfc.listeners.SparkyPunish;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MFC extends JavaPlugin {

    // getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes("&", msg)config.getString("logger.color") + msg);

    private static MFC instance;
    FileConfiguration config;

    public static MFC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new SparkyPunish(), this);
        getCommand("mfc").setExecutor(new MFCCommand());

        loadFile();

        getLogger().info("MFC enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MFC disabled!");
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

}