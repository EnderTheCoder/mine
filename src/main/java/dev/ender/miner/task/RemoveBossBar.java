package dev.ender.miner.task;

import dev.ender.miner.MineArea;
import dev.ender.miner.bossbar.MineAreaRefuelCountDownBar;
import dev.ender.miner.bossbar.MineAreaTeleportBackCountDownBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveBossBar extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (MineArea.isInAnyMineArea(player.getLocation())) continue;
            MineAreaRefuelCountDownBar.REFUEL_BAR.removePlayer(player);
            if (MineAreaTeleportBackCountDownBar.TELEPORT_BACK_BARS.get(player) != null) {
                MineAreaTeleportBackCountDownBar.TELEPORT_BACK_BARS.get(player).removePlayer(player);
                MineAreaTeleportBackCountDownBar.TELEPORT_BACK_BARS.remove(player);
            }
        }
    }
}
