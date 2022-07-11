package dev.ender.miner.task;

import dev.ender.miner.MineArea;
import dev.ender.miner.config.Config;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class MineRefuel extends BukkitRunnable {

    @Override
    public void run() {
        for (MineArea mineArea : MineArea.MINE_AREAS.values()) {
            for (int x = mineArea.getStartPos().getBlockX(); x != mineArea.getEndPos().getBlockX();) {
                x = mineArea.getStartPos().getBlockX() > mineArea.getEndPos().getBlockX() ? x - 1 : x + 1;
                for (int y = mineArea.getStartPos().getBlockY(); y != mineArea.getEndPos().getBlockY();) {
                    y = mineArea.getStartPos().getBlockY() > mineArea.getEndPos().getBlockY() ? y - 1 : y + 1;
                    for (int z = mineArea.getStartPos().getBlockZ(); z != mineArea.getEndPos().getBlockZ();) {
                        z = mineArea.getStartPos().getBlockZ() > mineArea.getEndPos().getBlockZ() ? z - 1 : z + 1;
                        Material refuelMaterial = Config.OPPOSITE_MINE_REPLACEMENT.get(mineArea.getWorld().getBlockAt(x, y, z).getType());
                        if (refuelMaterial == null) continue;
                        mineArea.getWorld().setBlockData(x, y, z, refuelMaterial.createBlockData());
                    }
                }
            }
        }
    }
}
