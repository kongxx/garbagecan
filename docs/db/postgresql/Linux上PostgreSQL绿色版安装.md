# Linux上PostgreSQL绿色版安装

## 下载
首先从下面的链接地址下载对应平台和版本的安装包。
> https://www.enterprisedb.com/download-postgresql-binaries 

## 安装
``` shell
# 以普通用户运行下面命令

# 解压压缩包到/opt目录下，如果对/opt目录没有写权限，先增加写权限再运行下面命令。
$ tar zxvf postgresql-9.6.10-1-linux-x64-binaries.tar.gz

# 创建数据目录
$ cd /opt/pgsql
$ mkdir data

# 设置环境变量，考虑到机器上有其它版本的postgresql在运行，所以端口使用了54321以避免冲突。
$ export PATH=/opt/pgsql/bin:$PATH
$ export PGDATA=/opt/pgsql/data
$ export PGUSER=kongxx
$ export PGPORT=54321
```

## 初始化
``` shell
$ ./bin/initdb -D ./data -E UTF8 
```

## 配置

编辑 data/postgresql.conf 文件
``` shell
将
listen_addresses = 'localhost'
改为
listen_addresses = '*'
```

编辑 data/pg_hba.conf 文件
``` shell
在 '# IPv4 local connections' 中增加一行
host    all             all             0.0.0.0/0            trust
```

## 启动服务
``` shell
$ ./bin/pg_ctl -D ./data -l logfile start
```

## 测试
``` shell
$ ./bin/psql -d postgres -p 54321
```