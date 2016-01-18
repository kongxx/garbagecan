# 使用 IBM Bluemix 容器构建 Django 环境

## 创建用户
首先需要在 IBM Bluemix 上创建一个用户
创建成功用户，就可以访问 IBM Bluemix 的仪表板了
https://console.ng.bluemix.net/

## 安装 CloudFoundry 命令行工具
可以从下面的链接下载安装包
https://github.com/cloudfoundry/cli#downloads
https://cli.run.pivotal.io/stable?release=redhat64&source=github

对于CentOS使用第二个链接，然后安装rpm包
``` bash
sudo yum install cf-cli-installer_6.14.0_x86-64.rpm
```

## 设置 namespace
要使用 IBM Bluemix 的容器，首先必须要创建一个namespace来作为用户的容器仓库
``` bash
cf ic namespace set [my_namespace]
cf ic namespace get
```

也可以在 仪表板 页面创建，在仪表板上选择“容器”，然后选择右侧一个容器映像就会弹出一个窗口让输入namespace

## 用cf命令行登录
``` bash
cf login -a https://api.ng.bluemix.net
```

## 初始化
``` bash
cf ic init
```

初始化之后，可以使用 cf 命令查看一些信息，比如：
查看镜像列表
``` bash
cf ic images
REPOSITORY                                        TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
registry.ng.bluemix.net/ibm-mobilefirst-starter   latest              5996bb6e51a1        6 weeks ago         770.4 MB
registry.ng.bluemix.net/ibm-node-strong-pm        latest              ef21e9d1656c        8 weeks ago         528.7 MB
registry.ng.bluemix.net/ibmliberty                latest              2209a9732f35        8 weeks ago         492.8 MB
registry.ng.bluemix.net/ibmnode                   latest              8f962f6afc9a        8 weeks ago         429 MB
```

查看IP列表，这里 Bluemix 会默认分配两个公网IP，因为还没有创建容器所有这个列表可能为空，等后面创建了容器之后就会看到有容器绑定了公网IP
``` bash
cf ic ip list
```

## 创建容器镜像Dockerfile
建一个目录，比如mytest，然后在其下创建一个Dockerfile，内容如下
``` bash
===========================================================
FROM centos:centos7

MAINTAINER Fanbin Kong "kongxx@hotmail.com"

RUN rpm -ivh http://dl.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-5.noarch.rpm
RUN yum install -y openssh-server sudo python-pip

RUN pip install django 
RUN pip install djangorestframework

RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config

RUN echo "root:Letmein" | chpasswd

RUN ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key
RUN mkdir /var/run/sshd

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
```
## 创建容器镜像
``` bash
cf ic build -t mytest:v1 .
```
创建完成后再使用 "cf ic images" 查看，就会看到新创建的mytest:v1镜像
``` bash
cf ic images
REPOSITORY                                        TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
registry.ng.bluemix.net/kongxx/mytest             v1                  63f1d401ab8c        9 minutes ago       340.8 MB
registry.ng.bluemix.net/kongxx/mytest             latest              63f1d401ab8c        17 minutes ago      340.8 MB
registry.ng.bluemix.net/ibm-mobilefirst-starter   latest              5996bb6e51a1        6 weeks ago         770.4 MB
registry.ng.bluemix.net/ibm-node-strong-pm        latest              ef21e9d1656c        8 weeks ago         528.7 MB
registry.ng.bluemix.net/ibmliberty                latest              2209a9732f35        8 weeks ago         492.8 MB
registry.ng.bluemix.net/ibmnode                   latest              8f962f6afc9a        8 weeks ago         429 MB
```

## 启动容器
``` bash
cf ic run --name=test -d -P registry.ng.bluemix.net/[namespace]/mytest:v1
```

启动后，稍等片刻，然后运行 "cf ic inspect test | grep Ip" 来查看IP地址。如果看到是空，就再等等再运行。
如果看到IP了，此时可以使用 "cf ic ip list" 来查看ip和容器的绑定关系，如果没有绑定成功，可以使用下面的命令来绑定
``` bash
cf ic ip {list,bind,unbind,request,release} [IP_ADDRESS] [CONTAINER_INSTANCE_ID]
```

容器的启动也可以在Bluemix的仪表板上来操作，还有就是也可以给容器绑定IP地址，因为都是图形操作这里就不说了。

## 访问容器
在上一步启动容器后并使用 "cf ic inspect test | grep Ip" 获取IP地址后，我们就可以ssh登录了。上面容器创建的时候使用的是root/Letmein作为用户名和密码。

## 测试Django
运行下面的命令来创建并启动Django应用
``` bash
django-admin.py startproject myapp
cd myapp
python manage.py runserver 0.0.0.0:8000
```

在浏览器访问 http://[ip]:8000/ ，即可看到Django的初始页面了。
