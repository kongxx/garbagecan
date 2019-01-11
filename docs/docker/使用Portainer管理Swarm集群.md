# 使用 Portainer 管理 Docker Swarm 集群

- 关于怎样搭建使用Docker Swarm请参考 [Docker使用Swarm组建集群](https://www.jianshu.com/p/c7010569614d)

## 安装

``` shell
$ sudo docker pull portainer/portainer
```

## 使用

### 创建 volume

为了保存 portainer 的数据，这里创建了一个 volume，如下：

``` shell
$ sudo docker volume create portainer_data

$ sudo docker volume ls
DRIVER              VOLUME NAME
local               portainer_data
```

### 启动 Portainer

**注意：必须在 swarm 的 manager 节点运行。**
``` shell
$ sudo docker service create \
--name portainer \
--publish 9000:9000 \
--constraint 'node.role == manager' \
--mount type=bind,src=//var/run/docker.sock,dst=/var/run/docker.sock \
--mount type=volume,src=portainer_data,dst=/data \
portainer/portainer \
-H unix:///var/run/docker.sock
```

启动后，访问 http://<ip>:9000 来验证之。

也可以在命令行通过 docker service 查看 portainer 服务。

``` shell
$ sudo docker service ls
ID            NAME       MODE        REPLICAS  IMAGE
ge2nr7gnhlv2  portainer  replicated  1/1       portainer/portainer:latest

$ sudo docker service ps portainer
ID            NAME         IMAGE                       NODE     DESIRED STATE  CURRENT STATE           ERROR  PORTS
is6lpt326djg  portainer.1  portainer/portainer:latest  docker0  Running        Running 20 seconds ago
```

### 启动服务

通过 portainer 服务。

### 通过 Stacks 启动

访问 “登录 portainer -> Stacks -> Add Stack -> Web editor”，然后输入下面内容并 “Deploy the stack”

``` yaml
version: '3.1'

services:
  web:
    image: nginx
    deploy:
      replicas: 2
    ports:
      - "8000:80"
    restart: always
    networks:
      - myswarm

networks:
  myswarm:
    external: true
```

然后可以在 Stacks 和 Services 列表中查看。

### 通过 Services 启动

填表并创建 :) 然后可以在 Services 列表中查看。