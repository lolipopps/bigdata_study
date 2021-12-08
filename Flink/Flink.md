#### Flink 任务提交 硬件

standalone



Client 客户端

Client负责将任务提交到集群，与 JobManager 构建 Akka 连接，然后将任务提交到 JobManager，通过和 JobManager 之间进行交互获取任务执行状态。

JobManager

JobManager 负责整个 Flink 集群任务的调度以及资源的管理，从客户端中获取提交的应用，然后根据集群中 TaskManager 上 TaskSlot 的使用情况，为提交的应用分配相应的 TaskSlot 资源并命令 TaskManager 启动从客户端中获取的应用。
同时在任务执行的过程中，Flink JobManager 会触发 Checkpoint 操作，每个 TaskManager 节点 收到 Checkpoint 触发指令后，完成 Checkpoint 操作，所有的 Checkpoint 协调过程都是在 Fink JobManager 中完成。

TaskManager

TaskManager 相当于整个集群的 Slave 节点，负责具体的任务执行和对应任务在每个节点上的资源申请和管理。
 

 任务提交流程
```aidl
1. standalone
   1）用户提交任务，Client负责作业的编译和提交，产生jobGraph提交到集群中执行 rest接口提交给Master的Dispatcher。
   2）Dispatcher把JobManager进程启动，把应用交给JobManager。
   3) JobManager 向 ResourceManager申请资源（slots），ResouceManager会启动对应的TaskManager进程，
      TaskManager空闲的slots会向ResourceManager注册。
   4）ResourceManager会根据JobManager申请的资源数量，向TaskManager发出指令（这些slots由你提供给JobManager）。
   5）接着，TaskManager可以直接和JobManager通信了（它们之间会有心跳包的连接），TaskManager向JobManager提供slots，JobManager向TaskManager分配在slots中执行的任务。
   6）最后，在执行任务过程中，不同的TaskManager会有数据之间的交换。

2。 yarn

1. Flink任务提交后，Client向HDFS上传Flink的Jar包和配置，之后向Yarn ResourceManager提交任务，
2. ResourceManager分配Container资源并通知对应的NodeManager启动ApplicationMaster，
3. ApplicationMaster启动后加载Flink的Jar包和配置构建环境，然后启动JobManager，之后ApplicationMaster 向ResourceManager申请资源启动TaskManager，
4. ResourceManager分配Container资源后，由ApplicationMaster通知资源所在节点的NodeManager启动TaskManager，NodeManager加载Flink的Jar包和配置构建环境并启动TaskManager，
5. TaskManager启动后向JobManager发送心跳包，并等待JobManager向其分配任务。
 


```
     
 
#### FLink 任务执行
```aidl
1: 加载配置文件

```
生成 StreamGraph 每个operate 对应一个 节点

生成 StreamingJobGraph