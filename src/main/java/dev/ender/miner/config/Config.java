package dev.ender.miner.config;

import dev.ender.miner.Miner;
import dev.ender.miner.exception.UnexpectedConfigFileException;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

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

    public static void reload() {
        Miner.INSTANCE.reloadConfig();
        Config.CONFIG = Miner.INSTANCE.getConfig();
    }
}
