# 使用Python操作ArangoDB

前面说过怎样使用 ArangoDB 的 Web，Shell 和 Restful API 来操作数据库，今天看一下怎样使用Python语言来操作ArangoDB数据库。

要通过 Python 脚本来访问 ArangoDB，我们需要先安装 pyArango 库
``` shell
$ pip install pyArango
```

下面写了一个简单的 Python 脚本，其中包含了一些常用的操作，如：创建连接，数据库操作，集合操作和文档操作等。

``` python
from pyArango.connection import *


# 创建连接
conn = Connection(arangoURL='http://106.54.228.237:8529', username="root", password="Letmein")
print('conn: %s' % conn)

# 创建数据库
if not conn.hasDatabase('mydb'):
    conn.createDatabase(name="mydb")

db = conn['mydb']
print('db: %s' % db)

# 创建集合
if not db.hasCollection('users'):
    db.createCollection(name='users')

collection = db['users']
print('collection: %s' % collection)

# 插入文档数据
print('collection count before insert: %s' % collection.count())
for i in range(0, 10):
    user = {
        'name': 'user_' + str(i), 
        'age': 20 + i, 
        'address': {
            'home': 'home address', 
            'office': 'office address'
        }
    }
    collection.createDocument(user).save()
print('collection count after insert: %s' % collection.count())

# 分页查询文档
print('fetchAll ...')
query = collection.fetchAll(skip=5, limit=2)
for doc in query:
    print(doc)

# 按条件查询文档
print('fetchByExample ...')
query = collection.fetchByExample({'name': 'user_5'}, batchSize=10, count=True)
for doc in query:
    print(doc)

# 使用AQL查询文档
print('query by AQL ...')
aql = "FOR user IN users FILTER user.name == @name || user.age > 25 LIMIT 5 RETURN user"
bindVars = {'name': 'user_0'}
query = db.AQLQuery(aql, rawResults=False, batchSize=1, bindVars=bindVars)
for doc in query:
    print(doc)

# 删除文档
print('collection count before delete: %s' % collection.count())
query = collection.fetchAll()
for doc in query:
    doc.delete()
print('collection count after delete: %s' % collection.count())

# 删除集合
collection.delete()
```

参考文档
- https://pyarango.readthedocs.io/en/stable/
