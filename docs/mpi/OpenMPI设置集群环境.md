# OpenMPI设置集群环境


## 创建Docker容器

### Dockerfile

``` shell
FROM gpmidi/centos-6.5

MAINTAINER Fanbin Kong "kongxx@hotmail.com"

RUN yum install -y openssh-server sudo passwd tar
RUN yum groupinstall -y 'Development tools'

RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config

RUN echo "root:Letmein" | chpasswd
RUN useradd jhadmin  
RUN echo "jhadmin:jhadmin" | chpasswd  
RUN echo "jhadmin   ALL=(ALL)       NOPASSWD: ALL" >> /etc/sudoers  

RUN ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key
RUN mkdir /var/run/sshd

EXPOSE 22  
CMD ["/usr/sbin/sshd", "-D"]

```

### 生成镜像

``` shell
$ sudo docker build -t mpitest:v1 .
```

### 创建容器实例

``` shell
$ sudo docker run --name=test -h test -d -P mpitest:v1
```



## 安装准备

首先准备两个机器，比如 host1 和 host2，设置这两个机器可以互相免密钥登录（[Linux SSH 免密码登录](http://blog.csdn.net/kongxx/article/details/47046817)）

修改两个机器的/etc/hosts文件，加入两个机器的信息，比如：
``` shell
172.17.0.2	test1
172.17.0.3	test2

```

## 安装openmpi

### 下载

``` shell
$ wget -c https://www.open-mpi.org/software/ompi/v1.10/downloads/openmpi-1.10.3.tar.gz
```

### 安装
``` shell
$ tar zxvf openmpi-1.10.3.tar.gz
$ cd openmpi-1.10.3
$ ./configure --prefix=/opt/openmpi
$ make
$ sudo make install
```

### 设置环境PATH和LD_LIBRARY_PATH

手动运行下面的命令

``` shell
$ export PATH=$PATH:/opt/openmpi/bin
$ export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/opt/openmpi/lib/
```

并且将其写入~/.bashrc文件中，这样mpiexec在远程机器上运行的时候就会自动source环境了。

``` shell
PATH=$PATH:/opt/openmpi/bin
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/opt/openmpi/lib/
export PATH LD_LIBRARY_PATH
```

### 测试

``` shell
$ cd examples     （源代码目录）

$ make

$ mpirun -np 10 hello_c
$ mpirun -np 10 ring_c

$ mpirun -np 3 printenv
```

## 在集群中运行mpi作业

首先创建一个集群机器列表，并指定每个机器的slots数，比如文件名hostfile，内如如下：

``` shell
test1    slots=2
test2    slots=2
```

运行mpi作业

``` shell
$ mpiexec --hostfile hosts -np 4 hello_c
Hello, world, I am 1 of 4, (Open MPI v1.10.3, package: Open MPI jhadmin@test1 Distribution, ident: 1.10.3, repo rev: v1.10.2-251-g9acf492, Jun 14, 2016, 124)
Hello, world, I am 0 of 4, (Open MPI v1.10.3, package: Open MPI jhadmin@test1 Distribution, ident: 1.10.3, repo rev: v1.10.2-251-g9acf492, Jun 14, 2016, 124)
Hello, world, I am 2 of 4, (Open MPI v1.10.3, package: Open MPI jhadmin@test2 Distribution, ident: 1.10.3, repo rev: v1.10.2-251-g9acf492, Jun 14, 2016, 124)
Hello, world, I am 3 of 4, (Open MPI v1.10.3, package: Open MPI jhadmin@test2 Distribution, ident: 1.10.3, repo rev: v1.10.2-251-g9acf492, Jun 14, 2016, 124)
```

## 问题及说明

如果在运行命令“mpiexec --hostfile hosts -np 4 hello_c”出现下面错误的时候
``` shell
bash: orted: command not found
--------------------------------------------------------------------------
ORTE was unable to reliably start one or more daemons.
This usually is caused by:

* not finding the required libraries and/or binaries on
  one or more nodes. Please check your PATH and LD_LIBRARY_PATH
  settings, or configure OMPI with --enable-orterun-prefix-by-default

* lack of authority to execute on one or more specified nodes.
  Please verify your allocation and authorities.

* the inability to write startup files into /tmp (--tmpdir/orte_tmpdir_base).
  Please check with your sys admin to determine the correct location to use.

*  compilation of the orted with dynamic libraries when static are required
  (e.g., on Cray). Please check your configure cmd line and consider using
  one of the contrib/platform definitions for your system type.

* an inability to create a connection back to mpirun due to a
  lack of common network interfaces and/or no route found between
  them. Please check network connectivity (including firewalls
  and network routing requirements).
--------------------------------------------------------------------------
```

可以使用下面命令来查看是不是环境变量没有设置对，开始我就是环境变量 PATH 和 LD_LIBRARY_PATH 设置有问题，才出现上面的错误。

``` shell
mpiexec --hostfile hosts -np 4 printenv
```

检查~/.bashrc文件，指定正确的路径即可。
