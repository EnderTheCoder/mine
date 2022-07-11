package dev.ender.miner.event;

import dev.ender.miner.MineArea;
import dev.ender.miner.Miner;
import dev.ender.miner.config.Config;
import dev.ender.miner.task.MineReplacement;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class MineAreaEvent implements Listener {
    @EventHandler
    public static void onPlayerBreak(BlockBreakEvent event) {
        Location blockLocation = event.getBlock().getLocation();
        if (!MineArea.isInAnyMineArea(blockLocation)) return; // not in mine area
        Material materialToReplace = Config.MINE_REPLACEMENT.get(event.getBlock().getBlockData().getMaterial());


        if (materialToReplace == null) {
            if (!event.getPlayer().isOp()) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + Miner.PREFIX + "你没有在矿区破坏方块的权限");
                event.setCancelled(true);
            }
        } else {
            new MineReplacement(materialToReplace, blockLocation).runTaskLater(Miner.INSTANCE, 1);
        }
    }

    @EventHandler
    public static void onPlayerPlace(BlockPlaceEvent event) {
        if (!MineArea.isInAnyMineArea(event.getBlock().getLocation())) return;
        if (!event.getPlayer().isOp()) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + Miner.PREFIX + "你没有在矿区放置方块的权限");
            event.setCancelled(true);
        }
    }
}