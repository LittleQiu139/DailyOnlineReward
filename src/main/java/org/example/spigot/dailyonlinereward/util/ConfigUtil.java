package org.example.spigot.dailyonlinereward.util;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.spigot.dailyonlinereward.DailyOnlineReward;

public class ConfigUtil {
    public static File file;
    public static FileConfiguration config;
    public static FileConfiguration data;
    public static FileConfiguration message;

    public ConfigUtil() {
    }

    static {
        file = new File(DailyOnlineReward.plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);
        file = new File(DailyOnlineReward.plugin.getDataFolder(), "data.yml");
        data = YamlConfiguration.loadConfiguration(file);
        file = new File(DailyOnlineReward.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration(file);
    }
}
