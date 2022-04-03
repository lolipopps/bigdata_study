package com.bigdata.flink.sink.dorisdb;

import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


public class DorisSql {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true);
        //自定义端口
        configuration.setInteger(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        env.setParallelism(1);
        EnvironmentSettings envSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env, envSettings);


        String orderTableDDL = "CREATE TABLE doris_kafka_orders (\n" +
                "  order_id STRING,\n" +
                "  item    STRING,\n" +
                "  currency STRING,\n" +
                "  amount DOUBLE,\n" +
                "  order_time TIMESTAMP(3),\n" +
                "  proc_time as PROCTIME()\n" +
                ") WITH (\n" +
                "  'connector.type' = 'kafka',\n" +
                "  'connector.topic' = 'order_table',\n" +
                "  'connector.version'='universal',\n" +
                "  'connector.properties.zookeeper.connect' = '172.18.1.11:2181',\n" +
                "  'connector.properties.bootstrap.servers' = '172.18.1.11:9092',\n" +
                "  'connector.properties.group.id' = 'DorisSql',\n" +
                "  'connector.startup-mode' = 'latest-offset',\n" +
                "  'format.type' = 'json',\n" +
                "  'format.derive-schema' = 'true'\n" +
                ")\n";


        String orderSinkTableDDL = "CREATE TABLE doris_orders (\n" +
                "  order_id STRING,\n" +
                "  item    STRING,\n" +
                "  order_time TIMESTAMP(3),\n" +
                "  amount DOUBLE,\n" +
                "  currency STRING,\n" +
                "  proc_time TIMESTAMP(3)\n" +
                ") WITH (\n" +
                " 'connector'='starrocks',\n" +
                "   'load-url'='172.18.1.60:8030',\n" +
                "   'jdbc-url'='jdbc:mysql://172.18.1.60:9030',\n" +
                "   'database-name'='demo',\n" +
                "   'table-name'='orders_update'," +
                "   'username' = 'root',\n" +
                "   'sink.buffer-flush.interval-ms' = '5000'," +
                "   'password' = 'hu1234tai'\n" +
                " )\n";
        String querySQL = "insert into doris_orders " +
                "SELECT  order_id,item,order_time,amount,currency,proc_time \n" +
                "FROM doris_kafka_orders";
        tableEnvironment.sqlUpdate(orderTableDDL);
        tableEnvironment.sqlUpdate(orderSinkTableDDL);
        tableEnvironment.executeSql(querySQL);

    }

}
