# ArangoDB文档操作

通过ArangoDB提供的shell终端，我们可以执行很多文档集合操作，下面就看看一些常用的方法。

``` shell
$ arangosh --server.username root --server.password <password> --server.database mydb

127.0.0.1:8529@mydb> db.users.insert({ name: "user1", age: 10, sex: 1, address: {home: "home address", office: "office address"}});
127.0.0.1:8529@mydb> db.users.insert({ name: "user2", age: 20, sex: 1, address: {home: "home address", office: "office address"}});
127.0.0.1:8529@mydb> db.users.insert({ name: "user3", age: 30, sex: 1, address: {home: "home address", office: "office address"}});
127.0.0.1:8529@mydb> db.users.insert({ name: "user4", age: 40, sex: 0, address: {home: "home address", office: "office address"}});
127.0.0.1:8529@mydb> db.users.insert({ name: "user5", age: 50, sex: 0, address: {home: "home address", office: "office address"}});

127.0.0.1:8529@mydb> db.users.count();
5
```

### all()方法
all()方法可以返回集合的所有文档对象，我们可以对其使用limit()等函数来限制返回结果。
``` shell
# 返回所有文档
db.users.all().toArray();
...
# 返回前两条结果
db.users.all().limit(2).toArray();
...
```

### any()方法
any()方法用来在集合中随机返回一个文档对象。
``` shell
# 随机返回一个文档对象
db.users.any().toArray();
...
```

### byExample()方法
byExample()方法用来根据条件查询文档对象。
``` shell
# 查询name=user3的对象集合
127.0.0.1:8529@mydb> db.users.byExample({"name": "user3"}).toArray();

# 查询name=user3并且age=30的对象集合
127.0.0.1:8529@mydb> db.users.byExample({"name": "user3", "age": 30}).toArray();
```

查询结果也可以使用AQL来遍历
``` shell
127.0.0.1:8529@mydb> var it = db.users.byExample({"sex" : 1});
...
127.0.0.1:8529@mydb> while (it.hasNext()) print(it.next());
...
```

### firstExample()方法
firstExample()方法用来返回查询结果中的第一个文档对象。
``` shell
127.0.0.1:8529@mydb> db.users.firstExample("sex", 1);
...
```

### document()方法
document()方法用来根据_id或_key查询文档对象。
``` shell
# 按 _id 查询
127.0.0.1:8529@mydb> db.users.document("users/16771");
...
127.0.0.1:8529@mydb> db.users.document({"_id": "users/16771"});
...

# 按 _key 查询
127.0.0.1:8529@mydb> db.users.document("16771");
...
127.0.0.1:8529@mydb> db.users.document({"_key": "16771"});
...

# 查询多个文档
127.0.0.1:8529@mydb> db.users.document(["users/16764", "users/16771"]);
...
127.0.0.1:8529@mydb> db.users.document(["16764", "16771"]);
...
127.0.0.1:8529@mydb> db.users.documents(["16764", "16771"]);
...
```

### exists()方法
exists()方法用来判断文档是否存在，和document类似，可以按_id或_key来判断。
``` shell
127.0.0.1:8529@mydb> db.users.exists("users/16771");
{ 
  "_id" : "users/16771", 
  "_key" : "16771", 
  "_rev" : "_Z8v3A76---" 
}
127.0.0.1:8529@mydb> db.users.exists("16771");
{ 
  "_id" : "users/16771", 
  "_key" : "16771", 
  "_rev" : "_Z8v3A76---" 
}
127.0.0.1:8529@mydb> db.users.exists("12345");
false
```
如果存在返回对象，如果不存在返回false。

### insert()方法
前面造测试数据的时候已经使用，这里不再多说。

### save()方法
前面造测试数据的时候已经使用，这里不再多说。

### replace()方法
replace()方法用来替换已存在的文档对象。
``` shell
127.0.0.1:8529@mydb> db.users.replace("users/16782", {name: "user5", age: 50, sex: 0, address: {home: "home address", office: "office address"}});
{ 
  "_id" : "users/16782", 
  "_key" : "16782", 
  "_rev" : "_Z86X57W--_", 
  "_oldRev" : "_Z86Xe1u--_" 
}

127.0.0.1:8529@mydb> db.users.replace("16782", {name: "user5", age: 50, sex: 0, address: {home: "home address", office: "office address"}});
{ 
  "_id" : "users/16782", 
  "_key" : "16782", 
  "_rev" : "_Z86YKTy--_", 
  "_oldRev" : "_Z86X57W--_" 
}
```
replace()方法也可以执行批量替换操作，语法如下：
- collection.replace(selectorarray, dataarray)
- collection.replace(selectorarray, dataarray, options)

### update()方法
update()方法用来替换文档中的一些属性。
``` shell
127.0.0.1:8529@mydb> db.users.update("users/16782", {name: "user5", age: 55});
{ 
  "_id" : "users/16782", 
  "_key" : "16782", 
  "_rev" : "_Z86e5zy--_", 
  "_oldRev" : "_Z86ewLW--_" 
}

127.0.0.1:8529@mydb> db.users.update("16782", {address: {home: "new home address"}});
{ 
  "_id" : "users/16782", 
  "_key" : "16782", 
  "_rev" : "_Z86faPm--_", 
  "_oldRev" : "_Z86e5zy--_" 
}
```

### remove()方法
remove()方法用来按_id删除文档对象，或者删除一个文档对象。
``` shell
# 按 _id 删除
127.0.0.1:8529@mydb> db.users.remove("users/16782");

# 按document删除
127.0.0.1:8529@mydb> d = db.users.document("users/16782")
127.0.0.1:8529@mydb> db.users.remove(d);
```

### removeByKeys()方法
removeByKeys()方法用来按_key删除文档。
``` shell
127.0.0.1:8529@mydb> db.users.removeByKeys(["16775", "19465"])
{ 
  "removed" : 2, 
  "ignored" : 0 
}
```
