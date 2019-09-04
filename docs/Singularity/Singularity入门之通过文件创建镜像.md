# Singularity入门之通过recipe文件创建镜像

Singularity中也提供了类似Docker中通过Dockerfile来创建镜像的功能，我们可以把创建镜像的内容写到Singularity文件中，然后使用Singularity这个文件来构建镜像。

## 创建Singularity文件

创建一个Singularity文件，内容如下：

``` shell
Bootstrap: docker
From: ubuntu

%post
    apt-get -y update
    apt-get -y install vim sudo

%environment
    export AAA=aaa
    export BBB=bbb
    export CCC=ccc

%runscript
    echo "Hello World"
```

其中 Bootstrap 可以是 shub (images hosted on Singularity Hub)，docker (images hosted on Docker Hub)，localimage (images saved on your machine)， yum (yum based systems such as CentOS and Scientific Linux)等。

## 构建镜像

``` shell
$ sudo singularity build ubuntu-test.simg Singularity
```

## 运行容器

``` shell
$ sudo singularity run ubuntu-test.simg
Hello World
```

## 参考

- http://singularity.lbl.gov/docs-recipes
