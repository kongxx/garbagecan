# Singularity入门之通过沙盒目录创建镜像

## 准备镜像目录

通过 “--sandbox” 选项指定一个目录来作为镜像运行目录。

``` shell
$ sudo singularity -d build --sandbox ubuntu/ docker://ubuntu
```

## 运行并修改容器

在此镜像目录之上运行容器，这里需要使用 “--writable” 选项来使其可写。

``` shell
$ sudo singularity shell --writable ubuntu/

# 在容器中运行 apt 安装程序来安装一些软件包，比如：安装vim

Singularity ubuntu:~> apt-get update
...

Singularity ubuntu:~> apt-get install vim
...
```

## 制作镜像

退出容器，然后使用 build 命令来创建新的容器镜像。

``` shell
$ sudo singularity build ubuntu-vim.simg ubuntu/
```

## 测试

``` shell
$ sudo singularity shell ubuntu-vim.simg

Singularity ubuntu-vim.simg:~> vim （运行vim检查镜像中是否包含安装的软件）
```
