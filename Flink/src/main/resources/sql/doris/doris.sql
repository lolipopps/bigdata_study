-- 聚合模型  不需要原始的明细数据，只关注汇总的结果；业务涉及的查询为汇总类查询，比如sum、min、max、count等类型的查询；查询维度固定。
CREATE TABLE table01
(
    user_id      INT COMMENT "id of user",
    device_code  INT COMMENT "code of device",
    device_price DECIMAL(10, 2) COMMENT "",
    event_time   DATETIME NOT NULL COMMENT "datetime of event",
    total        DECIMAL(18, 2) SUM DEFAULT "0" COMMENT "total amount of equipment",
    index index01 (user_id) USING BITMAP COMMENT "bitmap index" -- 创建索引，user_id列创建了名为index01的bitmap索引。当前这种写法仅支持BITMAP索引，
    --  创建语法为：INDEX index_name (col_name1[, col_name2, ...]) [USING BITMAP] COMMENT 'xxxxxx'
) AGGREGATE KEY(user_id, device_code,device_price,event_time) -- 聚合模型显式的定义排序键，在为聚合模型定义排序键时，需要把所有的维度列按照建表的顺序都写上
DISTRIBUTED BY HASH(user_id,device_code) BUCKETS 20
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="event_time"
);

PARTITION BY RANGE(event_time)  -- 为table01指定分区和分桶，我们按照event_time根据日期分为三个区，然后按照user_id和device_code将每个分区数据根据hash分散到20个桶中
(
PARTITION p1 VALUES LESS THAN ('2021-01-31'),
PARTITION p2 VALUES LESS THAN ('2021-02-28'),
PARTITION p3 VALUES LESS THAN ('2021-03-31')
)
DISTRIBUTED BY HASH(user_id,device_code) BUCKETS 20
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="event_time"
);

show create table table01;

desc table01;

insert into table01
values (1001, 27, 9.99, '2021-03-11', 9.99);
insert into table01
values (1001, 27, 9.99, '2021-03-11', 10.99);
insert into table01
values (1002, 27, 9.99, '2021-03-11', 9.99);
insert into table01
values (1003, 28, 15.49, '2021-03-12', 15.49);

select *
from table01;

-- 明细模型 保留原始数据的业务 查询维度不固定的业务； 数据产生后就不会发生太多变化的业务。
CREATE TABLE IF NOT EXISTS demo.table02
(                             -- 在指定排序键的时候列的顺序要和建表语句中的相同，否则建表语句会报错。
    event_time DATETIME NOT NULL COMMENT "datetime of event",
    event_type INT      NOT NULL COMMENT "type of event",
    user_id    INT COMMENT "id of user",
    channel    INT COMMENT "" -- 若在数据列中我们未定义聚合函数（例如2.1章table01的total列），则默认采用明细模型；
) DUPLICATE KEY(event_time, event_type,user_id)  -- 明细模型可以使用DUPLICATE KEY(列1,列2……)显式的说明使用明细模型，也可以整个省略；默认为表选择前三列作为排序键。
DISTRIBUTED BY HASH(user_id) BUCKETS 10
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="event_time"
);

insert into table02
values ('2021-03-11', 27, 1001, 01);
insert into table02
values ('2021-03-11', 27, 1001, 01);
insert into table02
values ('2021-03-12', 31, 1002, 02);

select *
from table02
ORDER BY user_id desc, event_type DESC;

-- 更新模型 明细模型会将所有写入的数据保留，聚合模型是对写入的数据进行聚合处理，而更新模型的特点是只保留相同主键下最新导入的数据。在更新模型中，排序键构成表的唯一性约束，成为我们常说的“主键”。

CREATE TABLE IF NOT EXISTS demo.table03
(
    create_time DATE   NOT NULL COMMENT "create time of an order",
    order_id    BIGINT NOT NULL COMMENT "id of an order",
    order_state INT COMMENT "state of an order",
    total_price BIGINT COMMENT "price of an order"
) UNIQUE KEY(create_time, order_id)  -- UNIQUE KEY(create_time, order_id)不能省略，且其中的排序键“create_time+order_id”构成了唯一主键，主键相同的数据，StarRocks只会保留最后导入的那一条。
DISTRIBUTED BY HASH(order_id) BUCKETS 8
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="create_time"
);

insert into table03
values ('2021-03-11', 20210311001, 01, 10);
insert into table03
values ('2021-03-11', 20210311001, 02, 200);
insert into table03
values ('2021-03-12', 20210312001, 01, 1000);
select *
from table03;

-- 主键模型 明细模型、聚合模型和更新模型都不支持update语法，为实现update功能，StarRocks又自研了主键模型：primary key。
create table demo.table04
(
    dt          date    NOT NULL,
    order_id    bigint  NOT NULL,
    user_id     int     NOT NULL,
    merchant_id int     NOT NULL,
    good_id     int     NOT NULL,
    good_name string NOT NULL,
    price       int     NOT NULL,
    cnt         int     NOT NULL,
    revenue     int     NOT NULL,
    state       tinyint NOT NULL

) PRIMARY KEY (dt, order_id)
PARTITION BY RANGE(`dt`) (
    PARTITION p20210929 VALUES [('2021-09-29'), ('2021-09-30')),
    PARTITION p20210930 VALUES [('2021-09-30'), ('2021-10-01'))
) DISTRIBUTED BY HASH(order_id) BUCKETS 4

