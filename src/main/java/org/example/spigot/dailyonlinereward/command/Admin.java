package org.example.spigot.dailyonlinereward.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.spigot.dailyonlinereward.DailyOnlineReward;
import org.example.spigot.dailyonlinereward.util.ConfigUtil;
import org.example.spigot.dailyonlinereward.util.ConnectMysql;

import java.io.File;

public class Admin implements CommandExecutor {
    private final JavaPlugin plugin;
    public Admin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConnectMysql sql = new ConnectMysql();
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("dailyonlinereward.reload")) {
                    File file = new File(DailyOnlineReward.plugin.getDataFolder(), "config.yml");
                    ConfigUtil.config = YamlConfiguration.loadConfiguration(file);
                    file = new File(DailyOnlineReward.plugin.getDataFolder(), "data.yml");
                    ConfigUtil.data = YamlConfiguration.loadConfiguration(file);
                    file = new File(DailyOnlineReward.plugin.getDataFolder(), "message.yml");
                    ConfigUtil.message = YamlConfiguration.loadConfiguration(file);
                    sql.reconnectDatabase();  // 尝试重新连接数据库
                    sender.sendMessage("§a[每日在线奖励]配置已重新加载.");
                    return true;
                } else {
                    sender.sendMessage("§c[每日在线奖励]你没有权限使用这个命令.");
                    return true;
                }
            }
        }
        return false;
    }
}
