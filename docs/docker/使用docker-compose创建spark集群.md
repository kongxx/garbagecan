# 使用docker-compose创建spark集群

## 下载docker镜像

``` shell
sudo docker pull sequenceiq/spark:1.6.0
```

## 创建docker-compose.yml文件

创建一个目录，比如就叫 docker-spark，然后在其下创建docker-compose.yml文件，内容如下：

``` shell
version: '2'

services:
  master:
    image: sequenceiq/spark:1.6.0
    hostname: master
    ports:
      - "4040:4040"
      - "8042:8042"
      - "7077:7077"
      - "8088:8088"
      - "8080:8080"
    restart: always
    command: bash /usr/local/spark/sbin/start-master.sh && ping localhost > /dev/null

  worker:
    image: sequenceiq/spark:1.6.0
    depends_on:
      - master
    expose:
      - "8081"
    restart: always
    command: bash /usr/local/spark/sbin/start-slave.sh spark://master:7077 && ping localhost >/dev/null

```

其中包括一个master服务和一个worker服务。

## 创建并启动spark集群

``` shell
sudo docker-compose up
```

集群启动后，我们可以查看一下集群状态

``` shell
sudo docker-compose ps
        Name                      Command               State                                                    Ports                                                 
----------------------------------------------------------------------
dockerspark_master_1   /etc/bootstrap.sh bash /us ...   Up      ...
dockerspark_worker_1   /etc/bootstrap.sh bash /us ...   Up      ...
```

默认我们创建的集群包括一个master节点和一个worker节点。我们可以通过下面的命令扩容或缩容集群。

``` shell
sudo docker-compose scale worker=2
```

扩容后再次查看集群状态，此时集群变成了一个master节点和两个worker节点。

``` shell
sudo docker-compose ps
        Name                      Command               State                                                    Ports                                                 
------------------------------------------------------------------------
dockerspark_master_1   /etc/bootstrap.sh bash /us ...   Up      ...      
dockerspark_worker_1   /etc/bootstrap.sh bash /us ...   Up      ...
dockerspark_worker_2   /etc/bootstrap.sh bash /us ...   Up      ...
```

此时也可以通过浏览器访问 http://ip:8080 来查看spark集群的状态。

## 运行spark作业

首先登录到spark集群的master节点

``` shell
sudo docker exec -it <container_name> /bin/bash
```

然后使用spark-submit命令来提交作业

``` shell
/usr/local/spark/bin/spark-submit --master spark://master:7077 --class org.apache.spark.examples.SparkPi /usr/local/spark/lib/spark-examples-1.6.0-hadoop2.6.0.jar 1000
```

## 停止spark集群
``` shell
sudo docker-compose down
```