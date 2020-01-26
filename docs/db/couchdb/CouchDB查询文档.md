# CouchDB查询文档

接前一篇文章，今天看看怎样在CouchDB数据库中使用索引和执行查询等操作。

在CouchDB中对文档的查询可以通过_find命令来实现，_find命令也需要使用POST方法请求，提交的数据是一个JSON对象。

## 准备测试数据

先准备一个数据库和一些测试数据

``` shell
# 创建一个users数据库
$ curl -X PUT http://localhost:5984/users
{"ok":true}

# 添加一些测试数据
$ curl -X POST http://localhost:5984/users/_bulk_docs -H "Content-Type:application/json" -d '{"docs": [{"name":"user1", "age": 20, "sex": 1}, {"name":"user2", "age": 25, "sex": 1}, {"name":"user3", "age": 30, "sex": 1}, {"name":"user4", "age": 35, "sex": 1}, {"name":"user5", "age": 40, "sex": 1}, {"name":"user6", "age": 45, "sex": 1}, {"name":"user7", "age": 50, "sex": 1}, {"name":"user8", "age": 60, "sex": 1}, {"name":"user9", "age": 70, "sex": 1}, {"name":"user10", "age": 80, "sex": 1}]}'
[{"ok":true,"id":"a10691778356d48a39f4ec678400efb0","rev":"1-28ba94df4e768e20af039ebcf1a56a92"},{"ok":true,"id":"a10691778356d48a39f4ec678400f541","rev":"1-708cef2b30e96cc60d0db2e51ffc4754"},{"ok":true,"id":"a10691778356d48a39f4ec678400f7ea","rev":"1-3668a6e0725f7158bd07d0a00b2606bf"},{"ok":true,"id":"a10691778356d48a39f4ec678400fff1","rev":"1-b2de620ef1e4f8e4cb39badb41629fc4"},{"ok":true,"id":"a10691778356d48a39f4ec6784010412","rev":"1-e674282fd617cdf0970f767bd528c070"},{"ok":true,"id":"a10691778356d48a39f4ec67840108be","rev":"1-477e98d943469098a19cb98e5ddfd5eb"},{"ok":true,"id":"a10691778356d48a39f4ec6784011650","rev":"1-cff431d713717931882056b1c6cb4b45"},{"ok":true,"id":"a10691778356d48a39f4ec6784011d50","rev":"1-b79e63171c37f8c41c4b3c49bc4eeb9a"},{"ok":true,"id":"a10691778356d48a39f4ec6784012bf8","rev":"1-bf42e3495863304b2a6ec520a833aa9e"},{"ok":true,"id":"a10691778356d48a39f4ec6784012eb3","rev":"1-848ad86d8b361e100b6cb78484ff3626"}]
```

## 查询

``` shell
# 查询年龄在25-75之间的用户，返回结果从第0条开始，返回5条，且只返回_id, name, age, _rev字段。
$ curl -X POST http://localhost:5984/users/_find -H "Content-Type:application/json" -d '{"selector": {"age": {"$gte": 25, "$lte": 75}}, "fields": ["_id", "name", "age", "_rev"], "limit": 5, "skip": 0}'

{"docs":[
{"_id":"a10691778356d48a39f4ec678400f541","name":"user2","age":25,"_rev":"1-708cef2b30e96cc60d0db2e51ffc4754"},
{"_id":"a10691778356d48a39f4ec678400f7ea","name":"user3","age":30,"_rev":"1-3668a6e0725f7158bd07d0a00b2606bf"},
{"_id":"a10691778356d48a39f4ec678400fff1","name":"user4","age":35,"_rev":"1-b2de620ef1e4f8e4cb39badb41629fc4"},
{"_id":"a10691778356d48a39f4ec6784010412","name":"user5","age":40,"_rev":"1-e674282fd617cdf0970f767bd528c070"},
{"_id":"a10691778356d48a39f4ec67840108be","name":"user6","age":45,"_rev":"1-477e98d943469098a19cb98e5ddfd5eb"}
],
"bookmark": "g1AAAABweJzLYWBgYMpgSmHgKy5JLCrJTq2MT8lPzkzJBYorJBoamFkamptbGJuapZhYJBpbppmkJpuZW5gYGBpYJKWC9HHA9BGlIwsAYPkdEQ",
"warning": "no matching index found, create an index to optimize query time"}
```

结果查询出来了，但是出现了一个警告，这个查询没有索引可用。下面我们创建一个索引，然后再执行查询看看。

``` shell
# 创建索引(这里只是做个演示)
$ curl -X POST http://localhost:5984/users/_index -H "Content-Type:application/json" -d '{"index": {"fields": ["age"]}, "name": "age-index", "type": "json"}'
```

然后再次执行上面的查询，可以发现不会再报上面的那个警告了。


## 参考

- http://docs.couchdb.org/en/latest/api/index.html

