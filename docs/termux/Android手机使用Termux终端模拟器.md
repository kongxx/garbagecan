# Android手机使用Termux终端模拟器

Termux 是 Android 平台上的一个终端模拟器，可以在 Android 手机上模拟 Linux 环境。它提供命令行界面，并且提供了功能健全的包管理工具（pkg）。另外就是 Termux 不需要 root 权限，安装后默认产生一个用户，可以运行 App 后使用 whoami 命令查看。

## 安装Termux

从 https://github.com/termux/termux-app 下载最新的安装包，这里我下载的 termux-app_v0.118.0+github-debug_arm64-v8a.apk 版本，然后在手机上安装。

安装完成后打开 Termux 就可以看到熟悉的 Linux 命令行窗口了。但是在手机上使用还是屏幕太小，所以先装个ssh服务，然后通过电脑连上来用吧。

## 安装启动SSH服务

安装 openssl 和 openssh 包

``` shell
~ $ pkg install openssl
...
~ $ pkg install openssh
...
```

安装完，在启动 sshd 之前，先看一下用户信息

``` shell
~ $ whoami
ssh u0_a390
```

设置一下用户密码

``` shell
~ $ passwd
...
...
```

启动ssh服务

``` shell
~ $ sshd
```

Termux 启动 sshd 的默认端口不是22，而是 8022，下面使用 ssh 登录验证一下，先查看一下 Termux 的 IP 地址

``` shell
~ $ ifconfig
```

远程登录 Termux

``` shell
$ ssh <user>@<ip> -p 8022
```

## 使用 sudo

Termux默认不能使用sudo，如果要使用sudo，需要安装 tsh 包

``` shell
pkg install tsu
```

## 配置 Termux 更新源

修改 Termux 的软件更新源，可以通过两种方法来修改

### 通过工具修改

首先安装termux-tools工具，然后再通过termux-change-repo工具设置，termux-change-repo工具会运行一个图形界面，然后可以通过图形界面选择。

``` shell
~ $ pkg install termux-tools

~ $ termux-change-repo
```

### 通过手动配置文件修改

直接编辑  /data/data/com.termux/files/usr/etc/apt/sources.list 文件，将原内容由
deb https://packages-cf.termux.dev/apt/termux-main stable main
改成
deb https://mirrors.ustc.edu.cn/termux/apt/termux-main stable main
或
deb https://mirrors.aliyun.com/termux/termux-main stable main
