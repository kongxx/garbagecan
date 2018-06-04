# 使用chroot和supermin5创建安全隔离的环境

## chroot

chroot 顾名思义就是 change root directory。在 linux 系统中，系统默认的根路径是 "/"。而在使用 chroot 之后，系统的目录结构将以指定的目录作为 "/"。

使用chroot后可以创建一个完全隔离的环境，方便用户在完全隔离的环境下的开发运行。

在运行 chroot 之后，用户就进入了新的根目录下，并且所有的操作都是基于新的根目录来操作，用户根本访问不到原来的系统根目录。

因此，我们可以设置用户登录前使用chroot，这样就可以限制用户只能在一定的目录下操作，避免用户访问一些系统的文件。

## supermin5

Supermin 是一个用来创建迷你虚拟环境的工具，有点类似创建的迷你虚拟机环境。

如果机器上没有 supermin5 命令，可以使用下面的命令安装

``` shell
sudo yum install -y supermin*
```

## 使用supermin5创建虚拟环境

注：以下命令需要使用root用户来执行

``` shell
# mkdir myenv
# cd myenv

# 下面命令是准备要安装的软件包并保存在supermin.d目录下
# supermin5 -v --prepare bash coreutils iputils tar wget git vim yum python python2-pip -o supermin.d

# 下面命令是根据上一步准备的安装包目录制作隔离的环境，并保存在appliance.d目录下
# supermin5 -v --build --format chroot supermin.d -o appliance.d

# 如果要上网执行下面命令
# cp /etc/resolv.conf appliance.d/etc/

# 如果要使用yum安装软件包 （supermin5 --build的时候需要加上--use-installed）
# echo 7 > appliance.d/etc/yum/vars/releasever
```

## 使用chroot切换根目录

注：以下命令需要使用root用户来执行

``` shell
# cd appliance.d/
# chroot .      <<< 切换根目录

# 以下操作均是在新的根目录下执行了

bash-4.2# ls    <<<
bin   dev  home  lib64	mnt  proc  run	 srv  tmp  var
boot  etc  lib	 media	opt  root  sbin  sys  usr
bash-4.2# python
Python 2.7.5 (default, Apr 11 2018, 07:36:10)
[GCC 4.8.5 20150623 (Red Hat 4.8.5-28)] on linux2
Type "help", "copyright", "credits" or "license" for more information.
>>>
...
```
