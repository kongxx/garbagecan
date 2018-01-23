# HBase入门

HBase是建立在Hadoop文件系统之上的分布式面向列的数据库，它是横向扩展的。它利用了Hadoop的文件系统（HDFS）提供的容错能力。

HBase提供对数据的随机实时读/写访问，可以直接HBase存储HDFS数据。

## 准备
- 安装JDK1.8+
- 下载 hbase-2.0.0-beta-1-bin.tar.gz 包，并解压到 /apps/目录下。
- 修改 conf/hbase-env.sh 文件，设置 JAVA_HOME 变量
``` shell
export JAVA_HOME=/opt/jdk1.8.0_112
```

## 单机模式

单机运行模式提供了一种最简单运行方式来方便开发人员在单机模式下开发调试。使用起来也非常简单。

### 修改 conf/hbase-site.xml 文件
- 使用 hbase.rootdir 参数来设置 hbase 保存数据的路径。这里使用 “file://” 表明是使用的本地目录。也可以使用 Hadoop 的 “hdfs://”来使用分布式文件系统。
- 设置 zookeeper 数据保存路径。
  完整文件内容如下：

``` xml
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>file:///apps/hbase-2.0.0-beta-1/data/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/apps/hbase-2.0.0-beta-1/data/zookeeper</value>
  </property>
</configuration>
```

### 运行 hbase 

使用下面的命令启动 hbase 集群

``` shell
$ bin/start-hbase.sh
```

启动完成后，可以访问下面的地址来查看集群的详细信息。
> http://192.168.0.192:16010/

### 测试

HBase是一个面向列的数据库，在表中它由行组成。表模式只定义列族，也就是键值对。一个表有多个列族，每一个列族可以有任意数量的列。后续列的值连续地存储在磁盘上。表中的每个单元格值都具有时间戳。

- 表是行的集合。
- 行是列族的集合。
- 列族是列的集合。
- 列是键值对的集合。

集群启动后，我们可以使用hbase自带的shell来做一些数据库操作，如下：

``` shell
# 启动shell
$ bin/hbase shell

# 创建 user 表，其中包括两个列族 base 和 address。
# base列族用来保存用户基本信息，username和password
# address列族用来保存家庭和办公地址 
> create 'user', 'base', 'address'
Created table user
Took 2.9153 seconds 

# 查看 user 表
> list 'user'
TABLE
user
1 row(s)
Took 0.1178 seconds 

# 向 user 表写入数据
> put 'user', 'row1', 'base:username', 'user1'
> put 'user', 'row1', 'base:password', 'user1'
> put 'user', 'row1', 'address:home', 'user1 home'
> put 'user', 'row1', 'address:office', 'user1 office'

> put 'user', 'row2', 'base:username', 'user2'
> put 'user', 'row2', 'base:password', 'user2'
> put 'user', 'row2', 'address:home', 'user2 home'
> put 'user', 'row2', 'address:office', 'user2 office'

# 查询 user 表中的数据
> scan 'user'
ROW                                        COLUMN+CELL
 row1                                      column=address:home, timestamp=1516586881667, value=user1 home
 row1                                      column=address:office, timestamp=1516586887852, value=user1 office
 row1                                      column=base:password, timestamp=1516586875823, value=user1
 row1                                      column=base:username, timestamp=1516586869321, value=user1
 row2                                      column=address:home, timestamp=1516586916024, value=user2 home
 row2                                      column=address:office, timestamp=1516586922659, value=user2 office
 row2                                      column=base:password, timestamp=1516586946567, value=user2
 row2                                      column=base:username, timestamp=1516586910234, value=user2

# 查询 user 表的一行数据
> get 'user', 'row1'
get 'user', 'row1'
COLUMN                                     CELL
 address:home                              timestamp=1516586881667, value=user1 home
 address:office                            timestamp=1516586887852, value=user1 office
 base:password                             timestamp=1516586875823, value=user1
 base:username                             timestamp=1516586869321, value=user1

# 删除 user 表的一行数据
> delete 'user', 'row2'

# 删除 user 表。需要先disable user表，然后才能删除。
> disable 'user'
> drop 'user'

```

### 停止 hbase 集群

``` shell
$ bin/stop-hbase.sh
```

## 伪分布式集群模式

这里为了简化没有使用Hadoop的HDFS，而是仍然使用本地存储。

### 修改 conf/hbase-site.xml 设置 hbase 为分布式集群模式

修改后的 conf/hbase-site.xml 文件内容如下：

``` xml
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>file:///apps/hbase-2.0.0-beta-1/data/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/apps/hbase-2.0.0-beta-1/data/zookeeper</value>
  </property>
  <property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
  </property>
</configuration>
```

### 启动 HBase 伪分布式集群的主 master
``` shell
$ bin/start-hbase.sh
```

### 启动 HBase 伪分布式集群的备份 master

这里使用 local-master-backup.sh 命令来启动备份master，其中
- 主master使用 16010，16020和16030三个端口
- 第二个备份 master使用16012, 16022和16032三个端口
- 第三个个备份 master使用16013, 16023和16033三个端口

``` shell
$ bin/local-master-backup.sh start 2
$ bin/local-master-backup.sh start 3
```

### 启动 HBase 伪分布式集群的区域服务器

``` shell
$ bin/local-regionservers.sh start 2
$ bin/local-regionservers.sh start 3
```

### 测试

可以继续使用单机模式的例子来验证集群的正确性。

### 停止集群

``` shell
$ bin/local-regionservers.sh stop 2
$ bin/local-regionservers.sh stop 3

$ bin/local-master-backup.sh stop 2
$ bin/local-master-backup.sh stop 3

$ bin/stop-hbase.sh
```

## 分布式集群模式

这里准备了三台服务器bd1，bd2和bd3来演示 HBase 的分布式集群模式。其中：
- bd1 作为主master
- bd2 作为备份master
- bd2,bd3 作为区域服务器

### 配置 SSH 免密码登陆

首先在其中一台机器上使用 ssh-keygen 工具来创建密钥，如下

``` shell
ssh-keygen -t rsa
```

使用 ssh-copy-id 命令自动在目标服务器上生成~/.ssh/authorized_keys文件

``` shell
ssh-copy-id -i ~/.ssh/id_rsa.pub <目标机器>
```

### 配置 conf/hbase-site.xml

``` xml
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>file:///apps/hbase-2.0.0-beta-1/data/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/apps/hbase-2.0.0-beta-1/data/zookeeper</value>
  </property>
  <property>
    <name>hbase.zookeeper.quorum</name>
    <value>bd1,bd2,bd3</value>
  </property>
  <property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
  </property>
</configuration>
```

### 创建 conf/backup-masters

定义备份master节点

``` shell
bd2
```

### 配置 conf/regionservers

定义区域服务器节点

``` shell
bd2
bd3
```

### 启动集群

``` shell
$ bin/start-hbase.sh
```

### 测试

可以继续使用单机模式的例子来验证集群的正确性。

### 停止集群

``` shell
$ bin/stop-hbase.sh
```
