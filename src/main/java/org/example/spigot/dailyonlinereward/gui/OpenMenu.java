package org.example.spigot.dailyonlinereward.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.spigot.dailyonlinereward.DailyOnlineReward;
import org.example.spigot.dailyonlinereward.util.CommonlyUtil;
import org.example.spigot.dailyonlinereward.util.ConfigUtil;
import org.example.spigot.dailyonlinereward.util.ConnectMysql;
import org.example.spigot.dailyonlinereward.util.ConsoleMsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpenMenu implements InventoryHolder, Listener {
    Inventory inventory;
    ConsoleMsg console;
    Player player;
    Integer playTime;
    List<String> lore = new ArrayList<>();
    private ConnectMysql sql;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int minute;
    int second;
    private boolean isListenerRegistered = false;
    public OpenMenu(Player player) {
        this.player = player;
        this.sql = new ConnectMysql();
        this.inventory = Bukkit.createInventory(this, 1 * 9, ChatColor.translateAlternateColorCodes('&', ConfigUtil.message.getString("Title")));

        // 获取玩家的在线时间
        try {
            this.playTime = getPlayTime(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (playTime != null) {
            minute = playTime / 60;
            second = playTime - 60 * minute;
            ItemStack playTimeItem = new ItemStack(Material.CLOCK);
            ItemMeta playTimeMeta = playTimeItem.getItemMeta();
            playTimeMeta.setDisplayName("§a在线时间: " + minute + " 分" + second + " 秒");

            // 清空 lore 列表，并添加新的 Lore
            lore.clear();
            lore.add("§7重新进入服务器以刷新在线时间");
            playTimeMeta.setLore(lore);

            playTimeItem.setItemMeta(playTimeMeta);

            this.inventory.setItem(0, playTimeItem);
        } else {
            // 如果获取在线时间失败，向玩家显示错误信息
            ItemStack errorItem = new ItemStack(Material.BARRIER);
            ItemMeta errorMeta = errorItem.getItemMeta();
            errorMeta.setDisplayName("§c获取在线时间失败,请重新进入服务器");
            errorItem.setItemMeta(errorMeta);

            this.inventory.setItem(0, errorItem);
        }

        ItemStack m10 = new ItemStack(Material.IRON_BLOCK);
        ItemStack m30 = new ItemStack(Material.GOLD_BLOCK);
        ItemStack m60 = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta m10Meta = m10.getItemMeta();
        ItemMeta m30Meta = m30.getItemMeta();
        ItemMeta m60Meta = m60.getItemMeta();
        m10Meta.setDisplayName("§a在线10分钟奖励");
        m30Meta.setDisplayName("§a在线30分钟奖励");
        m60Meta.setDisplayName("§a在线60分钟奖励");

        lore.clear();
        lore.add(CommonlyUtil.getLore("10m"));
        m10Meta.setLore(lore);
        m10.setItemMeta(m10Meta);
        this.inventory.setItem(2, m10);

        lore.clear();
        lore.add(CommonlyUtil.getLore("30m"));
        m30Meta.setLore(lore);
        m30.setItemMeta(m30Meta);
        this.inventory.setItem(4, m30);

        lore.clear();
        lore.add(CommonlyUtil.getLore("60m"));
        m60Meta.setLore(lore);
        m60.setItemMeta(m60Meta);
        this.inventory.setItem(6, m60);

        // 注册事件监听器
        if (!isListenerRegistered) {
            // 注册点击事件监听器
            Bukkit.getPluginManager().registerEvents(this, DailyOnlineReward.getPlugin(DailyOnlineReward.class));
            isListenerRegistered = true;
        }
    }

    // 物品栏被点击时的事件处理方法
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 获取玩家的在线时间
        try {
            this.playTime = getPlayTime(player.getUniqueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        minute = playTime / 60;
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof OpenMenu) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR && clickedItem.getType() != Material.CLOCK) {
                if (clickedItem.getType() == Material.IRON_BLOCK && minute >= 1) {
                    if(getCheck(player.getUniqueId(), "10min") == 1){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " diamond 1");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " 100");
                        player.sendMessage(ChatColor.GREEN + "[每日在线奖励]" + "你收到了一颗钻石和100金币!");
                        modifyCheck(player.getUniqueId(), "10min");
                    }else{
                        player.sendMessage("§a[每日在线奖励]§c请勿重复领取!");
                    }
                } else if (clickedItem.getType() == Material.GOLD_BLOCK && minute >= 30) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " diamond 2");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " 200");
                    player.sendMessage(ChatColor.GREEN + "[每日在线奖励]" + "你收到了两颗钻石和200金币!");
                } else if (clickedItem.getType() == Material.DIAMOND_BLOCK && minute >= 60) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " diamond 3");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " 300");
                    player.sendMessage(ChatColor.GREEN + "[每日在线奖励]" + "你收到了三颗钻石和300金币!");
                }else {
                    player.sendMessage("§a[每日在线奖励]§c在线时间未达要求!");
                }
            }
            // 阻止玩家移动物品
            event.setCancelled(true);
        }else{
            player.sendMessage("§c获取在线时间失败,请重新进入服务器");
            event.setCancelled(true);
        }
    }
    public void modifyCheck(UUID uuid, String min) {
        try (Connection conn = sql.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE player_times SET " + min + " = 0 WHERE uuid = ?")) {
            ps.setString(1, uuid.toString());
            int updatedRows = ps.executeUpdate();
        } catch (SQLException e) {
            console.consoleMessage("Failed to update play time for player: " + uuid + " - " + e.getMessage());
        }
    }
    public Integer getCheck(UUID uuid, String min){
        Connection conn = sql.getConnection();
        Integer value = 0;
        try {
            String query = "SELECT " + min + " FROM player_times WHERE uuid = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, uuid.toString()); // 使用实际的玩家 UUID
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                value = resultSet.getInt(min); // 使用实际的列名
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
        return value;
    }
    public Integer getPlayTime(UUID uuid) throws SQLException {
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


    public Inventory getInventory() {
        return this.inventory;
    }

    public Player getPlayer() {
        return this.player;
    }
}
