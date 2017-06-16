# 使用Sinopia搭建私有npm仓库

在用npm装包的时候，每次都要下载一大堆，慢且不说，npm还老被墙，所以就想到在公司内部搭建npm仓库镜像。大概看了几个，觉得Sinopia最简单也好用，所以就使用Sinopia搭建仓库吧。

## 安装
``` shell
sudo npm install -g sinopia
```

## 配置与运行

安装完成后，暂时不知道配置文件在哪里，可以先运行一下 sinopia，比如：
``` shell
$ sinopia
 warn  --- config file  - /home/<user>/.config/sinopia/config.yaml
 warn  --- http address - http://localhost:4873/
```

从上面命令输出可以看到配置文件路径：/home/<user>/.config/sinopia/config.yaml

下面修改配置文件，在最后加上一行 “listen: 0.0.0.0:4873”，目的是为了可以从别的机器上也能访问 sinopia 仓库。

``` shell
#
# This is the default config file. It allows all users to do anything,
# so don't use it on production systems.
#
# Look here for more config file examples:
# https://github.com/rlidwka/sinopia/tree/master/conf
#

# path to a directory with all packages
storage: /home/jhadmin/.local/share/sinopia/storage

auth:
  htpasswd:
    file: ./htpasswd
    # Maximum amount of users allowed to register, defaults to "+inf".
    # You can set this to -1 to disable registration.
    #max_users: 1000

# a list of other known repositories we can talk to
uplinks:
  npmjs:
    url: https://registry.npmjs.org/

packages:
  '@*/*':
    # scoped packages
    access: $all
    publish: $authenticated

  '*':
    # allow all users (including non-authenticated users) to read and
    # publish all packages
    #
    # you can specify usernames/groupnames (depending on your auth plugin)
    # and three keywords: "$all", "$anonymous", "$authenticated"
    access: $all

    # allow all known users to publish packages
    # (anyone can register by default, remember?)
    publish: $authenticated

    # if package is not available locally, proxy requests to 'npmjs' registry
    proxy: npmjs

# log settings
logs:
  - {type: stdout, format: pretty, level: info}
  #- {type: file, path: sinopia.log, level: info}

listen: 0.0.0.0:4873
```

修改完成后再次启动 Sinopia 服务，如下：
``` shell
$ sinopia
 warn  --- config file  - /home/<user>/.config/sinopia/config.yaml
 warn  --- http address - http://0.0.0.0:4873/
```

## 使用

私有仓库已经有了，下面来看看怎么使用，这里我们使用了nrm来管理私有仓库。（可以参考我的前一篇博客）

### 添加私有仓库

``` shell
$ nrm add mynpm http://192.168.0.123:4873
```

### 使用私有仓库
``` shell
$ nrm use mynpm
```

### 测试私有仓库
``` shell
$ mkdir test
$ cd test
$ npm install webpack # 第一次安装比较慢
...

$ rm -rf webpack
$ npm install webpack # 第二次安装就比较快了
...
```
