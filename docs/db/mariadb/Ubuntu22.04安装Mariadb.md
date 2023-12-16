# Ubuntu22.04安装Mariadb

## 安装Mariadb

``` shell
$ sudo apt update
$ sudo apt install mariadb-server
```

安装完后，可以用下面命令查看服务状态

``` shell
$ sudo service mariadb status
```

## 配置Mariadb

安装完mariadb后，可以使用mysql_secure_installation命令做一下安全配置

``` shell
$ sudo mysql_secure_installation

NOTE: RUNNING ALL PARTS OF THIS SCRIPT IS RECOMMENDED FOR ALL MariaDB
      SERVERS IN PRODUCTION USE!  PLEASE READ EACH STEP CAREFULLY!

In order to log into MariaDB to secure it, we'll need the current
password for the root user. If you've just installed MariaDB, and
haven't set the root password yet, you should just press enter here.

Enter current password for root (enter for none):                     <--- 默认是空，我这里直接回车
OK, successfully used password, moving on...

Setting the root password or using the unix_socket ensures that nobody
can log into the MariaDB root user without the proper authorisation.

You already have your root account protected, so you can safely answer 'n'.

Switch to unix_socket authentication [Y/n] n
 ... skipping.

You already have your root account protected, so you can safely answer 'n'.

Change the root password? [Y/n] n
 ... skipping.

By default, a MariaDB installation has an anonymous user, allowing anyone
to log into MariaDB without having to have a user account created for
them.  This is intended only for testing, and to make the installation
go a bit smoother.  You should remove them before moving into a
production environment.

Remove anonymous users? [Y/n] y
 ... Success!

Normally, root should only be allowed to connect from 'localhost'.  This
ensures that someone cannot guess at the root password from the network.

Disallow root login remotely? [Y/n] y
 ... Success!

By default, MariaDB comes with a database named 'test' that anyone can
access.  This is also intended only for testing, and should be removed
before moving into a production environment.

Remove test database and access to it? [Y/n] n
 ... skipping.

Reloading the privilege tables will ensure that all changes made so far
will take effect immediately.

Reload privilege tables now? [Y/n] y
 ... Success!

Cleaning up...

All done!  If you've completed all of the above steps, your MariaDB
installation should now be secure.

Thanks for using MariaDB!
```

## 验证

配置完成后，可以登录验证一下。

``` shell
$ sudo mysql
Welcome to the MariaDB monitor.  Commands end with ; or \g.
Your MariaDB connection id is 217
Server version: 10.6.12-MariaDB-0ubuntu0.22.04.1 Ubuntu 22.04

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]> 
```

## 创建用户

``` shell
$ sudo mysql
... 
MariaDB [(none)]> GRANT ALL ON *.* TO 'admin'@'localhost' IDENTIFIED BY '<password>' WITH GRANT OPTION;
MariaDB [(none)]> GRANT ALL ON *.* TO 'admin'@'%' IDENTIFIED BY '<password>' WITH GRANT OPTION;
MariaDB [(none)]> FLUSH PRIVILEGES;
```

使用新用户登录

``` shell
$ mysql -u admin -p<password>
```

## 配置允许远程登录

用户创建后，发现不能从远程登录数据库，此时可以修改配置文件 /etc/mysql/my.cnf (可以使用命令 mariadbd --help --verbose | grep my.cnf 查看配置文件位置)，在最后添加以下内容（如果已经存在，修改之）

``` shell
[mysqld]
skip-networking=0
skip-bind-address
```

保存后，重新启动服务
``` shell
$ sudo service mariadb restart
```

从远程访问数据库
``` shell
$ mysql -h <host> -u admin -p<password>
```
