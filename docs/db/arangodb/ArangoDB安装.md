# ArangoDB安装

ArangoDB的安装还是挺简单的，官方给了各个平台的安装包，下面是在CentOS8上面安装的步骤。

从官方网站下载了两个 rpm 包
- arangodb3-3.6.0-1.0.x86_64.rpm
- arangodb3-client-3.6.0-1.0.x86_64.rpm

## 安装

``` shell
$ sudo yum install arangodb3-3.6.0-1.0.x86_64.rpm

...
ArangoDB 3 (https://www.arangodb.com)
  The multi-model NoSQL database: distributed free and open-source database
  with a flexible data model for documents, graphs, and key-values. Build
  high performance applications using a convenient SQL-like query language
  or JavaScript extensions.

First Steps with ArangoDB:
  https://www.arangodb.com/docs/stable/getting-started.html

Configuring the storage Engine:
  https://www.arangodb.com/docs/stable/programs-arangod-server.html#storage-engine

Configuration file:
  /etc/arangodb3/arangod.conf

Start ArangoDB shell client:
  > /usr/bin/arangosh

Start ArangoDB service:
  > systemctl start arangodb3.service

Enable ArangoDB service:
  > systemctl enable arangodb3.service

SECURITY HINT:
run 'arango-secure-installation' to set a root password
the current password is '1453d989470216540f049c979021257e'
(You should do this for a FRESH install! For an UPGRADE the password does not need to be changed)
...
```

这里需要特别注意一下安装过程的输出，打印了arangodb配置文件，shell客户端，还有启动服务的命令。其中特别注意一下安全部分，安装过程会为arangodb的root用户设置一个默认的密码。后面需要用 arango-secure-installation 命令重新设置一下。

## 设置root密码

``` shell
$ sudo arango-secure-installation
```

## 启动服务

``` shell
# 查看服务状态
$ sudo systemctl status arangodb3.service

# 启动服务
$ sudo systemctl start arangodb3.service
```

## 验证服务

启动服务后，可以通过 Web 管理界面来验证一下，比如访问 http://localhost:8529/ 地址，登录后可以监控和管理 arangodb 数据库。

也可以使用 arangosh 命令来从命令行登录 arangodb 系统，用户使用root用户，密码使用上面新设置的密码。
