# 使用Sqoop从Postgresql中导入数据到HBase中

接前面的文章 “使用Sqoop从Postgresql中导入数据到Hive中”，今天看看怎样从 Postgresql 入数据到 HBase 中。

这里有一点需要注意的是 Sqoop 1.4.7 目前不支持 HBase 2.x，所以准备了一个 hbase 1.4.9 的环境来做测试。

## 配置

进入 sqoop 的 conf 目录，修改 sqoop-env.sh 文件，如下：

``` shell
#Set path to where bin/hadoop is available
export HADOOP_COMMON_HOME=/apps/hadoop-2.7.7

#Set path to where hadoop-*-core.jar is available
export HADOOP_MAPRED_HOME=/apps/hadoop-2.7.7

#set the path to where bin/hbase is available
export HBASE_HOME=/apps/hbase-1.4.9

#Set the path to where bin/hive is available
export HIVE_HOME=/apps/apache-hive-2.3.2-bin

#Set the path for where zookeper config dir is
export ZOOCFGDIR=/apps/zookeeper-3.4.10/conf
```

## 从 postgresql 向 HBase 导入数据

使用项目的命令来向 HBase 导入数据

``` shell
$ bin/sqoop import --connect jdbc:postgresql://localhost:5432/test --username test --password test --table users --hbase-table user --column-family base --hbase-row-key id --hbase-create-table --m 1
```

导入数据后，登录到 hbase 中查看一下结果
``` shell
$ bin/hbase shell

hbase(main):001:0> list
TABLE
user
1 row(s) in 0.0330 seconds

=> Hbase::Table - user
hbase(main):002:0> scan 'user'
ROW                              COLUMN+CELL
 1                               column=base:name, timestamp=1547609241178, value=user1
 1                               column=base:password, timestamp=1547609241178, value=password1
 2                               column=base:name, timestamp=1547609241178, value=user2
 2                               column=base:password, timestamp=1547609241178, value=password2
 3                               column=base:name, timestamp=1547609241178, value=user3
 3                               column=base:password, timestamp=1547609241178, value=password3
3 row(s) in 0.1540 seconds
```

其它导入参数可以参考
> http://sqoop.apache.org/docs/1.4.7/SqoopUserGuide.html
