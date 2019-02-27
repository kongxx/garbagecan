# python使用phoenixdb操作hbase

接前一篇：使用phoenix查询hbase

今天看看怎样在 python 中使用 phoenixdb 来操作 hbase

## 安装 phoenixdb 库

``` shell
pip install phoenixdb
```

## 例子

首先启动 queryserver 服务

``` shell
cd apache-phoenix-4.14.1-HBase-1.4-bin/bin
./queryserver.py
```

然后使用下面代码来建立连接、创建/删除并查询表。代码比较简单，和我们通常查询关系型数据库比较类似，这里就不多说了哈。

``` python
import phoenixdb
import phoenixdb.cursor

url = 'http://localhost:8765/'
conn = phoenixdb.connect(url, autocommit=True)

cursor = conn.cursor()
# cursor.execute("DROP TABLE users")
cursor.execute("CREATE TABLE users (id INTEGER PRIMARY KEY, username VARCHAR, password VARCHAR)")
cursor.execute("UPSERT INTO users VALUES (?, ?, ?)", (1, 'admin', 'Letmein'))
cursor.execute("UPSERT INTO users VALUES (?, ?, ?)", (2, 'kongxx', 'Letmein'))
cursor.execute("SELECT * FROM users")
print cursor.fetchall()

cursor = conn.cursor(cursor_factory=phoenixdb.cursor.DictCursor)
cursor.execute("SELECT * FROM users WHERE id=1")
user = cursor.fetchone()
print user['USERNAME']
print user['PASSWORD']
```

最后运行这个程序看一下效果吧。