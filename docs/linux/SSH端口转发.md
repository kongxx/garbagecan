# SSH端口转发

今天碰到一个需求，在用户环境中，如果要访问机房里某个机器上，必须通过一个机器中转才可以，如果仅仅是SSH登录一下，我也就忍了，毕竟跳跳更健康嘛。但是今天要访问里面机器上的Web服务，我可以纠结了一会，后来想到了可以使用SSH的端口转发来实现。下面就看看怎么实现吧。

这里假定两台机器，host1 和 host2，其中 host1 是我的桌面机器能访问的，host2 是我的桌面机器不能之间访问的，必须通过 host1 跳转。今天测试我要在 host2 上启动一个Web服务，然后通过host1来访问这个Web服务。

## 方法一：使用本地端口转发

这里本地的意思是说我运行 ssh 命令是在跳转机器上，就是用自己机器的端口转发。

首先在 host2 上启动一个 Web 服务，这里使用 python 语言来简单创建一个 Web 服务使用 8000 端口。
``` shell
python -m SimpleHTTPServer
```

然后在 host1 上运行下面 ssh 命令来使用 host1 上的端口转发。
注意：这里的 ssh 命令的 -L 选项。
``` shell
ssh -v -NL 0.0.0.0:8000:host2:8000 host2
```

使用浏览器访问 http://host1:8000 来验证端口转发效果。

## 方法二：使用远端端口转发

这里远端的意思是说我运行 ssh 命令是在服务机器上，这样跳转机器相对来说就是远端了，就是使用远端机器的端口转发。

为了能通过远端端口转发，首先需要修改一下 host1 上的  /etc/ssh/sshd_config 文件
``` shell
AllowAgentForwarding yes
AllowTcpForwarding yes
GatewayPorts yes
```

然后重启 host1 的 sshd 服务
``` shell
sudo systemctl restart sshd
```

在 host2 上启动服务，并启动 ssh 端口转发。
注意：这里的 ssh 命令的 -R 选项。
``` shell
# 启动Web服务
python -m SimpleHTTPServer

# 使用8000端口转发
ssh -v -NR 0.0.0.0:8000:localhost:8000 root@host1

# 如果要使用动态端口，可以使用下面命令，命令输出中会打印动态分配的端口。
ssh -v -NR 0.0.0.0:0:localhost:8000 root@host1

# 也可以使用 -f 在后台运行
ssh -f -NR 0.0.0.0:0:localhost:8000 root@host1
```

使用浏览器访问 http://host1:8000 来验证端口转发效果。
