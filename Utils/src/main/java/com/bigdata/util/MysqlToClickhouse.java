package com.bigdata.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlToClickhouse {

    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String dbURL = "jdbc:mysql://*********:3306/xjx1?characterEncoding=UTF-8";
    private static String userName = "test";
    private static String userPwd = "**********";
    private static String mysqlHost = "**********";
    private static String mysqlPort = "3306";
    private static String dbName = "3306";
    private static String tableName = "3306";

    public static Connection getMysqlConnection() {
        try {
            Class.forName(driverName);
            Connection dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
            return dbConn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void close(PreparedStatement pps, Connection con) {
        if (pps != null) {
            try {
                pps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
