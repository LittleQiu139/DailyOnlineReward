package org.example.spigot.dailyonlinereward.util;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CommonlyUtil {
    private ConnectMysql sql;
    Integer playTime;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    public CommonlyUtil() {
        this.sql = new ConnectMysql();
    }
    public static String getTime() {
        String timegroup = (new SimpleDateFormat("yyyy:MM:dd:HH:mm")).format(new Date());
        int nowYear = Integer.parseInt(timegroup.split(":")[0]);
        int nowMonth = Integer.parseInt(timegroup.split(":")[1]);
        int nowDay = Integer.parseInt(timegroup.split(":")[2]);
        int nowHour = Integer.parseInt(timegroup.split(":")[3]);
        int nowMinute = Integer.parseInt(timegroup.split(":")[4]);
        return nowYear + ":" + nowMonth + ":" + nowDay + ":" + nowHour + ":" + nowMinute;
    }

    public Integer getPlayTime(UUID uuid) throws SQLException {
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


    public static String getLore(String min) {
        String lore = ConfigUtil.config.getString("rewards." + min);
        lore = lore.replace("&", "§");
        return lore != null ? lore : "";
    }
    public static UUID getUUID(Player player){
        return player.getUniqueId();
    }
}
