#### hdfs 读写过程
读取

1: 客户端首先向namenode发起读数据请求。
2: namenode接受请求，校验用户权限和文件情况，找到文件block所在datanode
3: client 连接datanode 打开文件通道读取对应的文件
4:  读取完一个block 读取下一个。

写

1: 用户发起文件上传请求，client 对用户的文件进行拆分block
2: client 向namenode 发送写文件请求
3: namenode 检测用户权限以及文件状态通过后为 文件存储分配datanode
4: client 与datanode连接以文件包的形式写数据 a-b-c 拷贝的方式
5: 块写完后 异步向namenode 报告块信息 完成一个块的写入


#### hdfs 体系

namenode 元数据管理 ，客户端请求，datanode 管理  数据块管理

datanode 数据存储，读写数据块

secondary namenode 元数据备份 合并辅助 namenode

#### MR 过程

MRAppJob

数据 inputformat 分片 record 读取 之后的数据会进入 

map 函数 映射键值对  collect函数 partition 函数 combine 函数 map阶段的产出会进行 分区 排序（环形内存排序） 溢写 合并 生成一个带有索引的文件

reduce 根据 reduce id copy 拷贝map的产出 对其进行merge  排序sort 然后进行 reduce操作


shuffle  阶段分为四个步骤：依次为：collect函数partition，排序溢写spill， 排序合并merge， 
         merge（两个线程对内存到）  sort
         
#### yarn 

ResourceManager： RM 是一个全局的资源管理器，负责整个系统的资源管理和分配
                   两部分: 调度器（Scheduler）和应用程序管理器（Application Manager）。
                   
nodeManager： NodeManager 是每个节点上的资源和任务管理器，管理自身的 container

container： Container 是 YARN 中的资源抽象，封装了各种资源。一个应用程序会分配一个
           Container，这个应用程序只能使用这个 Container 中描述的资源
           
ApplicationMaster： 用户提交的一个应用程序会对应于一个 ApplicationMaster，它的主要功能有：
                   a.与 RM 调度器协商以获得资源，资源以 Container 表示。
                   b.将得到的任务进一步分配给内部的任务。
                   c.与 NM 通信以启动/停止任务。
                   d.监控所有的内部任务状态，并在任务运行失败的时候重新为任务申请资源以重启任务。

#### yarn 的任务提交

1) 用户向 YARN 提交一个应用程序，rm  与 NM 通信为其分配一个 container 启动 ApplicationMaster

2） ApplicationMaster 与 rm 拆分内部任务，申请资源，并监控 任务的状态

3） AM 采用轮询的方式向 RM 申请和领取资源。 RM 为 AM 分配资源，以 Container 形式返回

4) AM 申请到资源后，便与之对应的 NM 通讯，要求 NM 启动任务。 NodeManager 为任务设置好运行环境，启动任务

5) 各个任务向 AM 汇报自己的状态和进度，以便当任务失败时可以重启任务。

·6) 应用程序完成后，ApplicationMaster 向 ResourceManager 注销并关闭