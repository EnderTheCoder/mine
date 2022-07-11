package dev.ender.miner.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class MineAreaRefuelCountDownBar {
    public static BossBar REFUEL_BAR = Bukkit.createBossBar(
            "矿场刷新",
            BarColor.GREEN,
            BarStyle.SEGMENTED_12,
            BarFlag.PLAY_BOSS_MUSIC
    );
}
