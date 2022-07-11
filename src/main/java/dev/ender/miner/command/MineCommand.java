package dev.ender.miner.command;

import dev.ender.miner.MineArea;
import dev.ender.miner.Miner;
import dev.ender.miner.bossbar.MineAreaRefuelCountDownBar;
import dev.ender.miner.bossbar.MineAreaTeleportBackCountDownBar;
import dev.ender.miner.config.Config;
import dev.ender.miner.database.PlayerBackModel;
import dev.ender.miner.event.PlayerSelectEvent;
import dev.ender.miner.task.PlayerBack;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

public class MineCommand implements CommandExecutor {

    /**
     * /miner add <name> <start_pos> <end_pos> <spawn_pos>
     * /miner add <name>
     * /miner remove <name>
     * /miner edit <name> <start_pos> <end_pos> <spawn_pos>
     * /miner tp <name>
     * /miner reload
     */
    private Location parseLocation(World world, String x, String y, String z) {
        int locationX = Integer.parseInt(x);
        int locationY = Integer.parseInt(y);
        int locationZ = Integer.parseInt(z);
        return new Location(world, locationX, locationY, locationZ);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "add": {
                if (!(sender instanceof Player)) return false;
                Player player = (Player) sender;
                if (!player.hasPermission("miner.add")) {
                    player.sendMessage(ChatColor.RED + Miner.PREFIX + "你没有权限使用这条命令");
                    return true;
                }
                if (args.length == 2) {
                    PlayerSelectEvent.IS_ON_SELECT.put(player, PlayerSelectEvent.ClickState.START_CLICK);
                    PlayerSelectEvent.MINE_AREA_ON_SETTING_UP.put(player, args[1]);
                    sender.sendMessage(ChatColor.AQUA + String.format(Miner.PREFIX + "正在设定新矿场'%s'，请依次点击三个方块以分别设定新矿场的<起始位置>，<结束位置>，<出生点位置>", args[1]));
                } else if (args.length == 11) {
                    try {
                        Location startPos = parseLocation(player.getWorld(), args[2], args[3], args[4]),
                                endPos = parseLocation(player.getWorld(), args[5], args[6], args[7]),
                                spawnPos = parseLocation(player.getWorld(), args[8], args[9], args[10]);
                        MineArea mineArea = new MineArea(args[1], startPos, endPos, spawnPos);
                        MineArea.MINE_AREAS.put(args[1], mineArea);
                        sender.sendMessage(ChatColor.GREEN + Miner.PREFIX + String.format("矿场 %s 创建成功", args[1]));
                    } catch (NumberFormatException e) {
                        return false;
                    }
                } else return false;
                break;
            }
            case "remove": {
                if (args.length != 2) return false;
                if (!sender.hasPermission("miner.remove")) {
                    sender.sendMessage(ChatColor.RED + Miner.PREFIX + "你没有权限使用这条命令");
                    return true;
                }
                MineArea mineArea = MineArea.MINE_AREAS.get(args[1]);
                if (mineArea == null) {
                    sender.sendMessage(ChatColor.YELLOW + String.format(Miner.PREFIX + "未找到您输入的矿场'%s'", args[1]));
                    return true;
                }
                MineArea.removeMineArea(args[1]);
                sender.sendMessage(ChatColor.GREEN + Miner.PREFIX + String.format("矿场 %s 已删除", args[1]));
                break;
            }
            case "edit": {
                if (!(sender instanceof Player)) return false;
                if (!sender.hasPermission("miner.edit")) {
                    sender.sendMessage(ChatColor.RED + Miner.PREFIX + "你没有权限使用这条命令");
                    return true;
                }
                World world = ((Player) sender).getWorld();
                if (args.length != 11) return false;
                MineArea mineArea = MineArea.MINE_AREAS.get(args[1]);
                if (mineArea == null) {
                    sender.sendMessage(ChatColor.YELLOW + String.format(Miner.PREFIX + "未找到您输入的矿场'%s'", args[1]));
                    return true;
                }
                try {
                    Location startPos = parseLocation(world, args[2], args[3], args[4]),
                            endPos = parseLocation(world, args[5], args[6], args[7]),
                            spawnPos = parseLocation(world, args[8], args[9], args[10]);
                    mineArea.setStartPos(startPos);
                    mineArea.setEndPos(endPos);
                    mineArea.setSpawnPos(spawnPos);
                    MineArea.MINE_AREAS.put(args[1], mineArea);
                    sender.sendMessage(ChatColor.GREEN + Miner.PREFIX + String.format("矿场 %s 已更新", args[1]));
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            }
            case "list": {
                if (args.length != 1) return false;
                if (!sender.hasPermission("miner.list")) {
                    sender.sendMessage(ChatColor.RED + Miner.PREFIX + "你没有权限使用这条命令");
                    return true;
                }
                sender.sendMessage(ChatColor.YELLOW + Miner.PREFIX + "所有矿场信息：\n");
                sender.sendMessage(ChatColor.YELLOW + "名称 世界 传送点");
                for (MineArea mineArea : MineArea.MINE_AREAS.values()) {
                    sender.sendMessage(ChatColor.AQUA + String.format("%s || %s || (x:%s,y:%s,z:%s)\n",
                                    mineArea.getName(),
                                    mineArea.getWorld().getName(),
                                    mineArea.getSpawnPos().getX(),
                                    mineArea.getSpawnPos().getY(),
                                    mineArea.getSpawnPos().getZ()
                            )
                    );
                }
                break;
            }
            case "tp": {
                if (args.length != 2) return false;
                if (!(sender instanceof Player)) return false;
                if (!sender.hasPermission("miner.tp")) {
                    sender.sendMessage(ChatColor.RED + Miner.PREFIX + "你没有权限使用这条命令");
                    return true;
                }
                MineArea mineArea = MineArea.MINE_AREAS.get(args[1]);
                if (mineArea == null) {
                    sender.sendMessage(ChatColor.YELLOW + String.format(Miner.PREFIX + "未找到您输入的矿场'%s'", args[1]));
                    return true;
                }
                Player player = (Player) sender;
                player.teleport(mineArea.getSpawnPos());
                PlayerBackModel.addPlayer(player);
                //bossbar
                MineAreaRefuelCountDownBar.REFUEL_BAR.addPlayer(player);
                MineAreaTeleportBackCountDownBar.TELEPORT_BACK_BARS.get(player).addPlayer(player);

                player.sendMessage(ChatColor.GREEN + String.format(Miner.PREFIX + "已经将您传送至矿场'%s'", args[1]));
                break;
            }
            case "reload": {
                Config.reload();
                sender.sendMessage(ChatColor.GREEN + Miner.PREFIX + "插件重载成功");
                break;
            }
            default: {
                return false;
            }
        }
        return true;
    }
}
