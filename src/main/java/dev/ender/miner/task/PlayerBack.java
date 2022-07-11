package dev.ender.miner.task;

import dev.ender.miner.database.PlayerBackModel;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerBack extends BukkitRunnable {
    @Override
    public void run() {
        PlayerBackModel.sendBackPlayers();
    }
}