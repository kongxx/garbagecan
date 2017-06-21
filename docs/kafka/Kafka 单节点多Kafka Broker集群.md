# Kafka 单节点多Kafka Broker集群

接前一篇文章，今天搭建一下单节点多Kafka Broker集群环境。

## 配置与启动服务
由于是在一个节点上启动多个 Kafka Broker实例，所以我们需要使用不同的端口来实现。

``` shell
$ cp config/server.properties config/server-1.properties
$ cp config/server.properties config/server-2.properties
```

修改 config/server-1.properties
```
broker.id=1
listeners=PLAINTEXT://:9093
log.dir=/tmp/kafka-logs-1
```

修改 config/server-2.properties
```
broker.id=2
listeners=PLAINTEXT://:9094
log.dir=/tmp/kafka-logs-2
```

在三个终端中分别使用不同的配置文件启动 kafka，如下：

``` shell
# 终端一
$ bin/kafka-server-start.sh config/server.properties

# 终端二
$ bin/kafka-server-start.sh config/server-1.properties

#终端三
$ bin/kafka-server-start.sh config/server-2.properties
```

## 测试集群

创建一个复制因子是3的topic：

``` shell
$ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic mytopic
Created topic "mytopic".
```

创建后，使用 --describe 来查看一下

``` shell
bin/kafka-topics.sh --describe --zookeeper localh2181 --topic mytopic
Topic:mytopic	PartitionCount:1	ReplicationFactor:3	Configs:
	Topic: mytopic	Partition: 0	Leader: 2	Replicas: 2,1,0	Isr: 2,1,0
```

可以看出现在的Leader是 2 节点，复制节点是：2，1，0三个节点。

现在我们分别在两个终端上运行下面的命令来测试生产消息和消费消息。

``` shell
# 终端 A 生产消息
$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic mytopic
aaa
bbb
ccc
```

``` shell
# 终端 B 消费消息
$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic mytopic
aaa
bbb
ccc
```

目前为止一切工作正常，现在我们把其中的 Leader: 2 实例 kill 掉，然后再观察一下。

``` shell
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic mytopic
Topic:mytopic	PartitionCount:1	ReplicationFactor:3	Configs:
	Topic: mytopic	Partition: 0	Leader: 1	Replicas: 2,1,0	Isr: 1,0
```

发现 Leader 变成了 1，复制节点变成了两个：1，0。

此时，继续在“终端 A”中生产消息，然后可以看到“终端 B”中会继续消费新的消息。这说明尽管原来负责写入的 Leader 已经挂掉，但是消息不会丢失，仍然可以继续被消费。
