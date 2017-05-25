使用 nvm 管理多版本 node

首先，使用下面的命令来安装 nvm

``` shell
$ curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.2/install.sh | bash
```

下载并安装完成后用户退出重新登录或者重新 source 一下环境变量

``` shell
$ . ~/.bashrc
```

查看当前系统 node 版本

``` shell
$ nvm ls
            N/A
node -> stable (-> N/A) (default)
iojs -> N/A (default)
```

可以看到目前系统里没有安装任何版本的 node

使用 “nvm install <version>” 安装指定版本的 node

``` shell
$ nvm install v6.10.0
Downloading and installing node v6.10.0...
Downloading https://nodejs.org/dist/v6.10.0/node-v6.10.0-linux-x64.tar.gz...
######################################################################## 100.0%
Computing checksum with sha256sum
Checksums matched!
Now using node v6.10.0 (npm v3.10.10)
Creating default alias: default -> v6.10.0

$ nvm install v7.10.0
Downloading and installing node v7.10.0...
Downloading https://nodejs.org/dist/v7.10.0/node-v7.10.0-linux-x64.tar.gz...
######################################################################## 100.0%
Computing checksum with sha256sum
Checksums matched!
Now using node v7.10.0 (npm v4.2.0)
```

再次查看 node 版本信息

``` shell
$ nvm list
        v6.10.0
->      v7.10.0
default -> v6.10.0
node -> stable (-> v7.10.0) (default)
stable -> 7.10 (-> v7.10.0) (default)
iojs -> N/A (default)
lts/* -> lts/boron (-> N/A)
lts/argon -> v4.8.3 (-> N/A)
lts/boron -> v6.10.3 (-> N/A)
```

现在已经有两个版本的 node 了，其中 “->      v7.10.0” 说明当前使用的 v7.10.0 版本。

``` shell
$ node -v
v7.10.0

$ nvm current
v7.10.0
```

现在使用 “nvm use <version>” 可以切换 node 版本，比如：

``` shell
$ nvm use v6.10.0
Now using node v6.10.0 (npm v3.10.10)
```

切换后，查看 node 版本信息 “->      v6.10.0” 说明当前使用的 v6.10.0 版本了。

``` shell
$ nvm list
->      v6.10.0
        v7.10.0
default -> v6.10.0
node -> stable (-> v7.10.0) (default)
stable -> 7.10 (-> v7.10.0) (default)
iojs -> N/A (default)
lts/* -> lts/boron (-> N/A)
lts/argon -> v4.8.3 (-> N/A)
lts/boron -> v6.10.3 (-> N/A)
```

``` shell
$ node -v
v6.10.0

$ nvm current
v6.10.0
```

最后，如果要设置系统用户默认的 node 版本，可以使用 “nvm alias default <version>” 来设置。

``` shell
$ nvm alias default v7.10.0
default -> v7.10.0

$ nvm ls
->      v6.10.0
        v7.10.0
default -> v7.10.0
node -> stable (-> v7.10.0) (default)
stable -> 7.10 (-> v7.10.0) (default)
iojs -> N/A (default)
lts/* -> lts/boron (-> N/A)
lts/argon -> v4.8.3 (-> N/A)
lts/boron -> v6.10.3 (-> N/A)
```
