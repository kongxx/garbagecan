# ArangoDB Restful API

ArangoDB 除了提供 Web 和 shell 接口来管理数据库之外，还可以使用 Restful API 的方式来管理数据库。

## 数据库操作

### 数据库查询

当前数据库版本
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X GET http://localhost:8529/_db/mydb/_api/version | json_reformat
{
    "server": "arango",
    "license": "community",
    "version": "3.6.0"
}
```

当前用户数据库列表
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X GET http://localhost:8529/_api/database/user | json_reformat 
{
    "error": false,
    "code": 200,
    "result": [
        "_system",
        "mydb"
    ]
}
```

数据库列表
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X GET http://localhost:8529/_api/database | json_reformat
{
    "error": false,
    "code": 200,
    "result": [
        "_system",
        "mydb"
    ]
}
```

### 创建数据库
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X POST -H 'accept: application/json' --data-binary @- --dump - http://localhost:8529/_api/database <<EOF
{ 
  "name" : "mydb", 
  "options" : { 
    "sharding" : "flexible", 
    "replicationFactor" : 3 
  } 
}
EOF

HTTP/1.1 201 Created
X-Content-Type-Options: nosniff
Server: ArangoDB
Connection: Keep-Alive
Content-Type: application/json; charset=utf-8
Content-Length: 40

{"error":false,"code":201,"result":true}
```

### 删除数据库
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X DELETE -H 'accept: application/json' --dump - http://localhost:8529/_api/database/mydb

HTTP/1.1 200 OK
X-Content-Type-Options: nosniff
Server: ArangoDB
Connection: Keep-Alive
Content-Type: application/json; charset=utf-8
Content-Length: 40

{"error":false,"code":200,"result":true}
```

## 集合操作

### 查询列表
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X GET --header 'accept: application/json' http://localhost:8529/_api/collection | json_reformat
...
```

### 创建集合
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X POST -H 'accept: application/json' --data-binary @- --dump - http://localhost:8529/_api/collection ｜ json_reformat <<EOF
{ 
  "name" : "users"
}
EOF
```

### 删除集合
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X DELETE http://localhost:8529/_api/collection/users | json_reformat 
{
    "error": false,
    "code": 200,
    "id": "26506"
}
```

### 查询集合
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X GET http://localhost:8529/_api/collection/users | json_reformat
{
    "error": false,
    "code": 200,
    "type": 2,
    "isSystem": false,
    "id": "26654",
    "globallyUniqueId": "hC515043B52A2/26654",
    "name": "users",
    "status": 3
}

$ curl -u $USERNAME:$PASSWORD -s -X GET http://localhost:8529/_api/collection/users/properties | json_reformat
{
    "error": false,
    "code": 200,
    "writeConcern": 1,
    "waitForSync": false,
    "globallyUniqueId": "hC515043B52A2/26654",
    "id": "26654",
    "cacheEnabled": false,
    "isSystem": false,
    "keyOptions": {
        "allowUserKeys": true,
        "type": "traditional",
        "lastValue": 0
    },
    "objectId": "26653",
    "name": "users",
    "status": 3,
    "statusString": "loaded",
    "type": 2
}

$ curl -u $USERNAME:$PASSWORD -s -X GET http://localhost:8529/_api/collection/users/count | json_reformat
{
    "error": false,
    "code": 200,
    "writeConcern": 1,
    "waitForSync": false,
    "globallyUniqueId": "hC515043B52A2/26654",
    "id": "26654",
    "cacheEnabled": false,
    "isSystem": false,
    "keyOptions": {
        "allowUserKeys": true,
        "type": "traditional",
        "lastValue": 0
    },
    "objectId": "26653",
    "count": 0,
    "name": "users",
    "status": 3,
    "statusString": "loaded",
    "type": 2
}
```

### 清空集合
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X PUT http://localhost:8529/_api/collection/users/truncate | json_reformat
{
    "error": false,
    "code": 200,
    "type": 2,
    "isSystem": false,
    "id": "26654",
    "globallyUniqueId": "hC515043B52A2/26654",
    "name": "users",
    "status": 3
}
```

## 文档操作

### 添加文档
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X POST -H 'accept: application/json' --data-binary @- --dump - http://localhost:8529/_api/document/users <<EOF
{ 
  "name": "user1", 
  "age": 10, 
  "sex": 1, 
  "address": {
    "home": "home address", 
    "office": "office address"
  }
}
EOF

HTTP/1.1 202 Accepted
X-Content-Type-Options: nosniff
Etag: "_Z9-_4mW---"
Location: /_db/_system/_api/document/users/27157
Server: ArangoDB
Connection: Keep-Alive
Content-Type: application/json; charset=utf-8
Content-Length: 57

{"_id":"users/27157","_key":"27157","_rev":"_Z9-_4mW---"}
```

### 更新文档
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X PUT -H 'accept: application/json' --data-binary @- --dump - http://localhost:8529/_api/document/users/27157 <<EOF
{ 
  "name": "user1", 
  "age": 20, 
  "sex": 1, 
  "address": {
    "home": "home address", 
    "office": "office address"
  }
}
EOF
```

### 删除文档
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X DELETE http://localhost:8529/_api/dosers/27157 | json_reformat 
{
    "_id": "users/27157",
    "_key": "27157",
    "_rev": "_Z9-DAmi--_"
}
```

### 查询文档
``` shell
$ curl -u $USERNAME:$PASSWORD -s -X GET http://localhost:8529/_api/document/users/27157 | json_reformat 
{
    "_key": "27157",
    "_id": "users/27157",
    "_rev": "_Z9-_4mW---",
    "name": "user1",
    "age": 10,
    "sex": 1,
    "address": {
        "home": "home address",
        "office": "office address"
    }
}
```
