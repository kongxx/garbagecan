# Druid单机安装与运行

## 准备 安装zookeeper

Druid目前版本依赖 zookeeper，所以在安装 Druid 之前，我们需要先安装一个 zookeeper。

``` shell
$ wget -c http://mirror.bit.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz
$ tar zxvf zookeeper-3.4.10.tar.gz
$ cd zookeeper-3.4.10
$ cp conf/zoo_sample.cfg conf/zoo.cfg

# 启动 zookeeper 服务
$ ./bin/zkServer.sh start

# 查看 zookeeper 状态
./bin/zkServer.sh status
./bin/zkCli.sh
```

## 安装Druid

``` shell
$ wget -c http://static.druid.io/artifacts/releases/druid-0.12.0-bin.tar.gz
$ tar zxvf druid-0.12.0-bin.tar.gz
# 这里假定安装目录为 /apps/druid-0.12.0
```

## 初始化Druid

``` shell
$ cd /apps/druid-0.12.0
$ bin/init
```

## 运行Druid

### 创建启动脚本

在 bin 目录下创建一个文件 start.sh，内容如下：

``` shell
#!/bin/sh

cd $(dirname $0)/../

java `cat conf-quickstart/druid/historical/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/historical:lib/*" io.druid.cli.Main server historical > log/historical.log &
sleep 5
java `cat conf-quickstart/druid/broker/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/broker:lib/*" io.druid.cli.Main server broker > log/broker.log &
sleep 5
java `cat conf-quickstart/druid/coordinator/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/coordinator:lib/*" io.druid.cli.Main server coordinator > log/coordinator.log &
sleep 5
java `cat conf-quickstart/druid/overlord/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/overlord:lib/*" io.druid.cli.Main server overlord > log/overlord &
sleep 5
java `cat conf-quickstart/druid/middleManager/jvm.config | xargs` -cp "conf-quickstart/druid/_common:conf-quickstart/druid/middleManager:lib/*" io.druid.cli.Main server middleManager > log/middleManager &
```

### 启动服务

``` shell
$ bin/start.sh
```

### 查看服务

可以通过访问下面两个url来查看集群状态
- http://localhost:8090/console.htm
- http://localhost:8081/#/

## 测试

### 加载数据

``` shell
$ curl -X 'POST' -H 'Content-Type: application/json' -d @quickstart/wikiticker-index.json localhost:8090/druid/indexer/v1/task
{"task":"index_hadoop_wikiticker_2018-06-01T05:55:31.174Z"}
```

### 查询任务状态

任务提交后，可以通过 http://localhost:8090/console.htm 页面查看任务状态。

### 查询数据

``` shell
$ curl -L -X POST -H 'Content-Type: application/json' -d @quickstart/wikiticker-top-pages.json http://localhost:8082/druid/v2/?pretty
```