PROPERTIES("replication_num" = "1");


CREATE TABLE IF NOT EXISTS demo.orders
( -- 在指定排序键的时候列的顺序要和建表语句中的相同，否则建表语句会报错。
    order_id STRING NOT NULL COMMENT "datetime of event",
    item STRING NOT NULL COMMENT "type of event",
    order_time DATETIME NOT NULL COMMENT "type of event",
    amount     DOUBLE COMMENT "",
    currency STRING NOT NULL COMMENT "datetime of event",
    proc_time  DATETIME COMMENT "id of user"
    -- 若在数据列中我们未定义聚合函数（例如2.1章table01的total列），则默认采用明细模型；
) DUPLICATE KEY(order_id,item, order_time)  -- 明细模型可以使用DUPLICATE KEY(列1,列2……)显式的说明使用明细模型，也可以整个省略；默认为表选择前三列作为排序键。
DISTRIBUTED BY HASH(order_id) BUCKETS 10
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="order_time"
);

CREATE TABLE IF NOT EXISTS demo.orders_agg
( -- 在指定排序键的时候列的顺序要和建表语句中的相同，否则建表语句会报错。
    order_id STRING NOT NULL COMMENT "datetime of event",
    item STRING NOT NULL COMMENT "type of event",
    order_time DATETIME MAX DEFAULT "NULL" COMMENT "MAX of Time",
    amount    DOUBLE  SUM DEFAULT "0" COMMENT "total amount",
    currency INT REPLACE DEFAULT "0" COMMENT "currency replace",
    proc_time  DATETIME MAX DEFAULT "NULL" COMMENT "MAX of proc_time"
    -- 若在数据列中我们未定义聚合函数（例如2.1章table01的total列），则默认采用明细模型；
) AGGREGATE KEY(order_id, item)   -- 明细模型可以使用DUPLICATE KEY(列1,列2……)显式的说明使用明细模型，也可以整个省略；默认为表选择前三列作为排序键。
DISTRIBUTED BY HASH(order_id) BUCKETS 10
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="order_id"
);


CREATE TABLE IF NOT EXISTS demo.orders_update
(
    order_id STRING NOT NULL COMMENT "datetime of event",
    item STRING NOT NULL COMMENT "type of event",
    order_time DATETIME NOT NULL COMMENT "type of event",
    amount     DOUBLE COMMENT "",
    currency STRING NOT NULL COMMENT "datetime of event",
    proc_time  DATETIME COMMENT "id of user"
) UNIQUE KEY(order_id, item)  -- UNIQUE KEY(create_time, order_id)不能省略，且其中的排序键“create_time+order_id”构成了唯一主键，主键相同的数据，StarRocks只会保留最后导入的那一条。
DISTRIBUTED BY HASH(order_id) BUCKETS 8
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="order_id"
);



CREATE TABLE table01
(
    user_id      INT COMMENT "id of user",
    device_code  INT COMMENT "code of device",
    device_price DECIMAL(10, 2) COMMENT "",
    event_time   DATETIME NOT NULL COMMENT "datetime of event",
    total        DECIMAL(18, 2) SUM DEFAULT "0" COMMENT "total amount of equipment",
    index index01 (user_id) USING BITMAP COMMENT "bitmap index" -- 创建索引，user_id列创建了名为index01的bitmap索引。当前这种写法仅支持BITMAP索引，
    --  创建语法为：INDEX index_name (col_name1[, col_name2, ...]) [USING BITMAP] COMMENT 'xxxxxx'
) AGGREGATE KEY(user_id, device_code,device_price,event_time) -- 聚合模型显式的定义排序键，在为聚合模型定义排序键时，需要把所有的维度列按照建表的顺序都写上



CREATE TABLE IF NOT EXISTS demo.orders_detail
( -- 在指定排序键的时候列的顺序要和建表语句中的相同，否则建表语句会报错。
    order_id STRING NOT NULL COMMENT "datetime of event",
    item STRING NOT NULL COMMENT "type of event",
    create_time DATETIME COMMENT "id of user",
    status STRING NOT NULL COMMENT "type of event"

    -- 若在数据列中我们未定义聚合函数（例如2.1章table01的total列），则默认采用明细模型；
) DUPLICATE KEY(order_id,item, create_time)  -- 明细模型可以使用DUPLICATE KEY(列1,列2……)显式的说明使用明细模型，也可以整个省略；默认为表选择前三列作为排序键。
DISTRIBUTED BY HASH(order_id) BUCKETS 10
PROPERTIES ( -- 指定表的一些其他属性，比如副本数、存储介质、动态分区、bloom_filter索引等等。
"replication_num" = "1",
"bloom_filter_columns"="create_time"
);