# 使用nrm管理npm仓库

用npm装包的时候，经常碰到太慢或者npm官网被墙的情况，有时候凑合一下就改一下 "~/.npmrc" 文件，但是经常改来改去也挺麻烦的，于是找到了可以使用nrm来管理npm仓库。

## 安装

``` shell
sudo npm install -g nrm
```

## 查询仓库

使用 “nrm ls” 查看所有仓库
``` shell
$ nrm ls

* npm ---- https://registry.npmjs.org/
  cnpm --- http://r.cnpmjs.org/
  taobao - https://registry.npm.taobao.org/
  nj ----- https://registry.nodejitsu.com/
  rednpm - http://registry.mirror.cqupt.edu.cn/
  npmMirror  https://skimdb.npmjs.com/registry/
  edunpm - http://registry.enpmjs.org/
```

其中带 “*” 的表示当前正在使用的仓库。

## 添加删除仓库

使用 add 或 del 可以添加或删除仓库。比如：
``` shell
$ nrm add myrepos ...
$ nrm del myrepos
```

## 切换仓库

使用 use 来选择当前使用那个仓库。比如：
``` shell
$ nrm use taobao
...

$ nrm ls

  npm ---- https://registry.npmjs.org/
  cnpm --- http://r.cnpmjs.org/
* taobao - https://registry.npm.taobao.org/
  nj ----- https://registry.nodejitsu.com/
  rednpm - http://registry.mirror.cqupt.edu.cn/
  npmMirror  https://skimdb.npmjs.com/registry/
  edunpm - http://registry.enpmjs.org
```
