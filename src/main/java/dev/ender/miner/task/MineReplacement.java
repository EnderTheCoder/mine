package dev.ender.miner.task;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class MineReplacement extends BukkitRunnable {

    private final Material material;
    private final Location location;
    public MineReplacement(Material material, Location location) {
        this.material = material;
        this.location = location;
    }

    @Override
    public void run() {
        location.getWorld().setBlockData(location, material.createBlockData());
    }
}
