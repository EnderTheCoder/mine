package dev.ender.miner.task;

import dev.ender.miner.bossbar.MineAreaTeleportBackCountDownBar;
import dev.ender.miner.config.Config;
import dev.ender.miner.database.PlayerBackModel;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerBack extends BukkitRunnable {
    private final Player player;
    private final long startTime;

    public PlayerBack(Player player, Long startTime) {
        this.player = player;
        this.startTime = startTime;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void run() {
        if (player == null || !player.isOnline() || MineAreaTeleportBackCountDownBar.TELEPORT_BACK_BARS.get(this.player) == null) {
            this.cancel();
            return;
        }
        long elapsedTime = player.getWorld().getGameTime() - startTime;
        if (this.isCancelled()) return;

        MineAreaTeleportBackCountDownBar
                .TELEPORT_BACK_BARS
                .get(this.player)
                .setProgress((double) elapsedTime / Config.getTeleportBackTime());
        MineAreaTeleportBackCountDownBar
                .TELEPORT_BACK_BARS
                .get(this.player)
                .setTitle(String.format("剩余时间:%s秒", ((Config.getTeleportBackTime() - elapsedTime) / 20)));

        if (elapsedTime >= Config.getTeleportBackTime()) {
            PlayerBackModel.tpBack(player);
            MineAreaTeleportBackCountDownBar
                    .TELEPORT_BACK_BARS
                    .get(this.player)
                    .removePlayer(player);
            this.cancel();
        }
    }
}