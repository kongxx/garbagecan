# CentOS5.5上安装psycopg2

## 准备
接上一篇文章[CentOS5.5上安装Python2.7及ez_setup和pip包]，先安装好python2.7和pip。

## 安装postgresql开发包
CentOS5.5默认的postgresql版本比较低，需要安装高版本的postgresql库。

访问 http://yum.postgresql.org/rpmchart.php， 下载postgresql 9.1的库。

> 下面几个链接是我下载的版本
- http://yum.postgresql.org/9.1/redhat/rhel-5-x86_64/postgresql91-9.1.19-1PGDG.rhel5.x86_64.rpm
- http://yum.postgresql.org/9.1/redhat/rhel-5-x86_64/postgresql91-libs-9.1.19-1PGDG.rhel5.x86_64.rpm
- http://yum.postgresql.org/9.1/redhat/rhel-5-x86_64/postgresql91-devel-9.1.19-1PGDG.rhel5.x86_64.rpm


``` bash
$ sudo rpm -ivh postgresql91-libs-9.1.19-1PGDG.rhel5.x86_64.rpm
$ sudo rpm -ivh postgresql91-9.1.19-1PGDG.rhel5.x86_64.rpm
$ sudo rpm -ivh postgresql91-devel-9.1.19-1PGDG.rhel5.x86_64.rpm
```

## 安装psycopg2
此时如果使用下面的命令安装psycopg2，仍然会有编译错误。
``` bash
$ sudo /opt/python27/bin/pip install
```

所以，退而求其次，只能安装一个老版本的psycopg2。访问 http://initd.org/psycopg/tarballs/ 找到一个老版本，这里使用2.0.1版本，使用下面命令安装。

``` bash
$ sudo /opt/python27/bin/pip install http://initd.org/psycopg/tarballs/PSYCOPG-2-0/psycopg2-2.0.1.tar.gz
```

## 测试
``` python
import psycopg2

conn = psycopg2.connect(host="...", database="...", user="...", password="...")
cursor = conn.cursor ()
cursor.execute ("select * from ...")
row = cursor.fetchone ()
print row

cursor.close ()
conn.close ()
```