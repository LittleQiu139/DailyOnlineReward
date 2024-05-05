package org.example.spigot.dailyonlinereward.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.example.spigot.dailyonlinereward.util.ConnectMysql;
import org.example.spigot.dailyonlinereward.util.ConsoleMsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayTime implements Listener {
    private ConnectMysql sql;
    Integer playTime1;
    Integer playTime2;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    private HashMap<UUID, Long> playTimes = new HashMap<>();
    private  HashMap<UUID, String> getId = new HashMap<>();
    private ConsoleMsg console;

    public PlayTime() {
        this.sql = new ConnectMysql();  // Assume ConnectMysql handles connection pooling
        this.console = new ConsoleMsg();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getPlayerListName();
        UUID uuid = event.getPlayer().getUniqueId();
        Long joinTime = System.currentTimeMillis();
        playTimes.put(uuid, joinTime);
        getId.put(uuid, playerName);
        try {
            playTime1 = getPlayTime(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ensurePlayerRecord(uuid, 0);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Long joinTime = playTimes.get(uuid);

        try {
            playTime2 = getPlayTime(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (joinTime != null && playTime1 == playTime2) {
            long onlineDuration = (System.currentTimeMillis() - joinTime) / 1000;
            try (Connection conn = sql.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE player_times SET play_time = play_time + ? WHERE uuid = ?")) {
                ps.setLong(1, onlineDuration);
                ps.setString(2, uuid.toString());
                int updatedRows = ps.executeUpdate();
                if (updatedRows == 0) {
                    ensurePlayerRecord(uuid, onlineDuration); // 调用 ensurePlayerRecord 方法确保玩家记录存在
                }
            } catch (SQLException e) {
                console.consoleMessage("Failed to update play time for player: " + uuid + " - " + e.getMessage());
            }
        } else {
            console.consoleMessage("Failed to retrieve join time for player: " + uuid);
        }
    }


    private void ensurePlayerRecord(UUID uuid, long onlineDuration) {
        try (Connection conn = sql.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT uuid FROM player_times WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                insertNewRecord(uuid, onlineDuration); // 插入新记录
            }
        } catch (SQLException e) {
            console.consoleMessage("Error checking player record: " + e.getMessage());
        }
    }
    private void insertNewRecord(UUID uuid, long onlineDuration) {
        try (Connection conn = sql.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO player_times (uuid, name, play_time, 10min, 30min, 60min) VALUES (?, ?, ?, 1, 1, 1)")) {
            ps.setString(1, uuid.toString());
            ps.setString(2, getId.get(uuid));
            ps.setLong(3, onlineDuration);
            ps.executeUpdate();
        } catch (SQLException e) {
            console.consoleMessage("Error inserting new player record: " + e.getMessage());
        }
    }
    public Integer getPlayTime(UUID uuid) throws SQLException {
        Integer playTime = 0;
        this.sql = new ConnectMysql();
        Connection conn = sql.getConnection();
        try {
            String query = "SELECT play_time FROM player_times WHERE uuid = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, uuid.toString()); // 使用实际的玩家 UUID
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                playTime = resultSet.getInt("play_time"); // 使用实际的列名
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return playTime;
    }

}
