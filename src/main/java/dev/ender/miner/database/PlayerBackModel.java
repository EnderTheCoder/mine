package dev.ender.miner.database;

import dev.ender.miner.Miner;
import dev.ender.miner.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

/*
    Data class about player back.
 */
class PlayerData {
    public Location lastLocation;
    public long backTime;
    public PlayerData(Location location, long backTime) {
        lastLocation = location;
        this.backTime = backTime;
    }
}

public class PlayerBackModel {
    private static final HashMap<Player, PlayerData> PLAYER_BACK_DATA = new HashMap<>();
    private static int MINE_TIME = 0;
    public static void initMineTimeConfig() {
        ConfigurationSection mineTimeConfiguration = Config.CONFIG.getConfigurationSection("");
        if (mineTimeConfiguration == null) {
            Bukkit.getLogger().warning("Config File Read error");
            return;
        }
        MINE_TIME = mineTimeConfiguration.getInt("mine_time");
    }
    public PlayerBackModel() {
        initMineTimeConfig();
    }
    /**
        Must be called after player has tp to mine area.
     */
    public static void addPlayer(Player player) {
        PlayerData playerBackData = new PlayerData(player.getLocation(), player.getPlayerTime() + MINE_TIME);
        PLAYER_BACK_DATA.put(player, playerBackData);
    }
    public static void removePlayer(Player player) {
        PLAYER_BACK_DATA.remove(player);
    }
    public static PlayerData getPlayerData(Player player) {
        return PLAYER_BACK_DATA.get(player);
    }
    public static boolean hasOutOfTime(Player player) {
        PlayerData playerData = getPlayerData(player);
        return Objects.requireNonNull(playerData.lastLocation.getWorld()).getTime() >= playerData.backTime;
    }
    public static void tpBack(Player player) {
        if (!player.isOnline()) return;
        player.teleport(getPlayerData(player).lastLocation);
        removePlayer(player);
        player.sendMessage(ChatColor.GREEN + Miner.PREFIX + "挖矿时间已超时");
    }
    public static void sendBackPlayers() {
        for (Player player : PLAYER_BACK_DATA.keySet()) {
            PlayerData playerData = PLAYER_BACK_DATA.get(player);
            if (hasOutOfTime(player)) {
                tpBack(player);
            }
        }
    }
}
