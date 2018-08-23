# Linux下启动Oracle服务

在 Linux下启动Oracle服务需要下面两步：

- 启动lsnrctl监听。
- 启动数据库实例。

## 启动lsnrctl监听

### 首先以oracle用户登录系统

### 进入数据库目录

``` shell
$ cd /home/oracle/app/oracle/product/12.2.0/dbhome_1/bin
```

### 查看监听状态

``` shell

$ ./lsnrctl status

LSNRCTL for Linux: Version 12.2.0.1.0 - Production on 22-AUG-2018 20:28:21

Copyright (c) 1991, 2016, Oracle.  All rights reserved.

Connecting to (DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=wl1)(PORT=1521)))
TNS-12541: TNS:no listener
 TNS-12560: TNS:protocol adapter error
  TNS-00511: No listener
   Linux Error: 111: Connection refused
Connecting to (DESCRIPTION=(ADDRESS=(PROTOCOL=IPC)(KEY=EXTPROC1521)))
TNS-12541: TNS:no listener
 TNS-12560: TNS:protocol adapter error
  TNS-00511: No listener
   Linux Error: 111: Connection refused
```

### 启动监听

``` shell
# 启动监听
$ ./lsnrctl start

LSNRCTL for Linux: Version 12.2.0.1.0 - Production on 22-AUG-2018 20:28:27

Copyright (c) 1991, 2016, Oracle.  All rights reserved.

Starting /home/oracle/app/oracle/product/12.2.0/dbhome_1/bin/tnslsnr: please wait...

TNSLSNR for Linux: Version 12.2.0.1.0 - Production
System parameter file is /home/oracle/app/oracle/product/12.2.0/dbhome_1/network/admin/listener.ora
Log messages written to /home/oracle/app/oracle/diag/tnslsnr/wl1/listener/alert/log.xml
Listening on: (DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=wl1)(PORT=1521)))
Listening on: (DESCRIPTION=(ADDRESS=(PROTOCOL=ipc)(KEY=EXTPROC1521)))

Connecting to (DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=wl1)(PORT=1521)))
STATUS of the LISTENER
------------------------
Alias                     LISTENER
Version                   TNSLSNR for Linux: Version 12.2.0.1.0 - Production
Start Date                22-AUG-2018 20:28:28
Uptime                    0 days 0 hr. 0 min. 0 sec
Trace Level               off
Security                  ON: Local OS Authentication
SNMP                      OFF
Listener Parameter File   /home/oracle/app/oracle/product/12.2.0/dbhome_1/network/admin/listener.ora
Listener Log File         /home/oracle/app/oracle/diag/tnslsnr/wl1/listener/alert/log.xml
Listening Endpoints Summary...
  (DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=wl1)(PORT=1521)))
  (DESCRIPTION=(ADDRESS=(PROTOCOL=ipc)(KEY=EXTPROC1521)))
The listener supports no services
The command completed successfully
```

## 启动数据库实例

``` shell
# 以system用户登录oracle
$ sqlplus /nolog

SQL*Plus: Release 12.2.0.1.0 Production on Wed Aug 22 20:37:54 2018

Copyright (c) 1982, 2016, Oracle.  All rights reserved.

SQL> conn as sysdba
Enter user-name: system
Enter password: 
Connected to an idle instance.

# 启动数据库实例
SQL> startup
ORACLE instance started.

# 如果要关闭数据库实例
SQL> shutdown
Database closed.
Database dismounted.
ORACLE instance shut down.
```