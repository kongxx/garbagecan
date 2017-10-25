# Docker使用Swarm组建集群

Docker 使用 Swarm 可以很方便的在多个主机上创建容器集群，并且容器之间可以跨主机网络通信。

Swarm 的集群分为 Manager 节点和 Worker 节点。

Swarm 中使用 Service 来创建/管理使用相同镜像的多个容器，多个容器同时对外提供服务，多个容器之间负载均衡。每个 Service 有一个浮动IP（VIP），各个容器还有自己的物理IP。创建基于 Swarm 的 Overlay 网络，将 Service 挂载到此网络上。然后 Service 中的各个容器便可以通过 Service 名称和 IP 地址实现网络互通。

下面使用了三个机器来组建一个Swarm集群

bd0    192.168.0.109
bd1    192.168.0.192
bd2    192.168.0.193

## Docker 开启远程管理

默认情况下，Docker守护进程会生成一个 /var/run/docker.sock 文件来进行本地进程通信，而不会监听任何端口，所以默认情况下 Docker 只能在本地使用命令行操作。 如果要在其它机器上远程操作 Docker 主机，就需要让 Docker 监听一个端口，这样才能实现远程通信。

在我的 CentOS 7X 上，首先修改 Docker 配置文件 /etc/sysconfig/docker 中的 OPTIONS 参数，添加

``` shell
-H unix:///var/run/docker.sock -H 0.0.0.0:5555
```

修改后参数类似

``` shell
OPTIONS='--selinux-enabled --log-driver=journald --signature-verification=false -H unix:///var/run/docker.sock -H 0.0.0.0:5555'
```

然后重新启动 Docker 服务

``` shell
$ sudo systemctl restart docker.service
```

测试一下

``` shell
sudo docker -H <HOST>:5555 images
```

## 创建 Swarm 集群

### 获取 Swarm 镜像

分别在三个节点上下载swarm镜像

``` shell
$ sudo docker pull swarm
```

### 初始化Swarm集群

首先在 bd0 节点上初始化 Swarm 集群

``` shell
$ sudo docker swarm init
Error response from daemon: could not choose an IP address to advertise since this system has multiple addresses on different interfaces (192.168.0.109 on eth0 and 192.168.122.1 on virbr0) - specify one with --advertise-addr
```

第一次创建失败了，原因是我的机器有两块网卡，Swarm不知道要用那块网卡组建集群，所以需要使用 --advertise-addr 来指定使用那块网卡。

``` shell
$ sudo docker swarm init --advertise-addr 192.168.0.109
Swarm initialized: current node (1egy2ark49q6xokudps5wykhn) is now a manager.
To add a worker to this swarm, run the following command:

    docker swarm join \
    --token SWMTKN-1-0x11m2uk7ps9bh7nflkxwirgv0syvacl18rut3hilz4i9lgis3-d9m22hixt0b57hjj81im8bqdl \
    192.168.0.109:2377

To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.
```

然后分别在 bd1 和 bd2 节点上运行下面命令将机器加入集群

``` shell
$ sudo docker swarm join \
  --token SWMTKN-1-0x11m2uk7ps9bh7nflkxwirgv0syvacl18rut3hilz4i9lgis3-d9m22hixt0b57hjj81im8bqdl \
  192.168.0.109:2377
This node joined a swarm as a worker.
```

下面查看一下集群节点信息

``` shell
$ sudo docker node ls
ID                           HOSTNAME  STATUS  AVAILABILITY  MANAGER STATUS
11ochjq4o1s2m6w4u8jxb37w6    bd1       Ready   Active
1egy2ark49q6xokudps5wykhn *  bd0       Ready   Active        Leader
b4e2ywnhhd6fhgfxtr1qh4gew    bd2       Ready   Active
```

## 创建集群跨主机网络

### 首先查看一下集群网络

``` shell
$ sudo docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
36679de6466b        bridge              bridge              local
72e853673d8b        docker_gwbridge     bridge              local
b45cef05e017        host                host                local
1zzlk9hpwyqy        ingress             overlay             swarm
```

### 添加一个Swarm网络

``` shell
$ sudo docker network create --driver overlay myswarm
a04evrfrr4cvnbvrummzvg0mn
```

创建后，查看一下集群网络

``` shell
$ sudo docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
36679de6466b        bridge              bridge              local
72e853673d8b        docker_gwbridge     bridge              local
b45cef05e017        host                host                local
1zzlk9hpwyqy        ingress             overlay             swarm
a04evrfrr4cv        myswarm             overlay             swarm
```

## 部署服务

这里使用 nginx 服务来演示一下怎样创建一个服务集群。

### 下载nginx镜像

``` shell
$ sudo docker pull nginx
```

### 创建服务

``` shell
$ sudo docker service create --replicas 2 --name mynginx --publish 8000:80 --network=myswarm nginx
5xrm96xveqw5gq63srts1rbhw
```

