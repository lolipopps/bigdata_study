package com.bigdata.util;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

@Slf4j
public class MysqlUtil {
    private static String driver = "";
    private static String url = "";
    private static String username = "";
    private static String password = "";

    static {//静态代码块注册驱动
        try {
            Properties properties = new Properties();
            log.info("加载配置文件,初始化mysql数据库链接");
            properties.load(MysqlUtil.class.getResourceAsStream("/db.properties"));
            driver = properties.getProperty("mysql.driver");
            url = properties.getProperty("mysql.url");
            username = properties.getProperty("mysql.username");
            password = properties.getProperty("mysql.password");
            Class.forName(driver);//加载驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回连接
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            log.info("链接数据库");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //使用动态参数，更新数据-->插入记录，更新记录，删除记录（不需要返回）
    /*
     * String sql="insert into t_book (bookname,author,publisher,create_date) values(?,?,?,?)";
     *JDBCutil.executeUpdate(sql,book.getBookname(),book.getAuthor(),book.getPublisher(),book.getCreate_date());
     */
    public static void executeUpdate(String sql, Object... params) {//
        Connection conn = MysqlUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            if (null != params) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            ps.executeUpdate();
        } catch (SQLException e) {
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
        Connection conn = MysqlUtil.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("统计mysql记录数: " + sql + " 该表记录数为: " + count);
        return count;
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> executeQuery(String sql, T t, Object... params) {//查询数据库，需要返回
        Class<? extends Object> _class = t.getClass();
        Field[] fields = _class.getDeclaredFields();

        Connection conn = MysqlUtil.getConnection();
        PreparedStatement ps = null;

        ArrayList<T> list = new ArrayList<T>();
        try {
            ps = conn.prepareStatement(sql);
            if (null != params) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            ResultSet rs = ps.executeQuery();//获得结果集
            T returnobj = (T) _class.newInstance();
            while (rs.next()) {
                for (Field field : fields) {
                    String fieldName = field.getName();//获得属性的名字
                    Object obj = rs.getObject(fieldName);//通过名字获得结果集中的数据
                    String upName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = _class.getDeclaredMethod("set" + upName, field.getType());//获得方法
                    method.invoke(returnobj, obj);//设置值
                }
                list.add(returnobj);
            }
            if (list.size() == 0) {
                return null;
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ps);
            close(conn);
        }
        return null;
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
