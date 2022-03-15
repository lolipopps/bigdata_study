package com.bigdata.flink.sink.mysql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


public class MysqlJoinSql {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        EnvironmentSettings envSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, envSettings);
        String orderTableDDL = "CREATE TABLE orders (\n" +
                "  order_id STRING,\n" +
                "  item    STRING,\n" +
                "  currency STRING,\n" +
                "  amount DOUBLE,\n" +
                "  order_time TIMESTAMP(3),\n" +
                "  proc_time as PROCTIME(),\n" +
                "  amount_kg as amount * 1000\n" +
                ") WITH (\n" +
                "  'connector.type' = 'kafka',\n" +
                "  'connector.topic' = 'order_table',\n" +
                "  'connector.version'='universal',\n" +
                "  'connector.properties.zookeeper.connect' = '172.18.1.11:2181',\n" +
                "  'connector.properties.bootstrap.servers' = '172.18.1.11:9092',\n" +
                "  'connector.properties.group.id' = 'testGroup3',\n" +
                "  'connector.startup-mode' = 'latest-offset',\n" +
                "  'format.type' = 'json',\n" +
                "  'format.derive-schema' = 'true'\n" +
                ")\n";
        String currencyTableDDL = "CREATE TABLE orders_detail\n" +
                "(\n" +
                "    order_id   STRING,\n" +
                "    item       STRING,\n" +
                "    status   STRING,\n" +
                "    create_time TIMESTAMP(3)\n" +
                ") with ('connector.type' = 'kafka',\n" +
                "      'connector.version' = 'universal',\n" +
                "      'connector.properties.group.id' = 'g2.group1',\n" +
                "      'connector.properties.bootstrap.servers' = '172.18.1.11:9092',\n" +
                "      'connector.properties.zookeeper.connect' = '172.18.1.11:2181',\n" +
                "      'connector.topic' = 'orders_detail',\n" +
                "      'connector.startup-mode' = 'latest-offset',\n" +
                "      'format.type' = 'json',\n" +
                "      'format.derive-schema' = 'true'\n" +
                "      )\n";

        String sinkTableDDL = "CREATE TABLE join_order (\n" +
                "  order_id STRING,\n" +
                "  item STRING primary key,\n" +
                "  order_time TIMESTAMP(3),\n" +
                "  create_time TIMESTAMP(3),\n" +
                "  amount double,\n" +
                "  state STRING\n" +
                ")" +
                " WITH (\n" +
                "      'connector' = 'jdbc',\n" +
                "      'username' = 'root',\n" +
                "      'password' = 'hu1234tai',\n" +
                "      'driver' = 'com.mysql.jdbc.Driver',\n" +
                "      'url' = 'jdbc:mysql://172.18.1.12:3306/demo?useSSL=false&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8',\n" +
                "      'table-name' = 'join_order'\n" +
                ")";

        String querySQL = "insert into join_order" +
                " SELECT  t1.order_id \n" +
                "       ,t1.item \n" +
                "       ,t1.order_time \n" +
                "       ,t2.create_time \n" +
                "       ,t1.amount \n" +
                "       ,t2.status \n" +
                "FROM orders t1\n" +
                "LEFT JOIN orders_detail t2\n" +
                "ON t1.order_id = t2.order_id";

        tableEnvironment.sqlUpdate(orderTableDDL);
        tableEnvironment.sqlUpdate(currencyTableDDL);
        tableEnvironment.sqlUpdate(sinkTableDDL);
        tableEnvironment.sqlUpdate(querySQL);
        tableEnvironment.execute("tomysql");

    }

}
