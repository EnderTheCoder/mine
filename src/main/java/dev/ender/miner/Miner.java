package dev.ender.miner;

import dev.ender.miner.command.MineCommand;
import dev.ender.miner.config.Config;
import dev.ender.miner.database.SQLite;
import dev.ender.miner.event.JoinAndBackEvent;
import dev.ender.miner.event.MineAreaEvent;
import dev.ender.miner.event.PlayerSelectEvent;
import dev.ender.miner.exception.UnexpectedConfigFileException;
import dev.ender.miner.task.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.Objects;

public final class Miner extends JavaPlugin {
    public static String PREFIX = "[miner]";
    public static Plugin INSTANCE;
    private static BukkitRunnable MINE_REFUEL_TASK, REMOVE_BOSS_BAR, MINE_REFUEL_TASK_BOSS_BAR_CHANGE;
    @Override
    public void onEnable() {
        Miner.INSTANCE = this;
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new MineAreaEvent(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerSelectEvent(), this);
        Bukkit.getPluginManager().registerEvents(new JoinAndBackEvent(), this);
        if (Bukkit.getPluginCommand("miner") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("miner")).setExecutor(new MineCommand());
        }
        SQLite s = new SQLite();
        if (!s.isTableExists("mine_area")) s.initTable();

        new SQLiteSave().runTaskTimerAsynchronously(this, 20, 200);
        startBackgroundTask();

        try {
            Config.initMineReplacement();
        } catch (UnexpectedConfigFileException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("配置文件错误，你输入的方块不存在");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getLogger().info(Color.CYAN + "本插件由EnderTheCoder和Null联合制作");
        Bukkit.getLogger().info(Color.CYAN + "定制插件请联系QQ1991455223,量大优惠，最高半价");
        Bukkit.getLogger().info(Color.CYAN + "原作者保留所有版权");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().warning("插件正在被禁用");
        MineArea.saveAllMineAreas();
        startBackgroundTask();
    }

    public static void startBackgroundTask() {

        if (MINE_REFUEL_TASK == null) {
            MINE_REFUEL_TASK = new MineRefuelScan();
            MINE_REFUEL_TASK.runTaskTimerAsynchronously(Miner.INSTANCE, 0, Config.getRefuelTime());
        }

        if (REMOVE_BOSS_BAR == null) {
            REMOVE_BOSS_BAR = new RemoveBossBar();
            REMOVE_BOSS_BAR.runTaskTimerAsynchronously(Miner.INSTANCE, 20, 200);
        }

        if (MINE_REFUEL_TASK_BOSS_BAR_CHANGE == null) {
            MINE_REFUEL_TASK_BOSS_BAR_CHANGE = new MineRefuelBossBarChange();
            MINE_REFUEL_TASK_BOSS_BAR_CHANGE.runTaskTimer(Miner.INSTANCE, 0, 10);
        }

    }
    public static void stopBackgroundTask() {
        MINE_REFUEL_TASK.cancel();
        MINE_REFUEL_TASK = null;

        REMOVE_BOSS_BAR.cancel();
        REMOVE_BOSS_BAR = null;

        MINE_REFUEL_TASK_BOSS_BAR_CHANGE.cancel();
        MINE_REFUEL_TASK_BOSS_BAR_CHANGE = null;

        for (PlayerBack task : PlayerBackModel.PLAYER_BACK_TASKS) {
            PlayerBackModel.tpBack(task.getPlayer());
            task.cancel();
        }
        PlayerBackModel.PLAYER_BACK_TASKS.clear();
    }
}
