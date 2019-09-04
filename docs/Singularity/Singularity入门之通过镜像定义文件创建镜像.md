# Singularity入门之通过镜像定义文件创建镜像

## 镜像配置文件

下面以 Redis 数据库为例，主要说说 %startscript 和 %runscript 的区别。

看下面 redis.def 配置文件

``` shell
Bootstrap:library
From: ubuntu:18.04

%help
	Redis 5.0.0 for Ubuntu

%post
	apt-get update
	apt-get install -y wget build-essential
	wget http://download.redis.io/releases/redis-5.0.0.tar.gz
	tar xzf redis-5.0.0.tar.gz
	cd redis-5.0.0
	make

%startscript
	/redis-5.0.0/src/redis-server

%runscript
	/redis-5.0.0/src/redis-cli
```

其中：
- %startscript 是指容器启动时运行的命令，见下面：启动 Redis Server
- %runscript 是指容器执行时运行的命令，通过 run 子命令或者直接运行容器镜像来执行，见下面：运行 Redis Client
- 具体配置文件说明，可以参考： https://sylabs.io/guides/3.2/user-guide/definition_files.html

## 制作镜像

制作镜像需要 root 权限，所以下面命令用 sudo 来执行。

``` shell
$ sudo singularity build redis.sif redis.def
```

## 使用镜像

### 启动 Redis Server

启动 Redis Server 会调用 %startscript 来启动 Redis Server。

``` shell
$ singularity instance start redis.sif redis
INFO:    instance started successfully
```

### 运行 Redis Client

运行 Redis Client 会调用 %runscript 来运行 Redis Client。

``` shell
# 通过 run 子命令运行 Redis Client
$ singularity run redis.sif
127.0.0.1:6379> ?

# 通过直接执行镜像来运行 Redis Client
$ ./redis.sif
127.0.0.1:6379> 
```
