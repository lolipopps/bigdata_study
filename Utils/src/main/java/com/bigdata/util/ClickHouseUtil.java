package com.bigdata.util;


import lombok.extern.slf4j.Slf4j;
import ru.yandex.clickhouse.BalancedClickhouseDataSource;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseStatement;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@Slf4j
public class ClickHouseUtil {

    private static String driver = "";
    private static String url = "";
    private static String username = "";
    private static String password = "";

    static {//静态代码块注册驱动
        try {
            Properties properties = new Properties();
            log.info("加载配置文件,初始化clickhouse数据库链接");
            properties.load(MysqlUtil.class.getResourceAsStream("/db.properties"));
            driver = properties.getProperty("ck.driver");
            url = properties.getProperty("ck.url");
            username = properties.getProperty("ck.username");
            password = properties.getProperty("ck.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //返回连接
    public static ClickHouseConnection getConnection() {
        try {
            ClickHouseProperties props = new ClickHouseProperties();
            props.setUser(username);
            props.setPassword(password);
            BalancedClickhouseDataSource dataSource = new BalancedClickhouseDataSource(url, props);
            ClickHouseConnection conn = dataSource.getConnection();
            log.info("链接数据库");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void executeQuery(String sql, Object... params) throws SQLException {//查询数据库，需要返回
        ClickHouseConnection conn = ClickHouseUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            if (null != params) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            ResultSet rs = ps.executeQuery();//获得结果集
            log.info("处理完成: " + rs.getFetchSize());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
    }

    public static Integer countTable(String tableName) {//返回表中有多少条记录
        String sql = "select count(1) from " + tableName;
        return executeCount(sql);
    }


    public static Integer executeCount(String sql) {//返回表中有多少条记录
        Integer count = 0;
        ClickHouseConnection conn = ClickHouseUtil.getConnection();
        try {
            ClickHouseStatement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            count = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("统计 ClickHouse 记录数: " + sql + " 该表记录数为: " + count);
        return count;
    }


    //关闭链接操作
    public static void close(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
                log.info("关闭数据库链接");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭Statement-->发送sql语句
    public static void close(Statement stat) {
        if (null != stat) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭ResultSet
    public static void close(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
