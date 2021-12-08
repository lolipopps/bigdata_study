#### Flink SQL 优化

SQL优化

即根据业务需求选择合适的SQL实现⽅式，包括但不限于聚合优化、数据热点优 化、TOPN优化、使⽤内置函数、⾼效去重、慎⽤正则函数等

参数调优

```aidl
# EXACTLY_ONCE语义。
blink.checkpoint.mode=EXACTLY_ONCE
# checkpoint间隔时间，单位毫秒。
blink.checkpoint.interval.ms=180000
blink.checkpoint.timeout.ms=600000
# 2.x使⽤niagara作为statebackend，以及设定state数据⽣命周期，单位毫秒。
state.backend.type=niagara
state.backend.niagara.ttl.ms=129600000
# 2.x开启5秒的microbatch。
blink.microBatch.allowLatencyMs=5000
# 整个Job允许的延迟。
blink.miniBatch.allowLatencyMs=5000
# 单个batch的size。
blink.miniBatch.size=20000
# local 优化，2.x默认已经开启，1.6.4需⼿动开启。
blink.localAgg.enabled=true
# 2.x开启PartialFina优化，解决COUNT DISTINCT热点。
blink.partialAgg.enabled=true
# union all优化。
blink.forbid.unionall.as.breakpoint.in.subsection.optimization=true
# object reuse优化，默认已开启。
#blink.object.reuse=true
# GC优化（SLS做源表不能设置该参数）。
blink.job.option=-yD heartbeat.timeout=180000 -yD env.java.opts='-
verbose:gc -XX:NewRatio=3 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -
XX:ParallelGCThreads=4'
# 时区设置。
```

1. 开启MicroBatch或MiniBatch（提升吞吐）

缓存⼀ 定的数据后再触发处理，以减少对State的访问，从而提升吞吐并减少数据的输出量。

```aidl
miniBatch）。
sql.exec.mini-batch.window.enabled=true
# 批量输出的间隔时间，使⽤microBatch策略时需要加上该配置，且建议和blink.
miniBatch.allowLatencyMs保持⼀致。
blink.microBatch.allowLatencyMs=5000
# 使⽤microBatch时需要保留以下两个miniBatch配置。
blink.miniBatch.allowLatencyMs=5000
# 防⽌OOM设置每个批次最多缓存数据的条数。
```

2. 开启LocalGlobal

LocalGlobal优化即将原先的Aggregate分成Local+Global两阶段聚合，也就是在 MapReduce模型中熟知的Combine+Reduce处理模式。

LocalGlobal适⽤于提升如SUM、COUNT、MAX、MIN和AVG等普通聚合的性能，以及 开启LocalGlobal需要UDAF实现Merge⽅法。

```aidl
set blink.localAgg.enabled=true
观察最终⽣成的拓扑图的节点名字中是否包含GlobalGroupAggregate或LocalGroupAggregate。
```

3. 开启PartialFinal（解决COUNT DISTINCT热点问题）

```aidl
set blink.partialAgg.enabled=true。
PartialFinal优化⽅法不能在包含UDAF的Flink SQL中使⽤。
■ 数据量不⼤的情况下不建议使⽤PartialFinal优化⽅法。PartialFinal优化会⾃动打散
成两层聚合，引⼊额外的⽹络Shuffle，在数据量不⼤的情况下，可能反而会浪费资源。

观察最终⽣成的拓扑图的节点名中是否包含Expand节点，或者原来⼀层的聚合变成了两层
的聚合。
```

4 聚合优化

```aidl
COUNT(distinct visitor_id) as UV1 , COUNT(distinct case when
is_wireless='y' then visitor_id else null end) as UV2
```

AGG WITH FILTER语法来代替CASE WHEN实现多维度统计的功能。实 时计算⽬前的SQL优化器能分析出Filter参数，从而同⼀个字段上计算不同条件下的COUNT
DISTINCT能共享State，减少对State的读写操作。性能测试中，使⽤AGG WITH FILTER语 法来代替CASE WHEN能够使性能提⾼1倍。

5. TopN优化

