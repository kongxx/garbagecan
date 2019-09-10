# Singularity入门之持久化Overlay

## 介绍

在 Singularity 中运行容器，当容器退出时，对容器做的修改均会丢失，那怎么保存对容器的修改呢？此时我们可以通过 Singularity 的 Overlay 实现。

Singularity 的 Overlay 其实就是一个目录或文件系统镜像，其中保存着对基于基础镜像的修改，可以使用 --overlay 选项来使用，且应用在下面几个容器命令上：
- run
- exec
- shell
- instance.start

## 使用

首先，先获取一个基础镜像，比如：centos，获取后会下载一个 centos.sif 文件。

``` shell
$ singularity pull centos
```

创建一个目录作为 Overlay 目录。

``` shell
$ mkdir my_overlay
```

然后使用 --overlay 选项运行容器，由于安全原因，使用 --overlay 必须要具有 root 权限，所以可以使用 sudo 来运行。

``` shell
$ sudo singularity shell --overlay my_overlay centos.sif

# 先在容器里检查是否有zip工具，默认容器会找不到命令。
Singularity centos.sif:/home/data/singularity-study> zip
bash: zip: command not found

# 安装一下zip包
Singularity centos.sif:/home/data/singularity-study> yum install -y zip
...

# 安装完检查一下zip工具安装路径
Singularity centos.sif:/home/data/singularity-study> whereis zip
zip: /usr/bin/zip
```

然后退出容器，并使用下面命令再次运行容器，就会发现刚才安装的工具已经在容器中了。

``` shell
$ sudo singularity shell --overlay my_overlay centos.sif

# 然后直接检查zip工具安装路径
Singularity centos.sif:/home/data/singularity-study> whereis zip
zip: /usr/bin/zip
```

如果我们使用下面的命令运行容器，就不会使用我们上面创建的 Overlay。

``` shell
$ sudo singularity shell centos.sif

# 容器会找不到zip命令
Singularity centos.sif:/home/data/singularity-study> zip
bash: zip: command not found
```

最后说一下，对于使用 Overlay 的持久化的容器，我们无法根据 Overlay 目录来创建镜像。比如：如果我使用下面的命令来创建镜像

``` shell
$ sudo singularity build my_overlay.sif my_overlay
INFO:    Starting build...
INFO:    Creating SIF file...
INFO:    Build complete: my_overlay.sif
```

创建镜像后，我们使用此镜像来创建容器，此时会提示运行报错。

``` shell
$ singularity shell my_overlay.sif
WARNING: passwd file doesn't exist in container, not updating
WARNING: group file doesn't exist in container, not updating
FATAL:   exec /.singularity.d/actions/shell failed: no such file or directory
```
