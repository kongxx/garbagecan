# AWS Elastic Beanstalk通过单实例配置https

在 AWS Elastic Beanstalk 上提供了多种方式来实现 https，比如通过单实例配置，负载均衡配置等。今天就以 nodejs + express 为例说一下怎么通过单实例配置实现https

## 创建demo应用

创建一个简单的 nodejs + express 应用 myapp，其中包含两个文件 package.json 和 app.js。

### package.json
``` shell
{
  "name": "myapp",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "start": "node app.js",
    "dev": "nodemon app.js"
  },
  "author": "",
  "license": "ISC",
  "dependencies": {
    "express": "^4.18.2",
    "nodemon": "^3.0.3"
  }
}
```

### app.js
``` shell
const express = require('express');

const app = express();

const port = process.env.PORT || 3000;

app.get("/", (req, res, next) => {
  res.send("Express Server");
});

app.listen(port, () => {
  console.log(`[server]: Server is running at http://localhost:${port}`);
});
```

## 配置https

### .ebextensions/https-instance.config

在应用目录下创建 “.ebextensions/https-instance.config” 文件，内容如下

``` shell
files:
  # Public certificate
  /etc/pki/tls/certs/server.crt:
    mode: "000400"
    owner: root
    group: root
    content: |
      -----BEGIN CERTIFICATE-----
      ...
      -----END CERTIFICATE-----

  # Private key
  /etc/pki/tls/certs/server.key:
    mode: "000400"
    owner: root
    group: root
    content: |
      -----BEGIN PRIVATE KEY-----
      ...
      -----END PRIVATE KEY-----

Resources:
  sslSecurityGroupIngress:
    Type: AWS::EC2::SecurityGroupIngress
    Properties:
      GroupId: {"Fn::GetAtt" : ["AWSEBSecurityGroup", "GroupId"]}
      IpProtocol: tcp
      ToPort: 443
      FromPort: 443
      CidrIp: 0.0.0.0/0
```

其中证书和私钥的内容可以从证书和私钥文件获取。

### .platform/nginx/conf.d/https.conf

在应用目录下创建 “.platform/nginx/conf.d/https.conf” 文件，内容如下

``` shell
server {
    listen       443 ssl;
    server_name  localhost;

    ssl_certificate      /etc/pki/tls/certs/server.crt;
    ssl_certificate_key  /etc/pki/tls/certs/server.key;

    ssl_session_timeout  5m;

    ssl_protocols  TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers   on;

    location / {
        proxy_pass              http://localhost;
        proxy_set_header        Connection "";
        proxy_http_version      1.1;
        proxy_set_header        Host            $host;
        proxy_set_header        X-Real-IP       $remote_addr;
        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header        X-Forwarded-Proto https;
    }
}
```

## 部署

在应用目录下将所有元文件打包，然后按照正常应用部署的方式部署就可以了，只是其中 “Capacity -> Auto scaling group -> Environment type” 要选成 “Single instance”。

然后就可以通过 https://xxx.elasticbeanstalk.com/ 访问了。

