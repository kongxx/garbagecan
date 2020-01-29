# ArangoDB集合操作

通常我们可以通过 ArangoDB 提供的 Web 接口来对 ArangoDB 进行监控和管理。但今天来看看怎样通过 arangosh 的方式来对数据库进行操作。

## 连接数据库

首先通过 arangosh 命令来进入 arangodb 的 shell 终端。

``` shell
$ arangosh
```

进入 shell 终端后，可以通过输入 help 来查看帮助。

ArangoDB 中的数据库操作都是通过 db.xxx 命令来实现的，在 shell 命令提示符下输入 db. 然后按 Tab 键，系统会提示可以使用的函数列表。

``` shell
# 查看当前有哪些数据库
127.0.0.1:8529@_system> db._databases();
[ 
  "_system"
]

# 查看当前数据库名
127.0.0.1:8529@_system> db._name();
_system

127.0.0.1:8529@_system> db.toString();
[object ArangoDatabase "_system"]
```

上面的操作会登录默认的数据库 "_system"，也可以通过指定数据库名来直接使用指定的数据库，比如：

``` shell
arangosh --server.username root --server.password <password> --server.database mydb
```

## 数据库操作

``` shell
# 创建数据库
127.0.0.1:8529@_system> db._createDatabase("mydb");
true

# 查看数据库
127.0.0.1:8529@_system> db._databases();
[ 
  "_system", 
  "mydb" 
]

# 切换使用新创建的数据库
127.0.0.1:8529@_system> db._useDatabase("mydb");
true

127.0.0.1:8529@mydb> db._name();
mydb

127.0.0.1:8529@mydb> db._useDatabase("_system");
true

# 删除数据库
127.0.0.1:8529@_system> db._dropDatabase("mydb");
true
```

## 集合操作

### 创建集合

``` shell
127.0.0.1:8529@mydb> db._create("mycollection");
[ArangoCollection 10139, "mycollection" (type document, status loaded)]
```

### 写入数据

集合一旦创建好，就可以 db.mycollection. + Tab 来查看可以有哪些对集合的操作了。

``` shell
127.0.0.1:8529@mydb> db.mycollection.save({ _key: "mykey1", value : "myvalue1" });
{ 
  "_id" : "mycollection/mykey1", 
  "_key" : "mykey1", 
  "_rev" : "_Z8uG62W---" 
}

127.0.0.1:8529@mydb> db.mycollection.save({ _key: "mykey2", value : "myvalue2" });
{ 
  "_id" : "mycollection/mykey2", 
  "_key" : "mykey2", 
  "_rev" : "_Z8uG62e---" 
}

127.0.0.1:8529@mydb> db.mycollection.save({ _key: "mykey3", value : "myvalue3" });
{ 
  "_id" : "mycollection/mykey3", 
  "_key" : "mykey3", 
  "_rev" : "_Z8uG7vO---" 
}

127.0.0.1:8529@mydb> db.mycollection.save({ col1: "column1", col2 : "column2", col3: "column3" });
{ 
  "_id" : "mycollection/13352", 
  "_key" : "13352", 
  "_rev" : "_Z8uuVAG---" 
}

# 查看一下集合数
127.0.0.1:8529@mydb> db.mycollection.count();
4
```

### 查询集合

``` shell
127.0.0.1:8529@mydb> db._query('FOR my IN mycollection RETURN my._key').toArray();
[ 
  "13352",
  "mykey1", 
  "mykey2", 
  "mykey3" 
]

127.0.0.1:8529@mydb> db._query('FOR my IN mycollection RETURN my').toArray();
[ 
  { 
    "_key" : "mykey1", 
    "_id" : "mycollection/mykey1", 
    "_rev" : "_Z8uG62W---", 
    "value" : "myvalue1" 
  }, 
  { 
    "_key" : "mykey2", 
    "_id" : "mycollection/mykey2", 
    "_rev" : "_Z8uG62e---", 
    "value" : "myvalue2" 
  }, 
  { 
    "_key" : "mykey3", 
    "_id" : "mycollection/mykey3", 
    "_rev" : "_Z8uG7vO---", 
    "value" : "myvalue3" 
  } ,
  { 
    "_key" : "13352", 
    "_id" : "mycollection/13352", 
    "_rev" : "_Z8uuVAG---", 
    "col1" : "column1", 
    "col2" : "column2", 
    "col3" : "column3" 
  } 
]
```

使用 Filter 过滤查询

``` shell
127.0.0.1:8529@mydb> db._query('FOR my IN mycollection FILTER my._key == "mykey1" RETURN my').toArray();
[ 
  { 
    "_key" : "mykey1", 
    "_id" : "mycollection/mykey1", 
    "_rev" : "_Z8uG62W---", 
    "value" : "myvalue1" 
  } 
]
```

### 删除集合数据

``` shell
127.0.0.1:8529@mydb> db.mycollection.remove({_key: "mykey1"});
{ 
  "_id" : "mycollection/mykey1", 
  "_key" : "mykey1", 
  "_rev" : "_Z8uU7Eq---" 
}
```

### 删除集合

``` shell
127.0.0.1:8529@mydb> db.mycollection.drop();
```
