# KVM操作

[TOC]

## 安装
``` bash
sudo yum install libvirt.x86_64
```

## 命令行

- 连接KVM

``` bash
$ virsh -c qemu+ssh://root@<host>/system
```

### 列出虚拟机

``` bash
virsh # list
virsh # list --all
```

### 查看虚拟机信息

``` bash
virsh # dominfo <vm>
```

### 启动/停止虚拟机

``` bash
virsh # start <vm>
virsh # shutdown <vm>
```

### 暂停/继续虚拟机

``` bash
virsh # suspend <vm>
virsh # resume <vm>
```

### 保存/恢复虚拟机

``` bash
virsh # save <vm> /tmp/<vm>.saved
virsh # restore /tmp/<vm>.saved
```