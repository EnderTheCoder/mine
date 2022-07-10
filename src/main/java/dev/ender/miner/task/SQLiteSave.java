package dev.ender.miner.task;

import dev.ender.miner.MineArea;
import org.bukkit.scheduler.BukkitRunnable;

public class SQLiteSave extends BukkitRunnable {
    @Override
    public void run() {
        MineArea.saveAllMineAreas();
    }
}
