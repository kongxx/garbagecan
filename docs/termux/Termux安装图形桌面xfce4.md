# Termux安装图形桌面xfce4

参考前面几篇文章，安装 Termux 和 SSH 后，远程登录到 Termux 上，此时会提示

``` shell
$ ssh u0_a390@192.168.31.250 -p 8022
u0_a390@192.168.31.250's password: 

Welcome to Termux!

Community forum: https://termux.com/community
Gitter chat:     https://gitter.im/termux/termux
IRC channel:     #termux on libera.chat

Working with packages:

 * Search packages:   pkg search <query>
 * Install a package: pkg install <package>
 * Upgrade packages:  pkg upgrade

Subscribing to additional repositories:

 * Root:     pkg install root-repo
 * X11:      pkg install x11-repo

Report issues at https://termux.com/issues
```

其中提示可以通过“pkg install x11-repo” 安装图形包使用的源

``` shell
~ $ pkg install x11-repo
```

考虑到手机比较老（小米9），所以打算装一个轻量一点的图形桌面 xfce4，可以使用下面命令安装 xfce4 图像桌面，这一步安装的包比较多，可能会比较慢

``` shell
~ $ pkg install xfce4
```

装完桌面后，考虑到毕竟手机的屏幕比较小，直接从手机上用的可能性比较小，所以打算安装 vnc server，今后从电脑上连上使用，使用下面命令安装 vncserver

``` shell
~ $ pkg install tigervnc/x11
```

安装完 vnc server 后，先启动一下，这一步会提示设置密码，并且问是否使用 readonly 模式，我这里选择不使用 readonly 模式

``` shell
~ $ vncserver :9
```

首次启动后，先把服务停了，我们要改一下启动脚本，让 vnc server 使用 xfce4，可以通过下面命令停止 vnc server。

``` shell
~ $ vncserver -kill :9
```

修改配置文件 ~/.vnc/xstartup 文件，将最后一行由

``` shell
twm &
```
改成
``` shell
xfce4-session &
```

然后再次启动 vnc server

``` shell
~ $ vncserver :9
```

最后，找一个 VNC 客户端（我这里使用的是tightvnc），然后通过 <ip>:5909 就可以连接我们的 Termux 图形桌面了。
