# Termux安装Ubuntu和xfce4桌面

前面几篇 Termux 的文章介绍了怎么安装 xfce4，但是当时没有在 ubuntu 环境下安装，导致有些应用程序启动有问题，比如：eclipse、vsode和sublime等。所以这次打算在 Termux 上的 ubuntu 里安装桌面和应用程序试试。

## 登录 Termux 的 ubuntu 环境

安装并登录到 ubuntu 环境下 （安装方法参考前面的文章）

``` shell
~ $ proot-distro install ubuntu
~ $ proot-distro login ubuntu
```

后面的操作都需要在 ubuntu 上执行。

## 安装 sshd 服务

安装 ssh server 软件

``` shell
root@localhost:~#  apt update
root@localhost:~#  apt upgrade
root@localhost:~#  apt install openssh-server
```

配置 sshd 服务，编辑 /etc/ssh/sshd_config 文件，

``` shell
# 将端口配置成 8022
Port 8022

将
PermitRootLogin prohibit-password
改成
PermitRootLogin yes
```

然后启动 sshd 服务

``` shell
root@localhost:~# sshd
```

找个客户端，测试一下连接（注意端口配置的是8022）

``` shell
ssh root@<ip> -p 8022
```

## 安装 xfce4 桌面

安装 xfce4 桌面的软件包

``` shell
root@localhost:~# apt install xfce4 xfce4-goodies
```

## 安装 vncserver

这里注意下，咱们安装包都是用的 root 用户，但是启动 vnc 桌面和后面运行程序的时候都最好使用自己的用户来执行。

在 ubuntu 环境下可以使用 tightvnc 作为 vncserver

``` shell
kongxx@localhost:~$ sudo apt install tightvncserver
```

先启动一下 vncserver 设置一下密码

``` shell
kongxx@localhost:~$ vncserver :9
```

此时会提示输入密码和是否使用readonly模式。
启动好后，先不要连接，后面修改一下使用的桌面再连，所以这里直接把 vncserver 停掉。

``` shell
kongxx@localhost:~$ vncserver -kill :9
```

编辑  ~/.vnc/xstartup 文件，把最后一行设置启动 xfce4 桌面

``` shell
把
twm &
改成
xfce4-session &
```

再次启动 vncsrver，然后找个 vncclient 使用 5909 连接测试一下

``` shell
kongxx@localhost:~$ vncserver :9
```

## 安装应用程序

安装应用程序没啥说的，eclipse和sublime都很顺利就可以运行了，但是vscode启动后没有响应，于是乎手动运行一下，提示有权限问题

``` shell
kongxx@localhost:~$ code --verbose
[13497:1226/202227.454741:FATAL:setuid_sandbox_host.cc(158)] The SUID sandbox helper binary was found, but is not configured correctly. Rather than run without sandboxing I'm aborting now. You need to make sure that /usr/share/code/chrome-sandbox is owned by root and has mode 4755.
```

所以加个参数手动运行

``` shell
kongxx@localhost:~$ code --no-sandbox
```

## 访问手机的文件系统

可以通过 /storage/emulated/0/ 路径来访问手机文件系统。
