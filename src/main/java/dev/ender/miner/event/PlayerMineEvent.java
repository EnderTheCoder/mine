package dev.ender.miner.event;

import dev.ender.miner.MineArea;
import dev.ender.miner.Miner;
import dev.ender.miner.task.WoolReplacement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Wool;

public class PlayerMineEvent implements Listener {
    @EventHandler
    public static void onPlayerMine(BlockBreakEvent event) {
        if (!event.getBlock().getType().getKey().toString().contains("_ore")) return;

        Location blockLocation = event.getBlock().getLocation();
        for (MineArea mineArea : MineArea.MINE_AREAS.values()) {
            if (mineArea.isInArea(blockLocation)) {
                new WoolReplacement(Material.WHITE_WOOL, event.getBlock().getLocation()).runTaskLater(Miner.INSTANCE, 1);
                break;
            }
        }
    }
    @EventHandler
    public static void onPlayerTP() {

    }
}
