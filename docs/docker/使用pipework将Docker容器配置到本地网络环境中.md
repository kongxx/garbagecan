# 使用pipework将Docker容器配置到本地网络环境中

## 需求
在使用Docker的过程中，有时候我们会有将Docker容器配置到和主机同一网段的需求。要实现这个需求，我们只要将Docker容器和主机的网卡桥接起来，再给Docker容器配上IP就可以了。

下面我们就使用pipework工具来实现这一需求。

## 安装pipework
``` shell
$ git clone https://github.com/jpetazzo/pipework
$ sudo cp pipework/pipework /usr/local/bin/
```

## 使用pipework

首先看一下，当前我的主机的IP地址为192.168.0.109/24，网关为192.168.0.1，使用的是eth0这块网卡。假定我需要给Docker容器的分配的地址为192.168.0.200。

首先创建一个容器
``` shell
$ sudo docker run -itd --name test ubuntu /bin/bash
```

此时，查看容器的IP地址信息，其中只有一个eth0，IP地址是172.17.0.3/16 是Docker默认分配的地址，如下：
``` shell
$ sudo docker exec test ip addr show
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue qlen 1
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
25: eth0@if26: <BROADCAST,MULTICAST,UP,LOWER_UP,M-DOWN> mtu 1500 qdisc noqueue 
    link/ether 02:42:ac:11:00:03 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.3/16 scope global eth0
       valid_lft forever preferred_lft forever
    inet6 fe80::42:acff:fe11:3/64 scope link 
       valid_lft forever preferred_lft forever
```

下面配置容器test的网络，并连接到网桥br0上，其中@后面是网关地址
``` shell
$ sudo pipework br0 test 192.168.0.200/24@192.168.0.1
```

> 这一步中，pipework首先会检查主机是否存在br0网桥，若不存在，就自己创建一个。这里以"br"开头，所以创建的是Linux bridge。如果以"ovs"开头，就会创建OpenVswitch网桥。
>
> 另外，如果主机环境中有DHCP服务，也可以通过DHCP的方式获取IP
>
> ``` shell
> $ sudo pipework br0 test dhcp
> ```
>

此时查看容器的IP地址信息，发现新增加了一个网卡eth1，分配的IP地址是192.168.0.200/24，如下：
``` shell
$ sudo docker exec test ip addr show
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue qlen 1
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
25: eth0@if26: <BROADCAST,MULTICAST,UP,LOWER_UP,M-DOWN> mtu 1500 qdisc noqueue 
    link/ether 02:42:ac:11:00:03 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.3/16 scope global eth0
       valid_lft forever preferred_lft forever
    inet6 fe80::42:acff:fe11:3/64 scope link 
       valid_lft forever preferred_lft forever
27: eth1@if28: <BROADCAST,MULTICAST,UP,LOWER_UP,M-DOWN> mtu 1500 qdisc noqueue qlen 1000
    link/ether 4e:ab:e0:c5:a7:81 brd ff:ff:ff:ff:ff:ff
    inet 192.168.0.200/24 brd 192.168.0.255 scope global eth1
       valid_lft forever preferred_lft forever
    inet6 fe80::4cab:e0ff:fec5:a781/64 scope link 
       valid_lft forever preferred_lft forever
```

将主机eth0桥接到br0上，并把eth0的IP配置在br0上。这里由于是远程操作，中间网络会断掉，所以放在一条命令中执行。

``` shell
$ sudo ip addr add 192.168.0.109/24 dev br0
$ sudo ip addr del 192.168.0.109/24 dev eth0
$ sudo brctl addif br0 eth0
$ sudo ip route del default
$ sudo ip route add default via 192.168.0.1 dev br0
```

上面的命令执行过程中会断一次网，所以可以将上面的命令放到一步中执行，如下：

``` shell
$ sudo ip addr add 192.168.0.109/24 dev br0; \
    sudo ip addr del 192.168.0.109/24 dev eth0; \
    sudo brctl addif br0 eth0; \
    sudo ip route del default; \
    sudo ip route add default via 192.168.0.1 dev br0
```

> 注：上面这一步只有在第一次绑定容器地址的时候会用到，一旦执行过后，就会在主机上把原来eth0的IP地址分配给br0，然后把eth0和br0连起来。所以以后再创建容器就不需要执行这一步了。而只需要执行下面的绑定容器地址的命令就可以了。
>
> ``` shell
> $ sudo pipework br0 test 192.168.0.200/24@192.168.0.1
> ```
>

运行上面命令后查看主机的IP地址信息，如下：
``` shell
$ ip addr show eth0
2: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast master br0 state UP qlen 1000
    link/ether 76:e0:49:4f:00:ac brd ff:ff:ff:ff:ff:ff
    inet 192.168.0.109/24 brd 192.168.0.255 scope global dynamic eth0
       valid_lft 1464sec preferred_lft 1464sec
    inet6 fe80::6be1:989e:26e9:488e/64 scope link 
       valid_lft forever preferred_lft forever
$ ip addr show br0
12: br0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP qlen 1000
    link/ether 76:e0:49:4f:00:ac brd ff:ff:ff:ff:ff:ff
    inet 192.168.0.109/24 scope global br0
       valid_lft forever preferred_lft forever
    inet6 fe80::fc31:97ff:fe9e:872c/64 scope link 
       valid_lft forever preferred_lft forever
```

现在，另开一个终端，可以通过ping 192.168.0.200来测试容器网络。或者从其它物理主机上ping这个容器地址。

