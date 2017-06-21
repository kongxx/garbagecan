# Kafka 单节点单Kafka Broker集群

## 下载与安装
从 http://www.apache.org/dist/kafka/ 下载最新版本的 kafka，这里使用的是 kafka_2.12-0.10.2.1.tgz

``` shell
$ tar zxvf kafka_2.12-0.10.2.1.tgz
$ cd kafka_2.12-0.10.2.1
```

## 运行

### 启动 zookeeper 服务
``` shell
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```

### 启动 kafka Broker 服务
``` shell
$ bin/kafka-server-start.sh config/server.properties
```

## 测试

首先，创建一个单分区单副本的 topic: mytopic

``` shell
$ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic mytopic
Created topic "mytopic".
```

然后，可以通过运行 list 命令来查看已经存在的 topic，比如：

``` shell
$ bin/kafka-topics.sh --list --zookeeper localhost:2181
mytopic
```

也可以使用 describe 命令来查看。由于我们现在是单分区单副本的case，所以 Leader 和 Isr （复制节点集合）都只在Broker 0上。

``` shell
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic mytopic
Topic:mytopic	PartitionCount:1	ReplicationFactor:1	Configs:
	Topic: mytopic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
```

现在，我们通过 Kafka 自带命令行客户端向这个 topic 发送消息。

``` shell
$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic mytopic
aaa
bbb
ccc
```

另开一个终端，然后使用 Kafka 自带的命令行工具来消费这些消息。

``` shell
$ bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic mytopic --from-beginning
...
aaa
bbb
ccc
```

此时可以在 producer 命令行窗口继续输入消息，然后观察 consumer 终端窗口，可以看到它们被消费打印出来。

## 使用 Kafka Connect 导入导出数据

下面使用 Kafka 的 Connect 演示从一个变化的文件中读取增量数据然后输出到另外一个文件中。

首先运行下面的脚本，此脚本会每隔一秒会向 test.txt 文件中追加一个数字。
``` shell
$ for i in {1..300};do echo $i >> test.txt; sleep 1; done
```

然后运行下面的脚本，此时生产者从 test.txt 文件中读取文件内容并作为消息发送到topic中，然后消费者从topic中消费消息并输出到 test.sink.txt 文件中。命令行使用的配置文件中定义了输入输出的文件名和使用的topic名。
``` shell
$ bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
```

运行后，另开一个终端来观察 test.sink.txt 文件中的内容，可以看到文件中的内容会不停的增加。

## 删除 Topic

要删除 topic，可以使用下面的命令
``` shell
$ bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic mytopic
```

但是运行命令后，topic并没有被删除，使用 “bin/kafka-topics.sh --list --zookeeper localhost:2181” 仍然可以查到。此时我们需要修改config/server.properties文件中的 “delete.topic.enable=true” 来打开这个功能。此时我们再执行上面的 --delete 操作，即可删除topic了。
