package ac.minef.mfc.bungee;

import ac.minef.mfc.bungee.commands.*;
import ac.minef.mfc.bungee.listeners.*;
import com.google.common.io.ByteStreams;
import litebans.api.Database;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
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
    public Map<String, Boolean> playerInfo = new HashMap<>();
    Configuration config;
    int bcMsg;

    public static MFC getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        // ProxyServer.getInstance().getPluginManager().registerListener(this, new TabComplete());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MFCB(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Minefactions());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Ping());

        reloadConfig();
    }

    @Override
    public void onDisable() {}

    public void reloadConfig() {
        GetDefaultConfig();
        LoadConfig(config);
        getLogger().info("Reloaded.");
    }

    private void LoadConfig(Configuration conf) {
        Saves.pluginVersion = MFC.getInstance().getDescription().getVersion();
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