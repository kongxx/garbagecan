# Ubuntu16.04的Python virtualenv问题

## 问题描述

最新release的Ubuntu16.04想让大家都把Python环境都迁移到Python3上，但是目前python2还是主流，还有很多第三方库是基于Python2来实现的，所以我想还是有很多人和我一样，开发和生产环境必须要使用Python2。

而在系统上安装virtualenv基本是每个开发人员都必须的，但是这次在Ubuntu16.04上安装了virtualenv后却碰到了很多奇奇怪怪问题。

## 问题解决办法

1. 首先检查python环境，Ubuntu16.04默认使用的Python2.7.11

2. 然后仔细查了一下Ubuntu16.04安装包，发现Ubuntu16.04带了两个版本的的virtualenv：
- python-virtualenv：Python2的virtualenv。
- virtualenv 和 python3-virtualenv：Python3的virtualenv，这两个包会一起被安置。

而python-virtualenv和python3-virtualenv是互相冲突的，也就是说同时只能装其中一个。

3. 重新安装python-virtualenv包，如果检查已经安装了virtualenv 和 python3-virtualenv，那么先卸载这两个包，然后安装。

``` shell
sudo apt-get install python-virtualenv
```

4. 创建virtual env
安装完 python-virtualenv 之后，我发现没有可以直接运行virtualenv的文件，也就是不能直接在命令行输入“virtualenv”来创建虚拟环境。

最后找了个办法，可以使用下面的命令来创建Python2的virtual env
```shell
python -m virtualenv myenv
```

## 参考
- https://bugs.launchpad.net/ubuntu/+source/python-virtualenv/+bug/1550923


