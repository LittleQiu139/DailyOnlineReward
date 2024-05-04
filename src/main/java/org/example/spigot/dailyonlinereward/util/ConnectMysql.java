package org.example.spigot.dailyonlinereward.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.example.spigot.dailyonlinereward.DailyOnlineReward;

public class ConnectMysql {
    private Connection connection;
    private ConsoleMsg console = new ConsoleMsg();

    private String hostname = ConfigUtil.data.getString("database.hostname");
    private int port = ConfigUtil.data.getInt("database.port");
    private String dbname = ConfigUtil.data.getString("database.dbname");
    private String username = ConfigUtil.data.getString("database.username");
    private String password = ConfigUtil.data.getString("database.password");
    private String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?useSSL=false&autoReconnect=true";

    public ConnectMysql() {
        reconnectDatabase();
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                reconnectDatabase();
            }
        } catch (SQLException e) {
            console.consoleMessage("§c[每日在线奖励]检查连接状态时发生错误: " + e.getMessage());
        }
        return connection;
    }

    public void reconnectDatabase() {
        // 先尝试关闭旧的连接
        if (connection != null) {
            try {
                connection.close();
                console.consoleMessage("§a[每日在线奖励]数据库连接已关闭.");
            } catch (SQLException e) {
                console.consoleMessage("§c[每日在线奖励]关闭数据库连接时出错:" + e.getMessage());
            }
        }

        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            console.consoleMessage("§a[每日在线奖励]数据库连接成功.");
            ensureTableExists();
        } catch (SQLException e) {
            console.consoleMessage("§c[每日在线奖励]无法连接到数据库:" + e.getMessage());
        }
    }

    public void ensureTableExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_times (" +
                "uuid VARCHAR(255) NOT NULL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "play_time SMALLINT NOT NULL, " +
                "10min TINYINT NOT NULL, " +
                "30min TINYINT NOT NULL, " +
                "60min TINYINT NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            console.consoleMessage("§a[每日在线奖励]确保表 'player_times' 存在.");
        } catch (SQLException e) {
            console.consoleMessage("§c[每日在线奖励]创建表 'player_times' 时发生错误:" + e.getMessage());
        }
    }

    public boolean connectCheck() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            console.consoleMessage("§c[每日在线奖励]检查数据库连接时出错: " + e.getMessage());
            return false;
        }
    }

    public void shutDownMySQL() {
        if (connection != null) {
            try {
                connection.close();
                console.consoleMessage("§a[每日在线奖励]数据库连接已安全关闭.");
            } catch (SQLException e) {
                console.consoleMessage("§c[每日在线奖励]关闭数据库连接时出错:" + e.getMessage());
            }
        }
    }
}
