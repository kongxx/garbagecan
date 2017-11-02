
# Docker搭建私有镜像仓库

现在Docker用处越来越多了，所以今天就想着搭建一个私有镜像仓库来维护内部我们自己的镜像。

## 环境
- CentOS 7.x
- Docker 1.12.6

## 安装 docker-distribution

``` shell
$ sudo yum install -y docker-distribution

$ sudo systemctl enable docker-distribution

$ sudo systemctl start docker-distribution
```

## 使用

### 获取测试镜像

首先从Docker中央仓库获取一个用来测试的容器镜像，这里就使用busybox来作为测试镜像。

``` shell
$ sudo docker pull busybox

$ sudo docker images
REPOSITORY                    TAG                 IMAGE ID            CREATED             SIZE
docker.io/busybox             latest              9d7e6df8e5ca        8 hours ago         1.129 MB
```

### 标记并上传镜像私有镜像

我们这里不对busybox做任何修改，只是换个名字作为私有镜像。

``` shell
$ sudo docker tag busybox:latest localhost:5000/kongxx/mybusybox:latest
$ sudo docker push localhost:5000/kongxx/mybusybox:latest
```

上传完成后可以使用下面命令查看一下

``` shell
$ curl http://192.168.0.109:5000/v2/kongxx/busybox/tags/list
{"name":"kongxx/busybox","tags":["latest"]}
```

同时我们查看一下本地的镜像列表

``` shell
$ sudo docker images
REPOSITORY                        TAG                 IMAGE ID            CREATED             SIZE
localhost:5000/kongxx/mybusybox   latest              9d7e6df8e5ca        8 hours ago         1.129 MB
docker.io/busybox                 latest              9d7e6df8e5ca        8 hours ago         1.129 MB
```

### 测试镜像仓库

为了能访问私有仓库（因为这里是自己测试，所以没有使用https），还需要修改一下Docker配置文件

编辑 /etc/sysconfig/docker 文件，将其中的 OPTIONS 参数加上
``` shell
--insecure-registry 192.168.0.109:5000
```

然后重新启动Docker服务

``` shell
$ sudo systemctl restart docker
```

为了测试，我们先把原来本地已经有的镜像删除

``` shell
$ sudo docker rmi docker.io/busybox
$ sudo docker rmi localhost:5000/kongxx/mybusybox
```

然后重新获取镜像，如下：

``` shell
$ sudo docker pull 192.168.0.109:5000/kongxx/mybusybox
Using default tag: latest
Trying to pull repository 192.168.0.109:5000/kongxx/mybusybox ...
latest: Pulling from 192.168.0.109:5000/kongxx/mybusybox
414e5515492a: Pull complete
Digest: sha256:fbcd856ee1f73340c0b7862201b9c045571d1e357797e8c4c0d02a0d21992b80
```

从输出可以看到已经可以从自己的仓库下载镜像了。

## 其他

最后说一下，如果要查询私有仓库里有哪些镜像，我还没有找到啥好方法可以一次全部查到，但是可以通过下面的组合命令来查询。

首先查询私有仓库上有那些镜像名
``` shell
$ curl -XGET http://192.168.0.109:5000/v2/_catalog
{"repositories":["kongxx/mybusybox","mandy/mybusybox"]}
```

然后使用下面的命令查看镜像有那些版本
``` shell
# curl -XGET http://192.168.0.109:5000/v2/<image_name>/tags/list
$ curl -XGET http://192.168.0.109:5000/v2/kongxx/mybusybox/tags/list
{"name":"kongxx/mybusybox","tags":["latest"]}
```
