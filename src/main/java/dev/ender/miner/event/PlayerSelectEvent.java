package dev.ender.miner.event;

import dev.ender.miner.MineArea;
import dev.ender.miner.Miner;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Objects;


public class PlayerSelectEvent implements Listener {
    public enum ClickState {
        START_CLICK, END_CLICK, SPAWN_CLICK
    }
    public static HashMap<Player, ClickState> IS_ON_SELECT = new HashMap<>();
    public static HashMap<Player, String> MINE_AREA_ON_SETTING_UP = new HashMap<>();
    public static HashMap<Player, Location> START_BLOCK_LOC = new HashMap<>();
    public static HashMap<Player, Location> END_BLOCK_LOC = new HashMap<>();

    @EventHandler
    public static void onPlayerSelect(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (IS_ON_SELECT.get(player) == null) return;

        switch (IS_ON_SELECT.get(player)) {
            case START_CLICK: {
                Location blockLocation = Objects.requireNonNull(event.getClickedBlock()).getLocation().getBlock().getLocation();
                START_BLOCK_LOC.put(player, blockLocation);
                IS_ON_SELECT.put(player, ClickState.END_CLICK);
                player.sendMessage(ChatColor.AQUA + Miner.PREFIX + "已点击第一个方块");
                event.setCancelled(true);
                break;
            }
            case END_CLICK: {
                Location blockLocation = Objects.requireNonNull(event.getClickedBlock()).getLocation().getBlock().getLocation();
                if (!Objects.equals(blockLocation.getWorld(), START_BLOCK_LOC.get(player).getWorld())) {
                    player.sendMessage(ChatColor.RED + Miner.PREFIX + "不能在不同的世界点击");
                    resetClick(player);
                    break;
                }
                END_BLOCK_LOC.put(player, blockLocation);
                IS_ON_SELECT.put(player, ClickState.SPAWN_CLICK);
                player.sendMessage(ChatColor.AQUA + Miner.PREFIX + "已点击第二个方块");
                event.setCancelled(true);
                break;
            }
            case SPAWN_CLICK: {
                Location spawnLocation = Objects.requireNonNull(event.getClickedBlock()).getLocation().getBlock().getLocation();
                if (!Objects.equals(spawnLocation.getWorld(), START_BLOCK_LOC.get(player).getWorld())) {
                    player.sendMessage(ChatColor.RED + Miner.PREFIX + "不能在不同的世界点击");
                    resetClick(player);
                    break;
                }
                if (!isInArea(spawnLocation, START_BLOCK_LOC.get(player), END_BLOCK_LOC.get(player))) {
                    player.sendMessage(ChatColor.RED + Miner.PREFIX + "传送点不在此区域内！");
                }
                IS_ON_SELECT.remove(player);
                MineArea mineArea = new MineArea(MINE_AREA_ON_SETTING_UP.get(player), START_BLOCK_LOC.get(player), END_BLOCK_LOC.get(player), spawnLocation);
                MineArea.MINE_AREAS.put(mineArea.getName(), mineArea);
                player.sendMessage(ChatColor.GREEN + Miner.PREFIX + "已成功设置矿场");
                resetClick(player);
                event.setCancelled(true);
                break;
            }
        }
    }

    public static void resetClick(Player player) {
        IS_ON_SELECT.remove(player);
        MINE_AREA_ON_SETTING_UP.remove(player);
        START_BLOCK_LOC.remove(player);
        END_BLOCK_LOC.remove(player);
    }
    private static boolean axisTest(double pos, int start, int end) {
        int startPos = Math.max(start, end);
        int endPos = Math.min(start, end);
        return endPos <= pos && pos <= startPos;
    }

    private static boolean isInArea(Location location, Location startPos, Location endPos) {
        if (location == null) return false;
        return axisTest(location.getX(), startPos.getBlockX(), endPos.getBlockX())
                || axisTest(location.getY(), startPos.getBlockY(), endPos.getBlockY())
                || axisTest(location.getZ(), startPos.getBlockZ(), endPos.getBlockZ());
    }
}