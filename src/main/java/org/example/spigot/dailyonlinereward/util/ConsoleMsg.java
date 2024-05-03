package org.example.spigot.dailyonlinereward.util;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleMsg {
    public void consoleMessage(String msg){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(msg);
    }

}
