# 使用phoenix查询hbase

今天需要从的 hbase 使用 sql 来查询数据，于是想到了使用 phoenix 工具，在自己的环境里大概试了一下，一下子就通了，就这么神奇。

## 下载安装
首先从 apache 下载 phoenix 包，这里因为我的hbase hbase-1.4.9 版，所以我下载的对应的版本 apache-phoenix-4.14.1-HBase-1.4-bin.tar.gz

下载后解压之 apache-phoenix-4.14.1-HBase-1.4-bin.tar.gz

``` shell
tar zxvf apache-phoenix-4.14.1-HBase-1.4-bin.tar.gz
```

## 准备 hbase
为了使用 phoenix，需要将 phoenix 目录下的 phoenix-*。jar 包复制到 hbase 的 lib 目录下，比如：

``` shell
cp apache-phoenix-4.14.1-HBase-1.4-bin/phoenix-*.jar hbase-1.4.9/lib/
```

然后重新系统 hbase 服务

``` shell
cd hbase-1.4.9/bin
./stop-hbase.sh
./start-hbase.sh
```

## 连接phoenix

可以使用下面两种方式连接

### 方式一：直接连接

``` shell
cd apache-phoenix-4.14.1-HBase-1.4-bin/bin
# 默认连接本地hbase
./sqlline.py

# 连接指定机器的hbase
./sqlline.py localhost:2181
```

### 方式二：通过 queryserver 连接

首先启动 queryserver 服务

``` shell
cd apache-phoenix-4.14.1-HBase-1.4-bin/bin
./queryserver.py
```

然后使用 sqlline-thin 命令连接

``` shell
cd apache-phoenix-4.14.1-HBase-1.4-bin/bin
# 默认连接本地hbase
./sqlline-thin.py

# 连接指定机器的hbase
./sqlline-thin.py localhost:8765
```

## 表操作

### 基本操作

``` shell
# 查看帮助
0: jdbc:phoenix:thin:url=http://localhost:876> !?
...

# 查看连接
0: jdbc:phoenix:thin:url=http://localhost:876> !list
1 active connection:
 #0  open     jdbc:phoenix:thin:url=http://localhost:8765;serialization=PROTOBUF

# 查询表
0: jdbc:phoenix:thin:url=http://localhost:876> !table
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----+
| TABLE_CAT  | TABLE_SCHEM  | TABLE_NAME  |  TABLE_TYPE   | REMARKS  | TYPE_NAME  | SELF_REFERENCING_COL_NAME  | REF_GENERATION  | INDEX_STATE  | IMM |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----+
|            | SYSTEM       | CATALOG     | SYSTEM TABLE  |          |            |                            |                 |              | fal |
|            | SYSTEM       | FUNCTION    | SYSTEM TABLE  |          |            |                            |                 |              | fal |
|            | SYSTEM       | LOG         | SYSTEM TABLE  |          |            |                            |                 |              | tru |
|            | SYSTEM       | SEQUENCE    | SYSTEM TABLE  |          |            |                            |                 |              | fal |
|            | SYSTEM       | STATS       | SYSTEM TABLE  |          |            |                            |                 |              | fal |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----+

# 创建一个数据表 users
0: jdbc:phoenix:thin:url=http://localhost:876> CREATE TABLE users (id INTEGER PRIMARY KEY, username VARCHAR, password VARCHAR);
No rows affected (1.573 seconds)
0: jdbc:phoenix:thin:url=http://localhost:876> !table
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----+
| TABLE_CAT  | TABLE_SCHEM  | TABLE_NAME  |  TABLE_TYPE   | REMARKS  | TYPE_NAME  | SELF_REFERENCING_COL_NAME  | REF_GENERATION  | INDEX_STATE  | IMM |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----+
|            | SYSTEM       | CATALOG     | SYSTEM TABLE  |          |            |                            |                 |              | fal |
|            | SYSTEM       | FUNCTION    | SYSTEM TABLE  |          |            |                            |                 |              | fal |
|            | SYSTEM       | LOG         | SYSTEM TABLE  |          |            |                            |                 |              | tru |
|            | SYSTEM       | SEQUENCE    | SYSTEM TABLE  |          |            |                            |                 |              | fal |
|            | SYSTEM       | STATS       | SYSTEM TABLE  |          |            |                            |                 |              | fal |
|            |              | USERS       | TABLE         |          |            |                            |                 |              | fal |
+------------+--------------+-------------+---------------+----------+------------+----------------------------+-----------------+--------------+-----+

# 写入数据
0: jdbc:phoenix:thin:url=http://localhost:876> UPSERT INTO users (id, username, password) VALUES (1, 'admin', 'Letmein');
1 row affected (0.119 seconds)
0: jdbc:phoenix:thin:url=http://localhost:876>  UPSERT INTO users (id, username, password) VALUES (1, 'kongxx', 'Letmein');
1 row affected (0.033 seconds)

# 查询数据
0: jdbc:phoenix:thin:url=http://localhost:876> select * from users;
+-----+-----------+-----------+
| ID  | USERNAME  | PASSWORD  |
+-----+-----------+-----------+
| 1   | kongxx    | Letmein   |
+-----+-----------+-----------+
1 row selected (0.107 seconds)
```

### 操作多column-family的数据表

``` shell
cd apache-phoenix-4.14.1-HBase-1.4-bin/bin
./sqlline.py

# 创建一个表包括两个 column-family：A 和 B，每个 column-family 里包含两个 column
0: jdbc:phoenix:> CREATE TABLE TEST (PK INTEGER PRIMARY KEY, A.A1 VARCHAR, A.A2 VARCHAR, B.B1 VARCHAR, B.B2 VARCHAR);
No rows affected (1.506 seconds)

# 写入数据
0: jdbc:phoenix:> UPSERT INTO TEST (PK, A.A1, A.A2, B.B1, B.B2) VALUES (1, 'a11', 'a12', 'b11', 'b12');
1 row affected (0.15 seconds)
0: jdbc:phoenix:> UPSERT INTO TEST (PK, A.A1, A.A2, B.B1, B.B2) VALUES (2, 'a21', 'a22', 'b21', 'b22');
1 row affected (0.015 seconds)

# 查询数据
0: jdbc:phoenix:> SELECT * FROM TEST;
+-----+------+------+------+------+
| PK  |  A1  |  A2  |  B1  |  B2  |
+-----+------+------+------+------+
| 1   | a11  | a12  | b11  | b12  |
| 2   | a21  | a22  | b21  | b22  |
+-----+------+------+------+------+
2 rows selected (0.111 seconds)

# 删除表
0: jdbc:phoenix:> drop table TEST;
```
