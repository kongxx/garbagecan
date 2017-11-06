# Docker私有仓库管理

前一篇文章说了怎样搭建私有仓库，但是在仓库搭建好了之后发现维护仓库里的镜像还是不太方便，docker 官方也没有很好命令行接口来使用，只是提供了API，可以通过调用这些API来查看和删除镜像，但是这些API实在是不好用，所以找了一下发现有个开源的python工具可以实现这些功能。

## 安装

首先从下面的网站下载最新的 python 脚本，这里只要下载 registry.py 文件就可以了。

- https://github.com/andrey-pohilko/registry-cli

安装依赖的第三方包

``` shell
$ sudo pip install requests
``` 

## 配置
要使 Docker 仓库的镜像可以被删除，需要修改一下仓库的配置文件，在CentOS7上配置文件路径是：/etc/docker-distribution/registry/config.yml。在文件中需要添加

``` shell
storage:
    ...
    delete:
        enabled: true
``` 

修改后文件类似：

``` shell
$ cat /etc/docker-distribution/registry/config.yml
version: 0.1
log:
  fields:
    service: registry
storage:
    cache:
        layerinfo: inmemory
    filesystem:
        rootdirectory: /var/lib/registry
    delete:
        enabled: true
http:
    addr: :5000
``` 

修改完成后需要重新启动仓库服务。
``` shell
$ sudo systemctl restart docker-distribution.service
```

## 使用

首先查看一下仓库里有哪些镜像
``` shell
$ python registry.py -r http://localhost:5000
```

为了测试需要，我们先创建几个镜像
``` shell
$ sudo docker pull busybox

$ sudo docker tag busybox:latest localhost:5000/kongxx/busybox:v1
$ sudo docker push localhost:5000/kongxx/busybox:v1

$ sudo docker tag busybox:latest localhost:5000/kongxx/busybox:v2
$ sudo docker push localhost:5000/kongxx/busybox:v2

$ sudo docker tag busybox:latest localhost:5000/kongxx/busybox:v3
$ sudo docker push localhost:5000/kongxx/busybox:v3
```

再次查看一下仓库里的镜像
``` shell
$ python registry.py -r http://localhost:5000
---------------------------------
Image: kongxx/mybusybox
  tag: v1
  tag: v2
  tag: v3
---------------------------------
Image: ...
```

可以发现已经有一个镜像的三个tag存在了。下面我们删除一下镜像的tag

``` shell
# 删除镜像tag，但是默认会保留10个
$ python registry.py -r http://localhost:5000 -i kongxx/busybox -d 

# 删除镜像的所有tag
$ python registry.py -r http://localhost:5000 -i kongxx/busybox --delete-all

# 删除镜像tag，保留1个
$ python registry.py -r http://localhost:5000 -i kongxx/busybox -d --num 1
```

删除所有镜像tag后，我们查看一下，虽然镜像已经不包括任何tag了，但是镜像仍然存在
``` shell
$ python registry.py -r http://localhost:5000
---------------------------------
Image: kongxx/busybox
  no tags!
```

此时要想彻底删除镜像，可以先删除下面目录下的镜像目录，这个目录在 /etc/docker-distribution/registry/config.yml 文件中有定义
``` shell
/var/lib/registry/docker/registry/v2/repositories/<image>
```

然后运行 Docker 仓库的命令 garbage-collect 命令来清理一下。

``` shell
$ sudo registry garbage-collect /etc/docker-distribution/registry/config.yml
```
