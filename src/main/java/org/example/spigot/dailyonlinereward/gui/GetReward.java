/*
package org.example.spigot.dailyonlinereward.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.example.spigot.dailyonlinereward.util.ConfigUtil;
import org.example.spigot.dailyonlinereward.util.ConnectMysql;

public class GetReward implements InventoryHolder, Listener {
    Inventory inventory;
    Player player;
    private final ConnectMysql sql;

    public GetReward(Player player) {
        this.player = player;
        this.sql = new ConnectMysql();
        this.inventory = Bukkit.createInventory(this, 1 * 9, ChatColor.translateAlternateColorCodes('&', ConfigUtil.message.getString("Title")));




        //Bukkit.getPluginManager().registerEvents(this, DailyOnlineReward.getPlugin(DailyOnlineReward.class));
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 检查被点击的物品栏是否为当前物品栏，并且取消点击事件
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
*/