# 在Docker中使用mongodb数据库

## 获取mongo镜像

``` shell
sudo docker pull mongo
```

## 运行 mongodb 服务

``` shell
sudo docker run -p 27017:27017 -v /tmp/db:/data/db -d mongo
```

## 运行 mongodb 客户端

``` shell
sudo docker run -it mongo mongo --host <宿主机IP地址> --port 27017
> show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
> use local
switched to db local
> show collections
startup_log
> db.startup_log.find()
...
```

## 使用 mongo-express 管理mongodb

mongo-express是MongoDB的一个可视化图形管理工具，这里我们还是通过docker来运行一个mongo-express，来管理上面创建的mongodb服务。

### 下载 mongo-express 镜像
``` shell
sudo docker pull docker.io/mongo-express
```

### 启动 mongo-express 服务
``` shell
sudo docker run -it --rm -p 8081:8081 --link <mongoDB容器ID>:mongo mongo-express
```

### 访问 mongo-express

通过浏览器访问

``` shell
http://<宿主机IP地址>:8081
```

## 使用 mongoclient 管理 mongodb

### 下载 mongoclient 镜像
``` shell
sudo docker pull mongoclient/mongoclient
```

### 启动 mongoclient 服务
``` shell
sudo docker run --name mongoclient -d -p 3000:3000 -e MONGO_URL=mongodb://<宿主机IP地址>:27017/ mongoclient/mongoclient
```

### 访问 mongoclient

通过浏览器访问

``` shell
http://<宿主机IP地址>:3000
```
