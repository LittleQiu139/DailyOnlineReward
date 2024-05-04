package org.example.spigot.dailyonlinereward.event;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.spigot.dailyonlinereward.util.CommonlyUtil;
import org.example.spigot.dailyonlinereward.util.ConfigUtil;
import org.example.spigot.dailyonlinereward.util.ConnectMysql;
import org.example.spigot.dailyonlinereward.util.ConsoleMsg;

import java.sql.*;


public class DailyResetTask extends BukkitRunnable {
    private final Statement statement;
    private final ConsoleMsg console;

    public DailyResetTask() {
        // 使用 ConnectMysql 类获取数据库连接
        ConnectMysql connectMysql = new ConnectMysql();
        Connection connection = connectMysql.getConnection();

        try {
            // 从连接中创建 Statement 对象
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create statement", e);
        }

        this.console = new ConsoleMsg();
    }

    public void run() {
        String time = CommonlyUtil.getTime();
        int hour = Integer.parseInt(time.split(":")[3]);
        int minute = Integer.parseInt(time.split(":")[4]);
        if (hour == ConfigUtil.config.getInt("DailyHour") && minute == ConfigUtil.config.getInt("DailyMinute")) {
            String sql = "UPDATE player_times SET play_time = 0";
            String m10 = "UPDATE player_times SET 10min = 1";
            String m30 = "UPDATE player_times SET 30min = 1";
            String m60 = "UPDATE player_times SET 60min = 1";
            try {
                statement.executeUpdate(sql);
                statement.executeUpdate(m10);
                statement.executeUpdate(m30);
                statement.executeUpdate(m60);
                console.consoleMessage("§a[每日在线奖励]已刷新玩家在线时间和奖励状态.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