这里创建了需要两个 nginx 实例的 nginx 服务。同时，为了能被外网地址访问，我们做了一下端口映射，映射到物理主机的8000端口。

### 查看服务

``` shell
$ sudo docker service ls
ID            NAME     REPLICAS  IMAGE  COMMAND
5xrm96xveqw5  mynginx  2/2       nginx

$ sudo docker service ps mynginx
ID                         NAME       IMAGE  NODE     DESIRED STATE  CURRENT STATE           ERROR
1crmc5ecsjoci8xavxzbnbks3  mynginx.1  nginx  bd0      Running        Running 23 seconds ago
divhrq89xhxka8bvb8r9zqqhz  mynginx.2  nginx  bd2      Running        Running 20 seconds ago

$ sudo docker service inspect mynginx
...
"VirtualIPs": [
    {
        "NetworkID": "1zzlk9hpwyqyocloxy9j9vct7",
        "Addr": "10.255.0.6/16"
    },
    {
        "NetworkID": "a04evrfrr4cvnbvrummzvg0mn",
        "Addr": "10.0.0.2/24"
    }
]
...
```

在bd0上查看

``` shell
$ sudo docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
e480d427fd51        nginx:latest        "nginx -g 'daemon off"   41 seconds ago      Up 40 seconds       80/tcp              mynginx.1.1crmc5ecsjoci8xavxzbnbks3
```

在bd2上查看

``` shell
$ sudo docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
c12d4f9eb457        nginx:latest        "nginx -g 'daemon off"   56 seconds ago      Up 52 seconds       80/tcp              mynginx.2.divhrq89xhxka8bvb8r9zqqhz
```

服务已经创建并启动了，下面我们在浏览器访问 http://bd0:8000 来验证一下 nginx 服务的状态。

### 服务的Failover

首先我们查看一下当前服务的状态

``` shell
$ sudo docker service ps mynginx
ID                         NAME       IMAGE  NODE     DESIRED STATE  CURRENT STATE           ERROR
1crmc5ecsjoci8xavxzbnbks3  mynginx.1  nginx  bd0      Running        Running 23 seconds ago
divhrq89xhxka8bvb8r9zqqhz  mynginx.2  nginx  bd2      Running        Running 20 seconds ago
```

 然后在 bd2 上将其上的服务停止

``` shell
$ sudo docker kill mynginx.2.divhrq89xhxka8bvb8r9zqqhz
mynginx.2.divhrq89xhxka8bvb8r9zqqhz
```

再次观察服务状态，如下：

``` shell
$ sudo docker service ps mynginx
ID                         NAME           IMAGE  NODE     DESIRED STATE  CURRENT STATE            ERROR
1crmc5ecsjoci8xavxzbnbks3  mynginx.1      nginx  bd0      Running        Running 6 minutes ago
br5iyie0dr945ixnq7s77kunr  mynginx.2      nginx  bd1      Running        Running 4 seconds ago
divhrq89xhxka8bvb8r9zqqhz   \_ mynginx.2  nginx  bd2      Shutdown       Failed 5 seconds ago     "task: non-zero exit (137)"
```

可以看到在 bd1 上又起了一个实例用来接替原来在 bd2 上的实例。

### 服务的扩容/缩容

增加服务实例

``` shell
$ sudo docker service scale mynginx=3
mynginx scaled to 3

$ sudo docker service ps mynginx
ID                         NAME           IMAGE  NODE     DESIRED STATE  CURRENT STATE               ERROR
1crmc5ecsjoci8xavxzbnbks3  mynginx.1      nginx  bd0      Running        Running 7 minutes ago
br5iyie0dr945ixnq7s77kunr  mynginx.2      nginx  bd1      Running        Running about a minute ago
divhrq89xhxka8bvb8r9zqqhz   \_ mynginx.2  nginx  bd2      Shutdown       Failed about a minute ago   "task: non-zero exit (137)"
985tln0aprsvjthjpve0n6qmz  mynginx.3      nginx  bd2      Running        Preparing 3 seconds ago

```

减少服务实例

``` shell
$ sudo docker service scale mynginx=2
mynginx scaled to 2

$ sudo docker service ps mynginx
ID                         NAME           IMAGE  NODE     DESIRED STATE  CURRENT STATE                    ERROR
1crmc5ecsjoci8xavxzbnbks3  mynginx.1      nginx  bd0      Running        Running 7 minutes ago
br5iyie0dr945ixnq7s77kunr  mynginx.2      nginx  bd1      Shutdown       Shutdown less than a second ago
divhrq89xhxka8bvb8r9zqqhz   \_ mynginx.2  nginx  bd2      Shutdown       Failed about a minute ago        "task: non-zero exit (137)"
985tln0aprsvjthjpve0n6qmz  mynginx.3      nginx  bd2      Running        Running 5 seconds ago
```

### 删除服务

``` shell
$ sudo docker service rm mynginx
```
