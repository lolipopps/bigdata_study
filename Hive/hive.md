#### hive 架构

组件：  client  driver metastore execu

client  hive cli beeline dhbc

diver  parser 将sql转换为抽象语法树                     asttree -> queryblock
       compiler 将抽象语法树编译成逻辑执行计划             operatorTree
       optimizer  物理优化器                           合并reduce 减少shuffle
       executor 将逻辑执行计划转换成可执行的物理计划        生成DAG翻译成MR任务
       
metastore  mysql derby 等存储元数据信息

1.  客户端通过连接hiverserver2 建立会话
2.  提交sql语句 driver处理 driver 通过 parser解析器 进行语法解析，校验语法等
3.  通过compiler 编译生成 astree  同时语义分析器遍历 astree 同时结合hive元数据生成queryblock,
    进一步翻译成逻辑执行树
4.  optimizer会对逻辑执行计划进行优化（谓词下推 常量替换 列裁剪等）得到优化后的逻辑执行计划
5.  通过任务编译器生成物理执行计划 转化成每个 mr task 根据不同的引擎进行任务提交


#### HIVE 优化

1）  业务优化

1. 空值或无意义值

2. 单独处理倾斜key

3.不同数据类型

2） SQL 优化

1. sort by 代替 order by

配合distribute by一同使用。如果不加distribute by的话，map端数据就会随机分配到reducer。

2. group by代替distinct

3. 小表在前  多表join时key相同这种情况会将多个join合并为一个MR job来处理


参数优化

1. 并行度 设置 set hive.exec.parallel.thread.number=8;

2.  set mapred.job.reuse.jvm.num.tasks=10; 

3.  set mapred.job.reuse.jvm.num.tasks=10; 

4.  set hive.tez.container.size=10240; 容器内存大小

5.  set hive.map.aggr=true; map端聚合   hive.auto.convert.join map join

6.  hive.groupby.skewindata=true； group by 优化

7.  文件合并
```
## 是否合并Map输出文件, 默认值为true
SET hive.merge.mapfiles = true; 
## 是否合并Reduce端输出文件,默认值为false
SET hive.merge.mapredfiles = true;
## 合并文件的大小,默认值为256000000
SET hive.merge.size.per.task = 256000000;
## 当输出文件的平均大小小于128M时，启动一个独立的map-reduce任务进行文件merge
SET hive.merge.smallfiles.avgsize = 134217728;
## 默认false，是否对输出结果压缩
SET hive.exec.compress.output = true;
## 压缩格式设置
SET parquet.compression = snappy;
## 开启动态分区
SET hive.exec.dynamic.partition.mode = nonstrict;
SET hive.exec.dynamic.partition = true;
```
select * 
from a
left b
on a.id = b.id and b.pt = '2012103000000' 

8. 谓词下推

   1、对于Join(Inner Join) 会下推
   
   2、对于Left outer Join, 右侧的表写在on后面、左侧的表写在where后面，性能上有提高；
   
   3、对于Right outer Join, 左侧的表写在on后面、右侧的表写在where后面，性能上有提高；

   4、对于Full outer Join,条件写在on后面，还是where后面，性能上面没有区别；
   
   5、不确定因素不会走谓词下推


 