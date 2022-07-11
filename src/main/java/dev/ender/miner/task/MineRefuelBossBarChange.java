package dev.ender.miner.task;

import dev.ender.miner.bossbar.MineAreaRefuelCountDownBar;
import dev.ender.miner.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class MineRefuelBossBarChange extends BukkitRunnable {

    private final long startTime;

    public MineRefuelBossBarChange() {
        this.startTime = Bukkit.getWorlds().get(0).getGameTime();
    }
    @Override
    public void run() {
        long elapsedTime = (Bukkit.getWorlds().get(0).getGameTime() - startTime) % Config.getRefuelTime();
        MineAreaRefuelCountDownBar.REFUEL_BAR.setProgress((double) elapsedTime / Config.getRefuelTime());
        MineAreaRefuelCountDownBar.REFUEL_BAR
                .setTitle(String.format("距离下一次更新:%s秒", ((Config.getRefuelTime() - elapsedTime) / 20)));
    }
}