当TopN的输⼊是⾮更新流（例如Source），TopN只有⼀种算法AppendRank。当TopN的 输⼊是更新流时（例如经过了AGG/JOIN计算），TopN有3种算法，性能从⾼到低分别是： UpdateFastRank 、
UnaryUpdateRank和RetractRank。算法名字会显⽰在拓扑图的节 点名字上。

```aidl
UpdateFastRank

1. 输⼊流有主键信息
2.  排序字段的更新是单调的，且单调⽅向与排序⽅向相反。
例如，ORDER BY COUNT/ COUNT_DISTINCT/SUM（正数）DESC
insert into print_test
select  cate_id
       ,seller_id
       ,stat_date
       ,pay_ord_amt --不输出rownum字段，能减小结果表的输出量。
from
(
	select  * --注意要有时间字段，否则state过期会导致数据错乱。
	       ,ROW_NUMBER () OVER ( PARTITIon BY cate_id,stat_date order by pay_ord_amt DESC ) as rownum --根据上游sum结果排序。
	from
	(
		select  cate_id
		       ,seller_id
		       ,stat_date --重点。声明Sum的参数都是正数，所以Sum的结果是单调递增的，因此TopN能使⽤优化算法，只获取前100个数据。。
		       ,sum (total_fee) filter (where total_fee >= 0 ) as pay_ord_amt
		from random_test
		where total_fee >= 0
		group by  cate_id
		         ,seller_id
		         ,stat_date
	) a
	where rownum <= 100 
);

- UnaryUpdateRank：仅次于UpdateFastRank的算法。需要具备1个条件：输⼊流存在
PK信息。例如，ORDER BY AVG。 

- RetractRank：普通算法，性能最差，不建议在⽣产环境使⽤该算法。请检查输⼊流是否存
在PK信息，如果存在可进⾏UnaryUpdateRank或UpdateFastRank优化。
```

实时去重 rownumber

#### 窗口

```aidl
CREATE VIEW one_minute_window_output as
SELECT TUMBLE_ROWTIME(ts, INTERVAL '1' MINUTE) as rowtime, 
    username, 
    COUNT(click_url) as cnt
FROM user_clicks
GROUP BY TUMBLE(ts, INTERVAL '1' MINUTE)
, username;


SELECT TUMBLE_START(rowtime, INTERVAL '1' HOUR),
       TUMBLE_END(rowtime, INTERVAL '1' HOUR),
       username,
       SUM(cnt)
FROM one_minute_window_output
GROUP BY TUMBLE(rowtime, INTERVAL '1' HOUR), 
username


INSERT INTO hop_output
SELECT
    HOP_START (ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE),
    HOP_END (ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE),
    username,
    COUNT (click_url)
FROM user_clicks
GROUP BY
HOP (ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE),
username


INSERT INTO session_output
SELECT
    SESSION_START(ts, INTERVAL '30' SECOND),
    SESSION_END(ts, INTERVAL '30' SECOND),
    username,
    COUNT(click_url)
FROM user_clicks
GROUP BY SESSION(ts, INTERVAL '30' SECOND), username
```

OVER windows
```aidl
⽤OVER Window的流式数据中，每1个元素都对应1个OVER Window。每1个元素都触发⼀次数据计算。
窗口元素是与当前元素相邻的元素集合，流上元素分布在
多个窗口中。在Flink SQL Window的实现中，每个触发计算的元素所确定的⾏，都是该元素所
在窗口的最后⼀⾏。

ROWS OVER Window：每⼀⾏元素都视为新的计算⾏，即每⼀⾏都是⼀个新的窗口。
RANGE OVER Window：具有相同时间值的所有元素⾏视为同⼀计算⾏，即具有相同时间值
的所有⾏都是同⼀个窗口。

SELECT
agg1(col1) OVER (definition1) AS colName,
 ...
aggN(colN) OVER (definition1) AS colNameN
FROM Tab1;


SELECT
agg1(col1) OVER(
 [PARTITION BY (value_expression1,..., value_expressionN)] 
ORDER BY timeCol
ROWS
BETWEEN (UNBOUNDED | rowCount) PRECEDING AND CURRENT ROW) AS
colName, ... 
FROM Tab1

```