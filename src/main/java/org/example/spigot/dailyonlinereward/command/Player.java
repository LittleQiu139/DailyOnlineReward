package org.example.spigot.dailyonlinereward.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.spigot.dailyonlinereward.gui.OpenMenu;

public class Player implements CommandExecutor {
    private final JavaPlugin plugin;
    public Player(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage("§a[每日在线奖励]只有玩家才能执行此命令！");
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        Inventory inventory = (new OpenMenu(player)).getInventory();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("open")) {
                if (sender.hasPermission("dailyonlinereward.open")) {
                    player.openInventory(inventory);
                    sender.sendMessage("§a[每日在线奖励]已打开界面.");
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
