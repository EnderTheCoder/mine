package dev.ender.miner.database;

import java.sql.*;

import static org.bukkit.Bukkit.getLogger;


public class SQLite {
    private Connection connection = null;
    private PreparedStatement statement = null;

    private Timestamp startTime;

    public void connect() {
        this.startTime = new Timestamp(System.currentTimeMillis());
        try {
            Class.forName("org.sqlite.JDBC");
            //如果没有数据库文件的话会自动创建
            this.connection = DriverManager.getConnection("jdbc:sqlite:./plugins/Miner/data.db");
        } catch (Exception e) {
            getLogger().severe(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || !this.connection.isValid(1000)) connect();
        } catch (SQLException e) {
            getLogger().severe("获取数据库连接时发生错误");

            e.printStackTrace();
        }
        return connection;
    }

    public void prepare(String sql) {
        try {
            statement = getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            getLogger().severe("预处理SQL语句时发生错误");
            e.printStackTrace();
        }
    }

    public void bindString(int number, String value) {
        try {
            statement.setString(number, value);
        } catch (SQLException e) {
            getLogger().severe("绑定参数时时发生错误");
            e.printStackTrace();
        }
    }

    public void bindInt(int number, int value) {
        try {
            statement.setInt(number, value);
        } catch (SQLException e) {
            getLogger().severe("绑定参数时时发生错误");
            e.printStackTrace();
        }

    }

    public void bindDouble(int number, double value) {
        try {
            statement.setDouble(number, value);
        } catch (SQLException e) {
            getLogger().severe("绑定参数时时发生错误");
            e.printStackTrace();
        }
    }

    public void bindLong(int number, long value) {
        try {
            statement.setLong(number, value);
        } catch (SQLException e) {
            getLogger().severe("绑定参数时时发生错误");
            e.printStackTrace();
        }
    }

    public void execute() {
        try {
            statement.execute();
        } catch (SQLException e) {
            getLogger().severe("执行SQL语句时发生错误");
            e.printStackTrace();
        }
    }

    public ResultSet result() {
        try {
            return statement.getResultSet();
        } catch (SQLException e) {
            getLogger().severe("获取数据库查询结果时发生错误");
            e.printStackTrace();
            return null;
        }
    }
    public void close() {
        //计算数据库查询所用时间并输出调试信息
        Timestamp endTime = new Timestamp(System.currentTimeMillis());

//        if (ConfigReader.isOnDebug())
//            getLogger().info(String.format("数据库连接关闭，本次查询共用时%s毫秒", endTime.getTime() - startTime.getTime()));

        try {
            this.connection.close();
        } catch (SQLException e) {
            getLogger().severe("关闭数据库连接时发生错误");
            e.printStackTrace();
        }
    }
    public boolean isTableExists(String tableName) {
        SQLite s = new SQLite();
        s.prepare("SELECT * FROM sqlite_master WHERE type='table' AND name = ?");
        s.bindString(1, tableName);
        s.execute();
        ResultSet resultSet = s.result();
        try {
            boolean res = resultSet.next();
            s.close();
            return res;
        } catch (SQLException e) {
            s.close();
            return false;
        }
    }
    public void initTable() {
        SQLite s = new SQLite();
        s.prepare("create table mine_area\n" +
                "(\n" +
                "    name    TEXT,\n" +
                "    start_x INTEGER,\n" +
                "    start_y INTEGER,\n" +
                "    start_z INTEGER,\n" +
                "    end_x   INTEGER,\n" +
                "    end_y   INTEGER,\n" +
                "    end_z   INTEGER,\n" +
                "    world   TEXT\n" +
                ");\n" +
                "\n" +
                "create unique index mine_area_name_uindex\n" +
                "    on mine_area (name);");
        s.execute();
        s.close();
    }
}
