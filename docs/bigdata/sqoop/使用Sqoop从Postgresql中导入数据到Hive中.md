# 使用Sqoop从Postgresql中导入数据到Hive中

这里假定已经准备好了现成的Hadoop，Hive，Hbase，Zookeeper和一个postgresql数据库。

## 下载安装

从 http://mirror.bit.edu.cn/apache/sqoop/ 地址下载 sqoop 安装包，这里我使用的是1.4.7版本。

``` shell
wget -c http://mirror.bit.edu.cn/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz
```

解压压缩包到一个目录下

``` shell
cd /apps
tar zxvf sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz
```

## 配置

进入 sqoop 的 conf 目录，复制 sqoop-env-template.sh 为 sqoop-env.sh

``` shell
cd /apps/sqoop-1.4.7.bin__hadoop-2.6.0/conf
cp sqoop-env-template.sh sqoop-env.sh
```

编辑 sqoop-env.sh 文件，其中路径根据具体位置填写

``` shell
#Set path to where bin/hadoop is available
export HADOOP_COMMON_HOME=/apps/hadoop-2.7.7

#Set path to where hadoop-*-core.jar is available
export HADOOP_MAPRED_HOME=/apps/hadoop-2.7.7

#set the path to where bin/hbase is available
export HBASE_HOME=/apps/hbase-2.0.4

#Set the path to where bin/hive is available
export HIVE_HOME=/apps/apache-hive-2.3.2-bin

#Set the path for where zookeper config dir is
export ZOOCFGDIR=/apps/zookeeper-3.4.10/conf
```

验证安装配置

```shell
$ bin/sqoop-version
...
19/01/08 14:57:19 INFO sqoop.Sqoop: Running Sqoop version: 1.4.7
Sqoop 1.4.7
git commit id 2328971411f57f0cb683dfb79d19d4d19d185dd8
Compiled by maugli on Thu Dec 21 15:59:58 STD 2017
```

## 使用

### 连接 postgresql 数据库

首先需要准备 postgresql 的 jdbc 驱动包，并放入 sqoop 的根目录下。

准备数据库和表

``` shell
test=> create table users
(
  id serial primary key ,
  name varchar(128),
  password varchar(128)
);
test=> insert into users values(1,'user1','password1');
test=> insert into users values(2,'user2','password2');
test=> insert into users values(3,'user3','password3');

test=> select * from users;
 id | name  | password  
----+-------+-----------
  1 | user1 | password1
  2 | user2 | password2
  3 | user3 | password3
(3 rows)
```

查看数据库

``` shell
bin/sqoop list-databases --connect jdbc:postgresql://localhost:5432 --username test --password test
...
postgres
hive
test
```

查看数据库中表

``` shell
bin/sqoop list-tables --connect jdbc:postgresql://localhost:5432/test --username test --password test
...
users
```

查看数据表中数据

``` shell
bin/sqoop eval --connect jdbc:postgresql://localhost:5432/test --username test --password test -e 'select * from users'
-------------------------------------------------------------
| id          | name                 | password             | 
-------------------------------------------------------------
| 1           | user1                | password1            | 
| 2           | user2                | password2            | 
| 3           | user3                | password3            | 
-------------------------------------------------------------
```

### 从 postgresql 向 HDFS 导入数据

``` shell
# 导入数据到默认目录
$ bin/sqoop import --connect jdbc:postgresql://localhost:5432/test --username test  --password test --table users --m 1

# 查看hdfs文件系统
$ hdfs dfs -ls /user/kongxx/
drwxr-xr-x   - kongxx supergroup          0 2019-01-09 00:06 /user/kongxx/users

# 查看hdfs文件内容
$ hdfs dfs -cat /user/kongxx/users/*
1,user1,password1
2,user2,password2
3,user3,password3

# 导入数据到指定目录
$ bin/sqoop import --connect jdbc:postgresql://localhost:5432/test --username test --password test --table users --target-dir /user/kongxx/users2 --m 1

# 查看hdfs文件系统
$ hdfs dfs -ls /user/kongxx/
drwxr-xr-x   - kongxx supergroup          0 2019-01-09 00:06 /user/kongxx/users
drwxr-xr-x   - kongxx supergroup          0 2019-01-09 00:21 /user/kongxx/users2

# 查看hdfs文件内容
$ hdfs dfs -cat /user/kongxx/users2/*
1,user1,password1
2,user2,password2
3,user3,password3

# 导入使用查询语句查询的数据到指定目录，并指定分隔符
$ bin/sqoop import --connect jdbc:postgresql://localhost:5432/test --username test --password test --query 'select * from users where $CONDITIONS and 1=1' --target-dir /user/kongxx/users3 --fields-terminated-by '\t' --m 1

# 查看hdfs文件内容
$ hdfs dfs -cat /user/kongxx/users3/*
1	user1	password1
2	user2	password2
3	user3	password3
```

### 从 postgresql 向 Hive导入数据

在使用Hive前，需要在 sqoop 的根目录下创建一个 hive-exec.jar 的软连接，如下：

``` shell
ln -s /apps/apache-hive-2.3.2-bin/lib/hive-exec-2.3.2.jar
```

向 Hive 中导入数据

``` shell
# 导入数据到 hive 中 （也可以指定 Hive 中的数据库，表和使用增量导入方式）
$ bin/sqoop import --connect jdbc:postgresql://localhost:5432/test --username test --password test --table users --hive-import --hive-overwrite --lines-terminated-by "\n" --fields-terminated-by "\t" --m 1

# 查看数据文件
$ bin/hdfs dfs -cat /user/hive/warehouse/users/*
1	user1	password1
2	user2	password2
3	user3	password3
```

在 Hive 中查看数据

``` shell
$ hive

hive> show tables;
OK
users

hive> select * from users;
OK
1	user1	password1
2	user2	password2
3	user3	password3
```