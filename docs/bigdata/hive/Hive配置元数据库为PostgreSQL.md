# Hive配置元数据库为PostgreSQL

前一篇文章说了怎样搭建 Hive 环境，但是 Hive 使用的是默认 Derby 数据库作为元数据库，今天说说怎样把 Hive 的元数据库从默认的 Derby 改成 PostgreSQL 数据库。

## 安装 PostgreSQL

因为这里是侧重 Hive 的配置，所以安装和配置 PostgreSQL 的步骤就省略了。

## 配置 Hive 使用 PostgreSQL

编辑 ${HIVE_HOME}/conf/hive-site.xml文件，如果文件不存在，创建之。

``` xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
	<property>
		<name>javax.jdo.option.ConnectionURL</name>
		<value>jdbc:postgresql://<ip>:5432/<db></value>
	</property>
	<property>
		<name>javax.jdo.option.ConnectionDriverName</name>
		<value>org.postgresql.Driver</value>
	</property>
	<property>
		<name>javax.jdo.option.ConnectionUserName</name>
		<value><username></value>
	</property>
	<property>
		<name>javax.jdo.option.ConnectionPassword</name>
		<value><password></value>
	</property>
</configuration>
```

## 初始化PostgreSQL

``` shell
$ bin/schematool -dbType postgres -initSchema
```

## 测试

``` shell
$ bin/hive
hive> show databases;
OK
default
Time taken: 0.067 seconds, Fetched: 1 row(s)
hive> show tables;
OK
Time taken: 0.092 seconds
```
