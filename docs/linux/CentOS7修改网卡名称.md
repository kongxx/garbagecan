# CentOS7修改网卡名称

在CentOS7上，使用ifconfig查看默认的网卡名称是enp3s0，如果想修改网卡名称，比如把网卡名改为eth0，需要通过下面步骤来实现。

## 修改网卡配置文件名

``` shell
cd /etc/sysconfig/network-scripts
sudo mv ifcfg-enp3s0 ifcfg-eth0
```

## 修改网卡配置

编辑 ifcfg-eth0 文件，内容如下：

``` shell
DEVICE=eth0
ONBOOT=yes
TYPE=Ethernet
BOOTPROTO=dhcp
DEFROUTE=yes
NM_CONTROLLED=yes
PEERROUTES=yes
DNS1=8.8.8.8
```

## 修改 /etc/default/grub 配置

编辑 /etc/default/grub 文件

``` shell
将

GRUB_CMDLINE_LINUX="rhgb quiet"

改为

GRUB_CMDLINE_LINUX="net.ifnames=0 biosdevname=0 rhgb quiet"
```

## 重新生成grub配置文件

``` shell
sudo grub2-mkconfig -o /boot/grub2/grub.cfg 
```

最后，重新启动机器。
