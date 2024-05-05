package org.example.spigot.dailyonlinereward.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.spigot.dailyonlinereward.DailyOnlineReward;
import org.example.spigot.dailyonlinereward.gui.OpenMenu;

public class PlayerCMD implements CommandExecutor {
    private final JavaPlugin plugin;
    public PlayerCMD(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage("§a[每日在线奖励]只有玩家才能执行此命令！");
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 1) {
            Inventory inventory;
            if (args[0].equalsIgnoreCase("open")) {
                if (sender.hasPermission("dailyonlinereward.open")) {
                    //inventory = (new OpenMenu(player)).getInventory();
                    //player.openInventory(inventory);
                    //sender.sendMessage("§a[每日在线奖励]已打开界面.");
                    (new BukkitRunnable() {
                        public void run() {
                            player.sendMessage("§a[每日在线奖励]已打开界面.");
                            final Inventory inventory = (new OpenMenu(player)).getInventory();
                            (new BukkitRunnable() {
                                public void run() {
                                    player.openInventory(inventory);
                                }
                            }).runTask(DailyOnlineReward.plugin);
                        }
                    }).runTaskAsynchronously(DailyOnlineReward.plugin);
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
