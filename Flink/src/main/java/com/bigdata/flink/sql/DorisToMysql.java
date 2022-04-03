package com.bigdata.flink.sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class DorisToMysql {


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        EnvironmentSettings envSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, envSettings);
        String dorisSource = "CREATE TABLE doris_table (\n" +
                "  user_id int,\n" +
                "  device_code    int,\n" +
                "  device_price DECIMAL,\n" +
                "  event_time TIMESTAMP(3),\n" +
                "  total DECIMAL \n" +
                ") WITH (\n" +
                " 'connector'='starrocks',\n" +
                "   'scan-url'='172.18.1.60:8030',\n" +
                "   'jdbc-url'='jdbc:mysql://172.18.1.60:9030',\n" +
                "   'database-name'='demo',\n" +
                "   'table-name'='table01'," +
                "   'username' = 'root',\n" +
                "   'password' = 'hu1234tai'\n" +
                " )\n";

        String mysqlSink = "CREATE TABLE mysql_table (\n" +
                "  user_id int,\n" +
                "  device_code    int,\n" +
                "  device_price DECIMAL,\n" +
                "  event_time TIMESTAMP(3),\n" +
                "  total DECIMAL \n" +
                " ) WITH (\n" +
                "      'connector' = 'jdbc',\n" +
                "      'username' = 'root',\n" +
                "      'password' = 'hu1234tai',\n" +
                "      'driver' = 'com.mysql.jdbc.Driver',\n" +
                "      'url' = 'jdbc:mysql://172.18.1.12:3306/demo?useSSL=false&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8',\n" +
                "      'table-name' = 'table01'\n" +
                ")";


        String querySQL = "insert into mysql_table " +
                "       SELECT  t1.user_id \n" +
                "       ,t1.device_code \n" +
                "       ,t1.device_price \n" +
                "       ,t1.event_time \n" +
                "       ,t1.total \n" +
                "FROM doris_table t1\n";
        tableEnvironment.sqlUpdate(dorisSource);
        tableEnvironment.sqlUpdate(mysqlSink);
        tableEnvironment.executeSql(querySQL).print();
    }
}

