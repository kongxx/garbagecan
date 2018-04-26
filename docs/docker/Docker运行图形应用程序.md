# Docker运行图形应用程序

今天要用Docker在容器里运行一个带图形界面的应用程序，所有简单试了一下，还是比较容易实现，下面是我整理的步骤。

## Dockerfile

首先创建一个Dockerfile，内容如下：

``` shell
FROM centos:7

RUN yum install -y sudo tar wget openssh-server openssh-clients openssl openssl-devel epel-release
RUN yum install -y gedit

#RUN yum groupinstall -y "GNOME Desktop"
#RUN yum groupinstall -y "Development and Creative Workstation"

RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config \
    && echo 'root:admin' | chpasswd \
    && useradd -u 1000 admin \
    && echo "admin:admin" | chpasswd \
    && echo "admin   ALL=(ALL)       NOPASSWD: ALL" >> /etc/sudoers \
    && ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key \
    && ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key \
    && mkdir /var/run/sshd

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
```

主要就是安装了一些第三方包，这里是使用的gedit来做的测试，如果要使用别的程序，可以适当的修改一下。或者如果网速很快的话，可以直接安装"GNOME Desktop"（这个会安装很多包，懒人专用）。

另外就是最后启动了一个sshd服务，目的是为了我测试方便，可以换成自己封装的应用程序。

## 创建镜像

``` shell
$ sudo docker build --rm -t docker-gui .
```

## 设置xhost

为了能让容器里的应用程序投送到当前的窗口上，需要运行下面命令来允许任意的客户端访问。

``` shell
$ xhost +
access control disabled, clients can connect from any host
```

## 运行应用程序


``` shell
sudo docker run -it --rm -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix docker-gui gedit

```