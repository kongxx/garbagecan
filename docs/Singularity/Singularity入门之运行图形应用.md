# Singularity入门之运行图形应用

要在 Singularity 中运行图形程序需要重新制作一个镜像，使其包含执行图形应用程序需要的环境和程序，这里还是通过 sandbox 的方式来制作一个可以运行图形程序镜像。

首先以 root 身份运行命令创建一个 centos 的 sandbox。

``` shell
$ sudo singularity -d build --sandbox centos/ docker://centos
```

如果已经下载过 centos.simg 镜像，也可以通过下面的命令将其转换为一个 sandbox。

``` shell
$ sudo singularity build --sandbox centos/ centos.simg
```

用 root 用户以可写的方式运行容器，并安装桌面环境

``` shell
$ sudo singularity shell --writable centos/

Singularity: Invoking an interactive shell within container...

Singularity centos:~> yum groupinstall -y "GNOME Desktop"
```

退出容器并制作成镜像文件

``` shell
$ sudo singularity build centos-gui.simg centos/
```

测试一下，启动一个gedit界面看看

``` shell
singularity exec centos/ gedit
```
