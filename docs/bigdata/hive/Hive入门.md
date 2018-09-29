# Hive入门

## 介绍

Hive 是一个基于 Hadoop 的数据仓库工具，它可以将结构化数据文件映射成一张表，然后通过类似 SQL 的查询语句来执行查询。这些查询语句在Hive中被称作HQL，这些 HQL 会被翻译成 MapReduce 作业来执行。

Hive 把表和字段转换成 HDFS 中的文件夹和文件，并将这些元数据保持在关系型数据库中，如 derby 或 mysql。

Hive 查询的数据存储在HDFS上，运行在Yarn上。

Hive 适合做离线数据分析，如：批量处理和延时要求不高场景。

## 安装

### 安装 Hadoop

由于 Hive 是基于 Hadoop 的，所以需要先准备一个 Hadoop 环境。Hadoop 的安装参考我前面的文章。

### 安装 Hive

首先从下面地址下载最新版本的 Hive，这里我们使用 2.3.2 版。
- https://dist.apache.org/repos/dist/release/hive/hive-2.3.2/

下载并解压安装包

``` shell
$ wget -c https://dist.apache.org/repos/dist/release/hive/hive-2.3.2/apache-hive-2.3.2-bin.tar.gz
$ tar zxvf apache-hive-2.3.2-bin.tar.gz
$ cd apache-hive-2.3.2-bin
```

设置环境变量

``` shell
$ export JAVA_HOME=/opt/jdk8
$ export HADOOP_HOME=/apps/hadoop-3.0.0
$ export HIVE_HOME=/apps/apache-hive-2.3.2-bin
```

初始化 Derby 数据库，Hive 默认使用 Derby 数据库来保存元数据。生产环境建议使用 Mysql。

``` shell
$ bin/schematool -dbType derby -initSchema
```

## 运行 Hive

### 启动 Shell

通常我们使用 Hive 都是使用命令行工具来执行一些数据的更新和查询，下面命令就会启动 Hive 的命令行终端。

``` shell
$ bin/hive
> hive
```

### 查看表

``` shell
hive> show tables;
```

### 创建表

``` shell
hive> CREATE TABLE users(id int, username string, password string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';

or

hive> CREATE TABLE users(id int, username string, password string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS TEXTFILE;
```

### 插入查询数据

这里我们先准备一个数据文件 users.dat，内容如下：
``` shell
1,user1,password1
2,user2,password2
3,user3,password3
4,user4,password4
5,user5,password5
```

向数据表导入数据

``` shell
# 从本地文件系统导入
hive> LOAD DATA LOCAL INPATH '/tmp/users.dat' INTO TABLE users;

# 从 HDFS 导入
hive> LOAD DATA INPATH '/tmp/users.dat' INTO TABLE users;
```

导入数据后，使用 HQL 来查询结果

``` shell
hive> select * from users;
OK
1       user1   password1
2       user2   password2
3       user3   password3
4       user4   password4
5       user5   password5
```

此时，我们查看一下这些数据在 HDFS 上是怎样存储的，我在查看
``` shell
hive> dfs -ls /user/hive/warehouse/users
Found 1 items
-rwxr-xr-x   2 jhadmin supergroup         54 2018-02-05 00:14 /user/hive/warehouse/users/users.dat

hive> dfs -cat /user/hive/warehouse/users/users.dat
1,user1,password1
2,user2,password2
3,user3,password3
4,user4,password4
5,user5,password5
```

下面我们再插入一条记录看看，此时终端会出现MapReduce作业的相关信息。

``` shell
hive> INSERT INTO TABLE users(id, username, password) values (6, 'user6', 'password6');
... (MapReduce Job output)
```

``` shell
hive> select * from users;
OK
6       user6   password6
1       user1   password1
2       user2   password2
3       user3   password3
4       user4   password4
5       user5   password5
```

查看一下 HDFS 的文件信息，可以看到新添加了一个文件000000_0，其中保存着上面插入语句保存的数据。

``` shell
hive> dfs -ls /user/hive/warehouse/users
Found 2 items
-rwxr-xr-x   2 jhadmin supergroup         22 2018-02-05 00:26 /user/hive/warehouse/users/000000_0
-rwxr-xr-x   2 jhadmin supergroup         54 2018-02-05 00:14 /user/hive/warehouse/users/users.dat

hive> dfs -cat /user/hive/warehouse/users/000000_0
6,username6,password6
```

从上面的结果可以看到，每次单独的 INSERT 语句都会至少产生一个文件，因此在生产环境下还是要避免这样的插入操作，而应该使用批量导入来写入数据。

上面的查询语句 “select * from users;” 太简单，以至于看不出他是不是使用的 MapReduce 作业来执行查询的，下面我们把查询语句稍微修改一下，就可以看出 Hive 会把 HQL 转换成 MapReduce 作业来执行了。

``` shell
hive> select * from users order by id;
... (MapReduce Job output)
OK
1       user1   password1
2       user2   password2
3       user3   password3
4       user4   password4
5       user5   password5
6       user6   password6
```

