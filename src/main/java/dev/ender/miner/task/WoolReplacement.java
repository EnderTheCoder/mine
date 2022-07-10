package dev.ender.miner.task;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class WoolReplacement extends BukkitRunnable {

    private Material material;
    private Location location;
    public WoolReplacement(Material material, Location location) {
        this.material = material;
        this.location = location;
    }

    @Override
    public void run() {
        location.getWorld().setBlockData(location, material.createBlockData());
    }
}
