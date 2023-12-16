# Termux搭建nodejs环境

## 安装nodejs

``` shell
~ $ pkg install nodejs
```

## 使用http-server搭建文件下载服务

先安 http-server 并启动

``` shell
# 安装 http-server 包
~ $ npm install -g http-server

# 启动 http-server 服务
~ $ http-server 

Starting up http-server, serving ./

http-server version: 14.1.1

http-server settings: 
CORS: disabled
Cache: 3600 seconds
Connection Timeout: 120 seconds
Directory Listings: visible
AutoIndex: visible
Serve GZIP Files: false
Serve Brotli Files: false
Default File Extension: none

Available on:
  http://192.168.31.250:8080
  http://127.0.0.1:8080
Hit CTRL-C to stop the server
```

使用浏览器访问 http://192.168.31.250:8080/ 验证一下。

## 使用 express 框架搭建 web 服务

首先创建工程目录

``` shell
~ $ mkdir myapp
~/myapp $ cd myapp
```

初始化nodejs工程

```
~/myapp $ npm init

Press ^C at any time to quit.
package name: (myapp) 
version: (1.0.0) 
description: 
entry point: (app.js)         <- 这里把默认的index.js改成app.js
test command: 
git repository: 
keywords: 
author: 
license: (ISC) 
About to write to /data/data/com.termux/files/home/myapp/package.json:

{
  "name": "myapp",
  "version": "1.0.0",
  "description": "",
  "main": "app.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "",
  "license": "ISC"
}

Is this OK? (yes) 
```

安装 express 框架

``` shell
~/myapp $ npm install express
```

写个Hello World验证程序

``` JavaScript
const express = require('express')
const app = express()
const port = 3000

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})
```

启动服务

``` shell
~/myapp $ node app.js
Example app listening on port 3000
```

使用浏览器访问 http://192.168.31.250:3000/ 验证。
