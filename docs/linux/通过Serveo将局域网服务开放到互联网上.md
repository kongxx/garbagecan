# 通过 Serveo 将局域网服务开放到互联网上

发现一个好东西 Serveo，可以将局域网内的服务端口开放到互联网上，从而实现通过外网访问局域网。最主要这东西还不要安装，你说要命不。下面就看看怎么使用。

这里先假定，我在局域网内启动了一个 web server，使用 8080 端口。

## 开放本地服务

在命令行使用下面命令来吧服务开放到互联网上。

``` shell
$ ssh -R 80:localhost:8080 serveo.net
Hi there
Forwarding HTTP traffic from https://ferrum.serveo.net
Press g to start a GUI session and ctrl-c to quit.
...
```

然后，通过浏览器访问 https://ferrum.serveo.net，就可以访问局域网内的服务了。

上面的命令其实就是通过 ssh 命令端口映射，将本地 8080 端口映射到 serveo.net:80 上。其中 “ferrum” 是随机产生的一个二级域名。

我们也可以制定自己想使用的二级域名，比如：

``` shell
$ ssh -R abc:80:localhost:8080 serveo.net
Hi there
Forwarding HTTP traffic from https://abc.serveo.net
Press g to start a GUI session and ctrl-c to quit.
...
```

## SSH 重定向

使用下面的命令来开启 SSH 重定向功能。

``` shell
$ ssh -R myhost:22:localhost:22 serveo.net
Hi there
Forwarding SSH traffic from alias "myhost"
Press g to start a GUI session and ctrl-c to quit.
...
```

然后，就可以从别的机器上使用下面命令进行 SSH 访问了。

``` shell
$ ssh -J serveo.net <myuser>@<myhost>
Hi there
myuser@myhost's password:
Last login: Mon Dec 24 21:00:32 2018 from 127.0.0.1
...
```

## 参考

> https://serveo.net