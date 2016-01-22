# Linux上pip安装cx_Oracle

## 下载

从 [下载链接](http://www.oracle.com/technetwork/database/features/instant-client/index-097480.html) 下载下面两个文件
instantclient-basiclite-linux.x64-11.2.0.4.0.zip
instantclient-sdk-linux.x64-11.2.0.4.0.zip

解压这两个文件到/opt/instantclient_11_2目录下

## 设置环境变量

``` bash
export ORACLE_HOME=/opt/instantclient_11_2/
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$ORACLE_HOME
```
## 安装

安装cx_Oracle之前需要先建立一个链接libclntsh.so，如下：

``` bash
cd /opt/instantclient_11_2/
ln -s libclntsh.so.11.1 libclntsh.so
```

执行安装

``` bash
pip install cx_Oracle
```

## 测试
``` python
import cx_Oracle

conn = cx_Oracle.connect('jhinno/jhinno@192.168.0.188/jhinno')  
cursor = conn.cursor ()
cursor.execute ("select sysdate from dual")
row = cursor.fetchone ()
print row

cursor.close ()
conn.close ()
```
