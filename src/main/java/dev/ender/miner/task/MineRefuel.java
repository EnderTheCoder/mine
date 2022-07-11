package dev.ender.miner.task;

import dev.ender.miner.MineArea;
import dev.ender.miner.config.Config;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class MineRefuel extends BukkitRunnable {
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
                        Material refuelMaterial = Config.OPPOSITE_MINE_REPLACEMENT.get(mineArea.getWorld().getBlockAt(startX.Int, startY.Int, startZ.Int).getType());
                        if (refuelMaterial == null) continue;
                        mineArea.getWorld().setBlockData(startX.Int, startY.Int, startZ.Int, refuelMaterial.createBlockData());
                    } while (iterator(startZ, endZ));
                } while (iterator(startY, endY));
            } while (iterator(startX, endX));
        }
    }
}
