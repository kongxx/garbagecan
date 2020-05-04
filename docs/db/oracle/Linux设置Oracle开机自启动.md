# Linux设置Oracle开机自启动

## 编辑 /etc/oratab 文件

修改其中的路径，并且将最后的 “N” 改为 “Y”，如下：

``` shell
orcl:/home/oracle/app/oracle/product/12.2.0/dbhome_1:Y
```

## 创建/修改 /etc/systemd/system/oracle-rdbms.service 文件

文件内容如下（注意其中文件路径）：

``` shell
# /etc/systemd/system/oracle-rdbms.service
# Invoking Oracle scripts to start/shutdown Instances defined in /etc/oratab
# and starts Listener
[Unit]
Description=Oracle Database(s) and Listener
Requires=network.target
[Service]
Type=forking
Restart=no
ExecStart=/home/oracle/app/oracle/product/12.2.0/dbhome_1/bin/dbstart /home/oracle/app/oracle/product/12.2.0/dbhome_1
ExecStop=/home/oracle/app/oracle/product/12.2.0/dbhome_1/bin/dbshut /home/oracle/app/oracle/product/12.2.0/dbhome_1
User=oracle
[Install]
WantedBy=multi-user.target
```

## 设置服务开机自启动

``` shell
sudo systemctl enable oracle-rdbms.service 
```

重新启动机器。

也可以手动启动/停止服务
``` shell
sudo systemctl start oracle-rdbms.service 
sudo systemctl stop oracle-rdbms.service 
```

---
使用 /etc/init.d/xxx，参考
https://docs.oracle.com/en/database/oracle/oracle-database/12.2/unxar/stopping-and-starting-oracle-software.html#GUID-CA969105-B62B-4F5B-B35C-8FB64EC93FAA
