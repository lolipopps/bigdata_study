kafka 监听mysqk

```

[mysqld]
log-bin=mysql-bin # 添加这一行就ok，开启 Binlog 写入功能 
binlog-format=ROW # 选择row模式 
server_id=1 # 不能和canal的slaveId重复 



https://www.jianshu.com/p/b05bbd3acd67

./bin/kafka-run-class.sh  kafka.admin.TopicCommand --create --zookeeper 172.18.1.11:2181 --replication-factor 1 --partitions 1 --topic mysql-kafka


.新建source/sink配置文件，并放置在kafka config目录下

vim quickstart-mysql.properties

name=mysql-b-source-comments
connector.class=io.confluent.connect.jdbc.JdbcSourceConnector
tasks.max=1
connection.url=jdbc:mysql://127.0.0.1:3306/android_service?user=xxx&password=xxxx
# timestamp+incrementing 时间戳自增混合模式
mode=timestamp+incrementing
# 时间戳 commenttime
timestamp.column.name=commenttime
# 自增字段  id
incrementing.column.name=id
# 白名单表  comments
table.whitelist=comments
# topic前缀   mysql-kafka-
topic.prefix=mysql-kafka-

vim quickstart-mysql-sink.properties

name=mysql-b-sink-comments
connector.class=io.confluent.connect.jdbc.JdbcSinkConnector
tasks.max=1
#kafka的topic名称
topics=mysql-kafka-comments
# 配置JDBC链接
connection.url=jdbc:mysql://127.0.0.1:3306/android_service?user=xxx&password=xxxx
# 不自动创建表，如果为true，会自动创建表，表名为topic名称
auto.create=false
# upsert model更新和插入
insert.mode=upsert
# 下面两个参数配置了以pid为主键更新
pk.mode = record_value
pk.fields = id
#表名为kafkatable
table.name.format=kafkacomments

启动kafka connect

./bin/connect-standalone.sh  ./config/connect-standalone.properties  ./config/quickstart-mysql.properties  ./config/quickstart-mysql-sink.properties

```

