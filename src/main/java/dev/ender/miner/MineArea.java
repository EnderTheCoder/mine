package dev.ender.miner;

import dev.ender.miner.database.SQLite;
import dev.ender.miner.exception.MineAreaNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class MineArea {
    String name;

    public static HashMap<String, MineArea> MINE_AREAS = getAllMineAreas();
    private Location startPos;
    private Location endPos;
    private Location spawnPos;

    public MineArea(String name) throws SQLException, MineAreaNotFoundException {
        this.name = name;
        SQLite sqLite = new SQLite();
        sqLite.prepare("select * from mine_area where name = ?");
        sqLite.bindString(1, name);
        sqLite.execute();
        ResultSet resultSet = sqLite.result();
        if (!resultSet.next()) {
            sqLite.close();
            throw new MineAreaNotFoundException(name);
        }
        this.startPos = new Location(
                Bukkit.getWorld(UUID.fromString(resultSet.getString("world"))),
                Integer.parseInt(resultSet.getString("start_x")),
                Integer.parseInt(resultSet.getString("start_y")),
                Integer.parseInt(resultSet.getString("start_z"))
        );
        this.endPos = new Location(
                Bukkit.getWorld(UUID.fromString(resultSet.getString("world"))),
                Integer.parseInt(resultSet.getString("end_x")),
                Integer.parseInt(resultSet.getString("end_y")),
                Integer.parseInt(resultSet.getString("end_z"))
        );
        this.spawnPos = new Location(
                Bukkit.getWorld(UUID.fromString(resultSet.getString("world"))),
                Double.parseDouble(resultSet.getString("spawn_x")),
                Double.parseDouble(resultSet.getString("spawn_y")),
                Double.parseDouble(resultSet.getString("spawn_z"))
        );
        sqLite.close();
    }

    public MineArea(String name, Location startPos, Location endPos, Location spawnPos) {
        this.name = name;
        this.startPos = startPos;
        this.endPos = endPos;
        this.spawnPos = spawnPos;
    }

    private boolean axisTest(double pos, int start, int end) {
        int startPos = Math.max(start, end);
        int endPos = Math.min(start, end);
        return endPos <= pos && pos <= startPos;
    }

    public boolean isInArea(Location location) {
        if (location == null) return false;
        return axisTest(location.getX(), startPos.getBlockX(), endPos.getBlockX())
                || axisTest(location.getY(), startPos.getBlockY(), endPos.getBlockY())
                || axisTest(location.getZ(), startPos.getBlockZ(), endPos.getBlockZ());
    }

    public void save() {
        try {
            new MineArea(this.name);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (MineAreaNotFoundException exception) {
            SQLite s = new SQLite();
            s.prepare("INSERT INTO mine_area (name, start_x, start_y, start_z, end_x, end_y, end_z, world, spawn_x, spawn_y, spawn_z) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            s.bindString(1, this.name);
            s.bindInt(2, this.startPos.getBlockX());
            s.bindInt(3, this.startPos.getBlockY());
            s.bindInt(4, this.startPos.getBlockZ());
            s.bindInt(5, this.endPos.getBlockX());
            s.bindInt(6, this.endPos.getBlockY());
            s.bindInt(7, this.endPos.getBlockZ());
            s.bindString(8, Objects.requireNonNull(this.startPos.getWorld()).getUID().toString());
            s.bindDouble(9, this.spawnPos.getX());
            s.bindDouble(10, this.spawnPos.getY());
            s.bindDouble(11, this.spawnPos.getZ());
            s.execute();
            s.close();
        }

        MINE_AREAS.put(this.name, this);
        SQLite sqLite = new SQLite();
        sqLite.prepare("UPDATE mine_area SET name = ?, start_x = ?, start_y = ?, start_z = ?, end_x = ?, end_y = ?, end_z = ?, spawn_x = ?, spawn_y = ?, spawn_z = ?, world = ? WHERE name = ?");
        sqLite.bindString(1, this.name);
        sqLite.bindInt(2, this.startPos.getBlockX());
        sqLite.bindInt(3, this.startPos.getBlockY());
        sqLite.bindInt(4, this.startPos.getBlockZ());
        sqLite.bindInt(5, this.endPos.getBlockX());
        sqLite.bindInt(6, this.endPos.getBlockY());
        sqLite.bindInt(7, this.endPos.getBlockZ());
        sqLite.bindDouble(8, this.spawnPos.getX());
        sqLite.bindDouble(9, this.spawnPos.getY());
        sqLite.bindDouble(10, this.spawnPos.getZ());
        sqLite.bindString(11, Objects.requireNonNull(this.startPos.getWorld()).getUID().toString());
        sqLite.bindString(12, this.name);
        sqLite.execute();
        sqLite.close();
//        MINE_AREAS.put(this.name, this);
    }

    public static HashMap<String, MineArea> getAllMineAreas() {
        SQLite sqLite = new SQLite();
        sqLite.prepare("SELECT name FROM mine_area");
        sqLite.execute();
        HashMap<String, MineArea> mineAreaList = new HashMap<>();
        ResultSet resultSet = sqLite.result();
        try {
            while (resultSet.next()) {
                mineAreaList.put(resultSet.getString("name"), new MineArea(resultSet.getString("name")));
            }
        } catch (SQLException | MineAreaNotFoundException e) {
            e.printStackTrace();
        }
        sqLite.close();
        return mineAreaList;
    }
    public void setStartPos(Location location) {
        this.startPos = location;
    }

    public void setEndPos(Location location) {
        this.endPos = location;
    }

    public void setSpawnPos(Location location) {
        this.spawnPos = location;
    }

    public String getName() {
        return this.name;
    }

    public Location getStartPos() {
        return this.startPos;
    }

    public Location getEndPos() {
        return this.endPos;
    }

    public Location getSpawnPos() {
        return this.spawnPos;
    }

    public void remove() {
        SQLite sqLite = new SQLite();
        sqLite.prepare("DELETE FROM mine_area WHERE name = ?");
        sqLite.bindString(1, this.name);
        sqLite.execute();
        sqLite.close();
        MINE_AREAS.remove(this.name);
    }

    public static void removeMineArea(String name) {
        MineArea mineArea = MINE_AREAS.get(name);
        mineArea.remove();
        removeMineAreaFromCache(name);
    }

    public static void removeMineAreaFromCache(String name) {
        MINE_AREAS.put(name, null);
    }

    public static void saveAllMineAreas() {
        for (MineArea mineArea : MINE_AREAS.values()) {
            mineArea.save();
        }
    }

}
