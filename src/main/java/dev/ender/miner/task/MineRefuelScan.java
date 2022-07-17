package dev.ender.miner.task;

import dev.ender.miner.MineArea;
import dev.ender.miner.Miner;
import dev.ender.miner.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class MineRefuelScan extends BukkitRunnable {
    /**
     * [start, end]
     * i + 1
     */
    private class IntegerWrapper {
        public int Int;
        IntegerWrapper(int num) {
            Int = num;
        }
    }
    private boolean iterator(IntegerWrapper start, IntegerWrapper end) {
        if (start.Int == end.Int) return false;
        start.Int += start.Int < end.Int ? 1 : -1;
        return true;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long blockCount = 0;
        for (MineArea mineArea : MineArea.MINE_AREAS.values()) {
            IntegerWrapper startX = new IntegerWrapper(mineArea.getStartPos().getBlockX());
            IntegerWrapper endX = new IntegerWrapper(mineArea.getEndPos().getBlockX());
            do {
                IntegerWrapper startY = new IntegerWrapper(mineArea.getStartPos().getBlockY());
                IntegerWrapper endY = new IntegerWrapper(mineArea.getEndPos().getBlockY());
                do {
                    IntegerWrapper startZ = new IntegerWrapper(mineArea.getStartPos().getBlockZ());
                    IntegerWrapper endZ = new IntegerWrapper(mineArea.getEndPos().getBlockZ());
                    do {
                        blockCount++;
                        Material refuelMaterial = Config.OPPOSITE_MINE_REPLACEMENT.get(mineArea.getWorld().getBlockAt(startX.Int, startY.Int, startZ.Int).getType());
                        if (refuelMaterial == null) continue;
                        mineArea.getWorld().setBlockData(startX.Int, startY.Int, startZ.Int, refuelMaterial.createBlockData());
                        new MineReplacement(refuelMaterial, new Location(mineArea.getWorld(), startX.Int, startY.Int, startZ.Int)).runTaskLater(Miner.INSTANCE, 0);
                    } while (iterator(startZ, endZ));
                } while (iterator(startY, endY));
            } while (iterator(startX, endX));
        }
        Bukkit.getLogger().info("This round of refuel (" + blockCount +  " blocks) spent " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
