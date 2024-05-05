package org.example.spigot.dailyonlinereward;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.spigot.dailyonlinereward.command.Admin;
import org.example.spigot.dailyonlinereward.command.PlayerCMD;
import org.example.spigot.dailyonlinereward.event.DailyResetTask;
import org.example.spigot.dailyonlinereward.event.PlayTime;
import org.example.spigot.dailyonlinereward.gui.OpenMenu;
import org.example.spigot.dailyonlinereward.util.ConsoleMsg;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public final class DailyOnlineReward extends JavaPlugin implements Listener {
    public static Plugin plugin;
    public static boolean isListenerRegistered = false;

    ConsoleMsg console = new ConsoleMsg();
    public DailyOnlineReward(){
    }

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new PlayTime(), this);

        if (!(new File(this.getDataFolder(), "config.yml")).exists()) {
            this.saveResource("config.yml", false);
        }

        if (!(new File(this.getDataFolder(), "data.yml")).exists()) {
            this.saveResource("data.yml", false);
        }

        if (!(new File(this.getDataFolder(), "message.yml")).exists()) {
            this.saveResource("message.yml", false);
        }

        this.getCommand("dora").setExecutor(new Admin(this));  // 将 admin 实例设置为命令执行器
        this.getCommand("dor").setExecutor(new PlayerCMD(this));

        getServer().getPluginManager().registerEvents(this, this);
        (new DailyResetTask()).runTaskTimerAsynchronously(plugin, 1200L, 1200L);
        checkPlugins();
        console.consoleMessage("§a[每日在线奖励]已启用！");
    }
    @Override
    public void onDisable() {
        console.consoleMessage("§c[每日在线奖励]已停用!");
    }

    /*
    private void connectToDatabase() {
        String hostname = getConfig().getString("database.hostname");
        int port = getConfig().getInt("database.port");
        String dbname = getConfig().getString("database.dbname");
        String username = getConfig().getString("database.username");
        String password = getConfig().getString("database.password");
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?useSSL=false";

        try {
            // Ensure the driver class is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to the MySQL database
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            getLogger().info("Connected to the database successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().severe("Could not connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }*/


    private void checkPlugins() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            console.consoleMessage("§a[每日在线奖励]已检测到Vault插件，已成功加载!");
        } else {
            console.consoleMessage("§a[每日在线奖励]未检测到Vault插件，某些功能可能无法正常使用!");
        }
    }
}

