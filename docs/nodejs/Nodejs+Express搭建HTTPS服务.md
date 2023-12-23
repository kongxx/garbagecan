# Nodejs+Express搭建HTTPS服务

最近开发需要搭建一个https的服务，正好最近在用nodejs和express，于是乎想到就近就使用这两东西来搭建一个https的服务吧。这里搭建过程总共需要两步，第一步生成证书，第二步使用https模块启动服务。

## 生成自签名证书

这里因为是自己开发测试使用，因此就简单点使用自签名证书了。

使用 openssl 命令生成服务器私钥文件

``` shell
openssl genrsa -out server.key 2048
```

生成证书请求

``` shell
openssl req -new -key server.key -out server.csr
```

根据私钥和证书请求生成证书

``` shell
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
```

## 使用nodejs的https模块

创建一个express项目（可以参考express官方文档），修改 app.js 文件，主要是问最后几行创建 https server部分。

``` shell
var express = require('express');
var path = require('path');
var http = require('http');
var https = require('https');
var fs = require('fs');

var app = express();

app.get('/', function(req, res, next) {
  res.send('hello world');
});

var httpServer = http.createServer(app);
httpServer.listen(9080);

const options = {
  key: fs.readFileSync('./server.key'),
  cert: fs.readFileSync('./server.crt')
};
var httpsServer = https.createServer(options, app);
httpsServer.listen(9443);
```

## 测试

使用 “node app.js” 或 ”npm start“ 启动服务，然后访问 https://localhost:9443 进行验证。
