# 使用Supervisor管理Linux进程

## 简介
Supervisor是一个C/S系统,它可以在类UNIX系统上控制系统进程，由python编写，提供了大量的功能来实现对进程的管理。

## 安装
``` bash
sudo pip install supervisor
```

## 配置
安装完成 supervisor 之后，可以使用 “echo_supervisord_conf” 命令来生成样例配置文件
``` bash
echo_supervisord_conf
```

默认 supervisor 会使用 /etc/supervisord.conf 作为默认配置文件。

## 启动服务
### 服务程序
首先写个小程序来模拟一个服务程序，如下
myserver.sh
``` bash
#!/bin/sh

while true
do
    date 
    sleep 5
done
```

### 配置
修改配置文件 /etc/supervisord.conf ，内容如下
``` bash
[supervisord]
nodaemon=true

[program:myserver]
command=/home/kongxx/test/myserver.sh
```

### 启动服务
``` bash
supervisord -c /etc/supervisord.conf
```
运行上面的程序即可启动supervisor服务，此时会在当前目录下生成一个日志文件 supervisord.log。

此时我们使用 “ps -ef | grep myserver” 找到上面的服务进程，然后kill掉这个进程。此时就会看到日志中 supervisor 会启动一个新的myserver进程。

## 管理服务
对于上面的例子我们只能启动一个服务，却不能管理这些配置的服务，下面就看看怎样管理服务。
### 服务程序
还是使用上面myserver.sh程序。

### 配置
/etc/supervisord.conf 
``` python
[inet_http_server]         ; inet (TCP) server disabled by default
port = *:9999              ; (ip_address:port specifier, *:port for all iface)
username = admin           ; (default is no username (open server))
password = Letmein         ; (default is no password (open server))

[supervisord]
nodaemon = false

[rpcinterface:supervisor]
supervisor.rpcinterface_factory = supervisor.rpcinterface:make_main_rpcinterface

[supervisorctl]
serverurl = http://127.0.0.1:9999 ; use an http:// url to specify an inet socket
username = admin              ; should be same as http_username if set
password = Letmein            ; should be same as http_password if set
prompt = mysupervisor         ; cmd line prompt (default "supervisor")

[program:myserver]
command = /home/kongxx/test/myserver.sh
redirect_stderr = true
stdout_logfile = /tmp/myserver.log

```

### 启动服务
``` bash
supervisord -c /etc/supervisord.conf
```

### 查询/启动/停止服务
``` bash
$ supervisorctl status myserver
myserver                         RUNNING   pid 14034, uptime 0:00:03 

$ supervisorctl start myserver
$ supervisorctl stop myserver
```

### supervisor 管理命令行
supervisorctl也可以不带任何参数，此时即可进入supervisor的管理命令行接口，如下：
``` bash
$ supervisorctl 
myserver                         RUNNING   pid 15297, uptime 0:00:27
mysupervisor> ?

default commands (type help <topic>):
=====================================
add    exit      open  reload  restart   start   tail   
avail  fg        pid   remove  shutdown  status  update 
clear  maintail  quit  reread  signal    stop    version

mysupervisor> 
```

### 远程管理
``` bash
supervisorctl -s http://<ip>:9999 -u admin -p Letmein status myserver
```

### Web接口
可以使用浏览器访问 http://<ip>:9999 来通过web接口管理服务。
