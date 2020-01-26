# CouchDB入门

## 配置yum源

在CentOS8上默认的yum源是没有couchdb包的，因此需要添加couchdb的yum源。创建一个新文件 /etc/yum.repos.d/bintray-apache-couchdb-rpm.repo，内容如下：

``` shell
[bintray--apache-couchdb-rpm]
name=bintray--apache-couchdb-rpm
baseurl=http://apache.bintray.com/couchdb-rpm/el$releasever/$basearch/
gpgcheck=0
repo_gpgcheck=0
enabled=1
```

## 安装CouchDB

``` shell
$ yum install -y epel-release
$ yum install -y couchdb
```

默认couchdb的安装路径为：/opt/couchdb

## 运行CouchDB
``` shell
$ sudo -i -u couchdb /opt/couchdb/bin/couchdb
```

启动后，可以通过浏览器访问Fauxton页面 http://localhost:5984/_utils/index.html 来验证安装是否成功，也可以通过此页面管理CouchDB。

或者通过 curl 命令行来验证

``` shell
$ curl http://localhost:5984
{"couchdb":"Welcome","version":"2.3.1","git_sha":"c298091a4","uuid":"04de5436d56ab4a1a4ace6c16555fbe4","features":["pluggable-storage-engines","scheduler"],"vendor":{"name":"The Apache Software Foundation"}}
```

默认CouchDB只能被通过本机访问，可以通过修改 /opt/couchdb/etc/default.ini 文件中的 bind_address 配置来实现。

``` shell
将
bind_address = 127.0.0.1
改成
bind_address = 0.0.0.0
```

然后重新启动CouchDB。

## 使用CouchDB

通过Fauxton页面可以对CouchDB做常规的管理操作，也可以通过 CouchDB 的 API 来进行常规操作。

### 创建数据库

``` shell
# 创建数据库
$ curl -X PUT http://localhost:5984/mydb
{"ok":true}

# 查看所有数据库
$ curl -X GET http://localhost:5984/_all_dbs
["mydb"]

# 查看数据库信息
$ curl -X GET http://localhost:5984/mydb | json_reformat
{
    "db_name": "mydb",
    "purge_seq": "0-g1AAAAFTeJzLYWBg4MhgTmEQTM4vTc5ISXIwNDLXMwBCwxygFFMeC5BkeACk_gNBViIDQbUHIGrvE6N2AUTtfmLUNkDUzsevNikBSCbVE3RrkgNIXTxhdQogdfYE1SUyJMlDFGUBAD9sXo4",
    "update_seq": "0-g1AAAAFTeJzLYWBg4MhgTmEQTM4vTc5ISXIwNDLXMwBCwxygFFMiQ5L8____sxIZ8ChKUgCSSfaE1TmA1MUTVpcAUldPUF0eC5BkaABSQKXziVG7AKJ2PzFqD0DU3idG7QOIWpB7swBegl6O",
    "sizes": {
        "file": 33992,
        "external": 0,
        "active": 0
    },
    "other": {
        "data_size": 0
    },
    "doc_del_count": 0,
    "doc_count": 0,
    "disk_size": 33992,
    "disk_format_version": 7,
    "data_size": 0,
    "compact_running": false,
    "cluster": {
        "q": 8,
        "n": 1,
        "w": 1,
        "r": 1
    },
    "instance_start_time": "0"
}
```

### 创建文档

``` shell
$ curl -X POST http://localhost:5984/mydb -d '{"name":"kongxx", "age": 30, "sex": 1}' -H "Content-Type:application/json"
{"ok":true,"id":"a10691778356d48a39f4ec6784000d2c","rev":"1-1b738f642df6eb80b3eb3e2839bbd10f"}
```

查询一下新创建的文档

``` shell
$ curl -X GET http://localhost:5984/mydb/a10691778356d48a39f4ec6784000d2c
{"_id":"a10691778356d48a39f4ec6784000d2c","_rev":"1-1b738f642df6eb80b3eb3e2839bbd10f","name":"kongxx","age":30,"sex":1}
```

查看所有文档

``` shell
$ curl -X GET http://localhost:5984/mydb/_all_docs
{"total_rows":1,"offset":0,"rows":[
{"id":"a10691778356d48a39f4ec6784000d2c","key":"a10691778356d48a39f4ec6784000d2c","value":{"rev":"1-1b738f642df6eb80b3eb3e2839bbd10f"}}
```

批量创建文档

``` shell
$ curl -X POST http://localhost:5984/mydb/_bulk_docs -d '{"docs": [{"name":"user3", "age": 30, "sex": 1}, {"name":"user4", "age": 30, "sex": 1}, {"name":"user5", "age": 30, "sex": 1}]}' -H "Content-Type:application/json"
[{"ok":true,"id":"a10691778356d48a39f4ec6784002d88","rev":"1-3668a6e0725f7158bd07d0a00b2606bf"},{"ok":true,"id":"a10691778356d48a39f4ec678400384f","rev":"1-16de004f6aaafd2d9045cbe6a48ff701"},{"ok":true,"id":"a10691778356d48a39f4ec678400393f","rev":"1-d1ddf562a1e77674a1c1b5b8f4a5f350"}]
```

也可以使用 PUT 方法创建文档，比如

``` shell
$ curl -X PUT http://localhost:5984/users
{"ok":true}
$ curl -X PUT http://localhost:5984/users/001 -d '{"name":"user1", "age": 30, "sex": 1}'
{"ok":true,"id":"001","rev":"1-d502adf1ee03f7145710c17d39a99701"}
$ curl -X PUT http://localhost:5984/users/002 -d '{"name":"user2", "age": 30, "sex": 1}'
{"ok":true,"id":"002","rev":"1-c0507d20e6d6b44bf8ca7203ead8d952"}
```

### 更新文档

``` shell
# 先查询一下文档
$ curl -X GET http://localhost:5984/mydb/a10691778356d48a39f4ec6784000d2c
{"_id":"a10691778356d48a39f4ec6784000d2c","_rev":"1-1b738f642df6eb80b3eb3e2839bbd10f","name":"kongxx","age":30,"sex":1}

# 更新文档，需要指定 _rev
$ curl -X PUT http://localhost:5984/mydb/a10691778356d48a39f4ec6784000d2c/ -d '{"name":"kongxx", "age": 36, "sex": 1,  "_rev": "1-1b738f642df6eb80b3eb3e2839bbd10f"}'
{"ok":true,"id":"a10691778356d48a39f4ec6784000d2c","rev":"2-9b552a207bbdea7e7b6ce6cb184c6f4e"}

# 更新文档后查询
$ curl -X GET http://localhost:5984/mydb/a10691778356d48a39f4ec6784000d2c
{"_id":"a10691778356d48a39f4ec6784000d2c","_rev":"2-9b552a207bbdea7e7b6ce6cb184c6f4e","name":"kongxx","age":36,"sex":1}
```

### 删除文档

``` shell
# 删除文档
$ curl -X DELETE http://localhost:5984/mydb/a10691778356d48a39f4ec6784000d2c?rev=2-9b552a207bbdea7e7b6ce6cb184c6f4e
{"ok":true,"id":"a10691778356d48a39f4ec6784000d2c","rev":"3-60f90d5d7f1866688cbd55833b2b8c3a"}

# 查询文档
$ curl -X GET http://localhost:5984/mydb/a10691778356d48a39f4ec6784000d2c
{"error":"not_found","reason":"deleted"}
```

### 删除数据库

``` shell
$ curl -X DELETE http://localhost:5984/mydb
{"ok":true}
```
