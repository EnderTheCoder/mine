package dev.ender.miner;

import dev.ender.miner.command.MineCommand;
import dev.ender.miner.database.SQLite;
import dev.ender.miner.event.PlayerMineEvent;
import dev.ender.miner.event.PlayerSelectEvent;
import dev.ender.miner.task.SQLiteSave;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class Miner extends JavaPlugin {
    public static String PREFIX = "[miner]";
    public static Plugin INSTANCE;
    @Override
    public void onEnable() {
        Miner.INSTANCE = this;
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerMineEvent(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerSelectEvent(), this);
        if (Bukkit.getPluginCommand("miner") != null) {
            Bukkit.getPluginCommand("miner").setExecutor(new MineCommand());
        }
        SQLite s = new SQLite();
        if (!s.isTableExists("mine_area")) s.initTable();
        new SQLiteSave().runTaskTimerAsynchronously(this, 20, 200);

        Bukkit.getLogger().info(Color.CYAN + "本插件由EnderTheCoder和Null联合制作");
        Bukkit.getLogger().info(Color.CYAN + "定制插件请联系QQ1991455223,量大优惠，最高半价");
        Bukkit.getLogger().info(Color.CYAN + "原作者保留所有版权");


    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().warning("插件正在被禁用");
        MineArea.saveAllMineAreas();
    }
}