package dev.ender.miner.event;

import dev.ender.miner.MineArea;
import dev.ender.miner.database.PlayerBackModel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinAndBackEvent implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (MineArea.isInAnyMineArea(player.getLocation()) && PlayerBackModel.hasOutOfTime(player)) {
            PlayerBackModel.tpBack(player);
        }
    }
}
