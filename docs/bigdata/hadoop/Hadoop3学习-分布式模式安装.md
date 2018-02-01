# Hadoop3学习-分布式模式安装

接前一篇博客，这次做安装一个真正的分布式集群环境。

## 准备

- 这里准备三台机器bd1,bd2,bd3来组个hadoop集群，其中bd1作为namenode，bd1,bd2,bd3作为datanode
- 配置这三台机器之间可以免密码 SSH 登录，参考我以前的博客。

## 配置

修改下面几个配置文件

### etc/hadoop/core-site.xml

``` xml
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://bd1:9000</value>
  </property>
</configuration>
```

### etc/hadoop/hdfs-site.xml

``` xml
<configuration>
  <property>
    <name>dfs.replication</name>
    <value>3</value>
  </property>
</configuration>
```

- 这里由于我三个节点均作为datanode，所以复制配置3。

### etc/hadoop/mapred-site.xml

``` xml
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
	<property>
		<name>yarn.app.mapreduce.am.env</name>
		<value>HADOOP_MAPRED_HOME=/apps/hadoop-3.0.0</value>
	</property>
	<property>
		<name>mapreduce.map.env</name>
		<value>HADOOP_MAPRED_HOME=/apps/hadoop-3.0.0</value>
	</property>
	<property>
		<name>mapreduce.reduce.env</name>
		<value>HADOOP_MAPRED_HOME=/apps/hadoop-3.0.0</value>
	</property>
</configuration>
```

### etc/hadoop/yarn-site.xml

``` xml
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
	<property>
		<name>yarn.nodemanager.vmem-check-enabled</name>
		<value>false</value>
	</property>
	<property>
		<name>yarn.nodemanager.vmem-pmem-ratio</name>
		<value>4</value>
	</property>
</configuration>
```

### etc/hadoop/workers 
``` shell
bd1
bd2
bd3
```

## 运行 HDFS 和 yarn

### 格式化 HDFS

``` shell
$ bin/hdfs namenode -format
```

### 启动集群

``` shell
$ sbin/start-dfs.sh
$ sbin/start-yarn.sh
```

### 运行 MapReduce 作业

- 参考运行前一篇博客中使用 MapReduce 作业

### 停止集群

``` shell
$ sbin/stop-yarn.sh
$ sbin/stop-dfs.sh
```