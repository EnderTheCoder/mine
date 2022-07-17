package dev.ender.miner.database;

import dev.ender.miner.Miner;
import dev.ender.miner.bossbar.MineAreaTeleportBackCountDownBar;
import dev.ender.miner.config.Config;
import dev.ender.miner.exception.UnexpectedConfigFileException;
import dev.ender.miner.task.PlayerBack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlayerBackModel {


    public static List<PlayerBack> PLAYER_BACK_TASKS = new ArrayList<>();

    /**
     * Must be called after player has tp to mine area.
     *
     * @param player
     */
    public static void addPlayer(Player player) {
        new PlayerBack(player, player.getWorld().getGameTime()).runTaskTimer(Miner.INSTANCE, 1, 10);
        MineAreaTeleportBackCountDownBar.TELEPORT_BACK_BARS.put(player, Bukkit.createBossBar(
                "挖矿时间",
                BarColor.BLUE,
                BarStyle.SEGMENTED_12,
                BarFlag.PLAY_BOSS_MUSIC
        ));
    }

    public static void tpBack(Player player) {
        if (!player.isOnline()) return;
        player.performCommand(Config.getTeleportBackCommand());
        //player.sendMessage(ChatColor.AQUA + Miner.PREFIX + "挖矿时间已结束");
        player.sendMessage(Config.getString("time_up"));
    }
}
