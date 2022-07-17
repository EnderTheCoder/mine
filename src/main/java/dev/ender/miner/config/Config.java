package dev.ender.miner.config;

import dev.ender.miner.Miner;
import dev.ender.miner.database.PlayerBackModel;
import dev.ender.miner.exception.UnexpectedConfigFileException;
import jdk.tools.jlink.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Steerable;

import java.util.HashMap;
import java.util.Objects;

public class Config {
    public static Configuration CONFIG = Miner.INSTANCE.getConfig();
    public static HashMap<Material, Material> MINE_REPLACEMENT = new HashMap<>();
    public static HashMap<Material, Material> OPPOSITE_MINE_REPLACEMENT = new HashMap<>();
    public static void initMineReplacement() throws UnexpectedConfigFileException {
        ConfigurationSection mineReplacementConfig = (CONFIG.getConfigurationSection("mine_replacement"));
        if (mineReplacementConfig == null) return;
        for (String key : mineReplacementConfig.getKeys(false)) {
            Material materialToBeReplaced = Material.getMaterial(key);
            Material materialAfterReplacement = Material.getMaterial(Objects.requireNonNull(mineReplacementConfig.getString(key)));

            if (materialToBeReplaced == null) throw new UnexpectedConfigFileException(key);
            if (materialAfterReplacement == null) throw new UnexpectedConfigFileException(Objects.requireNonNull(mineReplacementConfig.getString(key)));

            MINE_REPLACEMENT.put(materialToBeReplaced, materialAfterReplacement);
            OPPOSITE_MINE_REPLACEMENT.put(materialAfterReplacement, materialToBeReplaced);
        }
    }

    public static String getString(String str_to_get){
        String s = CONFIG.getString(str_to_get);
        if(Objects.isNull(s)){
            Bukkit.getLogger().severe("配置文件错误");
            Bukkit.getPluginManager().disablePlugin(Miner.INSTANCE);
        }
        return s;
    }


    public static int getRefuelTime() {
        return CONFIG.getInt("refuel_time");
    }

    public static int getTeleportBackTime() {
        return CONFIG.getInt("mine_time");
    }

    public static String getTeleportBackCommand() {
        return CONFIG.getString("command_back");
    }

    public static void reload() {
        Miner.INSTANCE.reloadConfig();
        Config.CONFIG = Miner.INSTANCE.getConfig();
        try {
            Config.initMineReplacement();
            PlayerBackModel.initMineTimeConfig();
        } catch (UnexpectedConfigFileException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("配置文件错误，你输入的方块不存在");
            Bukkit.getPluginManager().disablePlugin(Miner.INSTANCE);
        }
    }
}
