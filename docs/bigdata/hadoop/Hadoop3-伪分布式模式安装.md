# Hadoop3-伪分布式模式安装

今天无意间看到Hadoop3去年年底就release了，今天就准备装个环境看看。

## 安装配置

首先从下面的地址下载安装包

- http://hadoop.apache.org/releases.html

这里我下载的是hadoop-3.0.0.tar.gz包，解压安装。

``` shell
$ tar zxvf hadoop-3.0.0.tar.gz
$ cd hadoop-3.0.0/
```

编辑etc/hadoop/hadoop-env.sh文件，设置JAVA_HOME环境变量,
``` shell
export JAVA_HOME=/opt/jdk8
```

修改配置文件 core-site.xml
``` xml
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://<hostname>:9000</value>
  </property>
</configuration>
```

修改配置文件 hdfs-site.xml，因为是伪分布式模式，所以设置复制为1。
``` xml
<configuration>
  <property>
    <name>dfs.replication</name>
    <value>1</value>
  </property>
</configuration>
```

## 运行 HDFS

### 格式化 HDFS

第一次启动 HDFS 时，需要做一次格式化才行。

``` shell
$ bin/hdfs namenode -format
```

### 启动 HDFS

``` shell
$ sbin/start-dfs.sh
```

启动 HDFS 后，可以通过浏览器访问下面的地址查看HDFS状态。

- http://localhost:9870/

### 运行 MapReduce 作业

先创建当前用户在 HDFS 中的家目录，如下

``` shell
$ bin/hdfs dfs -mkdir /user
$ bin/hdfs dfs -mkdir /user/<username>
```

准备数据，运行测试并查看结果

``` shell
$ bin/hdfs dfs -mkdir input
$ bin/hdfs dfs -put etc/hadoop/*.xml input
$ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.0.0.jar grep input output 'dfs[a-z.]+'
$ bin/hdfs dfs -cat output/*
```

删除上面测试结果
``` shell
$ bin/hdfs dfs -rm output/*
$ bin/hdfs dfs -rmdir output/
```

### 停止 HDFS

``` shell
$ sbin/stop-dfs.sh
```

## 运行 YARN

### 修改 etc/hadoop/mapred-site.xml 文件

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

### 修改 etc/hadoop/yarn-site.xml 文件

``` xml
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
```

### 启动 YARN

``` shell
$ sbin/start-yarn.sh
```

启动后可以通过下面地址查看作业请求

- http://192.168.0.192:8088/cluster

### 运行 MapReduce 作业

``` shell
$ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.0.0.jar grep input output 'dfs[a-z.]+'
$ bin/hdfs dfs -cat output/*
```

### 停止 YARN
``` shell
$ sbin/stop-yarn.sh
http://192.168.0.192:8088/cluster
```

## 问题

在测试YARN的过程中，开始总出现类似下面的错误，导致作业都运行失败

``` shell
[2018-01-30 22:40:02.211]Container [pid=22658,containerID=container_1517369701504_0003_01_000028] is running beyond virtual memory limits. Current usage: 87.9 MB of 1 GB physical memory used; 2.6 GB of 2.1 GB virtual memory used. Killing container.
```

最后发现是机器内存不够，导致yarn的配置在我的机器上不合理，所以修改了一下 etc/hadoop/yarn-site.xml 文件，添加下面两个配置项目，然后重启yarn就可以了。
``` xml
 <property>
   <name>yarn.nodemanager.vmem-check-enabled</name>
    <value>false</value>
    <description>Whether virtual memory limits will be enforced for containers</description>
  </property>
 <property>
   <name>yarn.nodemanager.vmem-pmem-ratio</name>
    <value>4</value>
    <description>Ratio between virtual memory to physical memory when setting memory limits for containers</description>
  </property>
```

- 参考： https://stackoverflow.com/questions/21005643/container-is-running-beyond-memory-limits
