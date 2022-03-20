package ac.minef.mfc.spigot;

import ac.minef.mfc.spigot.commands.MFCCommand;
import ac.minef.mfc.spigot.commands.VoteCommand;
import ac.minef.mfc.spigot.listeners.*;

import litebans.api.Database;
import net.luckperms.api.LuckPerms;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

public class MFC extends JavaPlugin {

    private static MFC instance;
    FileConfiguration config;

    private static Economy econ = null;
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
        getServer().getPluginManager().registerEvents(new Inventory(this), (Plugin) this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);

        getCommand("mfc").setExecutor(new MFCCommand());
        getCommand("vote").setExecutor(new VoteCommand());

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }

        Reload();

        getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                for (Player p : getServer().getOnlinePlayers()) {
                    if (getRank(p.getDisplayName()).equalsIgnoreCase("A")
                    && getEconomy().getBalance(p) >= 50000) {
                        p.performCommand("rankup");
                        return;
                    }
                    if (getRank(p.getDisplayName()).equalsIgnoreCase("B")
                            && getEconomy().getBalance(p) >= 100000) {
                        p.performCommand("rankup");
                        return;
                    }
                    if (getRank(p.getDisplayName()).equalsIgnoreCase("C")
                            && getEconomy().getBalance(p) >= 200000) {
                        p.performCommand("rankup");
                    }
                }
            }
        },20, 20);

        getServer().getScheduler().runTaskTimer(getInstance(), new Runnable() {
            public void run() {
                executeCommand("bc &a&lKey-all Event&f will begin in &65 minutes");
                getServer().getScheduler().runTaskLater(getInstance(), new Runnable() {
                    public void run() {
                        if (getServer().getOnlinePlayers().size() != 0) {
                            getServer().dispatchCommand(getServer().getConsoleSender(), "cc giveall physical Vote 2");
                            getServer().dispatchCommand(getServer().getConsoleSender(), "cc giveall physical Rare 1");
                        }
                    }
                }, 20 * 60 * 5);
            }
        },20 * 60 * 130, 20 * 60 * 130);


        /*getServer().getScheduler().runTaskTimer(getInstance(), new Runnable() {
            public void run() {
                for (Player p : MFC.getInstance().getServer().getOnlinePlayers()) {
                    if (!p.getWorld().getName().equalsIgnoreCase("world")) return;
                    if (p.getLocation().getBlockX() < 160 &&
                            p.getLocation().getBlockZ() < 160) {
                        if (p.getLocation().getBlockX() > -160 && p.getLocation().getBlockZ() > -160) {
                            p.getActivePotionEffects().clear();
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                        }
                    }
                }
            }
        },40, 40);*/
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
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

    public boolean isProfanity(Player p, String m) {
        if (!p.hasPermission("group.moderator")) {
            if (m.contains("nigg") || m.contains("niqq")
                    || m.contains("n1g") || m.contains("n3gro")
                    || m.contains("negro") || m.contains("kys")
                    || m.contains("fag") || m.contains("phag")
                    || m.contains("queer")
                    || m.contains("blackie") || m.equalsIgnoreCase("nig")
                    || m.contains("blacky") || m.contains("marlowe")
                    || m.contains("dick") || m.contains("cock")
                    || m.contains("nigger") || m.contains("aggo")
                    || m.contains("tits") || m.contains("puss")
                    || m.contains("0ck") || m.contains("aqg")
                    || m.contains("sex")) {

                TextComponent msg = new TextComponent("\n        §8[§c!§8]§e "
                        + p.getName() + " §cfailed cmd§7: §e" + m + "\n");
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§8[§eClick to warn§8]").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        "/warn " + p.getName() + " Inappropriate Language -s"));

                for (Player pnew : getServer().getOnlinePlayers()) {
                    if (pnew.hasPermission("group.moderator"))
                        pnew.sendMessage(String.valueOf(msg));
                }
                p.sendMessage(ChatColor.RED + "Inappropriate language detected.");

                return true;
            }
        }
        return false;
    }

}