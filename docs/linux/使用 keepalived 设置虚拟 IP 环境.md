# 使用 keepalived 设置虚拟 IP 环境

## 准备

装备两个机器，IP地址信息如下：

``` shell
host1： 192.168.56.103
host2： 192.168.56.104
```

为了测试需要，分别在两个机器上安装apache并启动httpd服务，使下面两个url均可访问
- http://192.168.56.103
- http://192.168.56.104

## 安装 keepalived
在两台机器上分别安装 keepalived

``` shell
$ sudo yum install -y keepalived
```

## 配置 keepalived

### host1 上 keepalived 配置

``` shell
$ cat /etc/keepalived/keepalived.conf
vrrp_instance VI_1 {
    state MASTER
    interface eth2
    virtual_router_id 51
    priority 101
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 123456
    }
    virtual_ipaddress {
        192.168.56.105
    }
}
```

### host2 上 keepalived 配置
``` shell
$ cat /etc/keepalived/keepalived.conf
vrrp_instance VI_1 {
    state MASTER
    interface eth2
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 123456
    }
    virtual_ipaddress {
        192.168.56.105
    }
}
```

## 启动 keepalived 服务

``` shell
$ sudo service keepalived start
$ sudo chkconfig keepalived on
```

## 测试

服务启动后，先访问下面的虚拟地址访问http服务器
``` shell
http://192.168.56.105
```

### 查看IP地址信息

host1 上 IP 地址信息
``` shell
$ ip addr show eth2
3: eth2: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
    link/ether 08:00:27:32:83:82 brd ff:ff:ff:ff:ff:ff
    inet 192.168.56.103/24 brd 192.168.56.255 scope global eth2
    inet 192.168.56.105/32 scope global eth2
    inet6 fe80::a00:27ff:fe32:8382/64 scope link
       valid_lft forever preferred_lft forever
```
* 其中可以看到 inet 192.168.56.105/32 scope global eth2，说明现在host1是作为虚拟IP的master来运行的。

host2 上 IP 地址信息
``` shell
$ ip addr show eth2
3: eth2: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
    link/ether 08:00:27:ec:90:7b brd ff:ff:ff:ff:ff:ff
    inet 192.168.56.104/24 brd 192.168.56.255 scope global eth2
    inet6 fe80::a00:27ff:feec:907b/64 scope link
       valid_lft forever preferred_lft forever
```
* 此时host2上ip地址信息中不包含虚拟IP “192.168.56.105” 信息。

### 验证 IP 地址 Failover

现在手动停止host1上的 keepalived 服务
``` shell
$ sudo service keepalived stop
```

host1 上 IP 地址信息
``` shell
$ ip addr show eth2
3: eth2: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
    link/ether 08:00:27:32:83:82 brd ff:ff:ff:ff:ff:ff
    inet 192.168.56.103/24 brd 192.168.56.255 scope global eth2
    inet6 fe80::a00:27ff:fe32:8382/64 scope link
       valid_lft forever preferred_lft forever
```
* 此时 host1 上 ip 地址信息中不再包含虚拟IP “192.168.56.105” 信息。

host2 上 IP 地址信息
``` shell
$ ip addr show eth2
3: eth2: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
    link/ether 08:00:27:ec:90:7b brd ff:ff:ff:ff:ff:ff
    inet 192.168.56.104/24 brd 192.168.56.255 scope global eth2
    inet 192.168.56.105/32 scope global eth2
    inet6 fe80::a00:27ff:feec:907b/64 scope link
       valid_lft forever preferred_lft forever
```
* 现在可以看到 host2 上 ip 地址信息中已经包含虚拟IP “192.168.56.105” 信息了。

此时如果再把 host1 上的 keepalived 服务启动，会发现虚拟IP “192.168.56.105” 又重新绑定到 host1 上了。
