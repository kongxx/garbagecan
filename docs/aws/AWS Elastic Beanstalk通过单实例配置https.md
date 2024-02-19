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
      MIIDyjCCArKgAwIBAgIRAIIG4y+fnTdrBoHvq2ABZT4wDQYJKoZIhvcNAQELBQAw
      gYUxCzAJBgNVBAYTAkdCMQ8wDQYDVQQHEwZMb25kb24xDzANBgNVBAkTBkxvbmRv
      bjEwMC4GA1UECxMnSUJNIE1heGltbyBBcHBsaWNhdGlvbiBTdWl0ZSAoSW50ZXJu
      YWwpMSIwIAYDVQQDExlpbnRlcm5hbC5pODEwLm1hcy5pYm0uY29tMB4XDTIzMDQy
      NzE2NDAzNloXDTQzMDQyMjE2NDAzNlowIDEeMBwGA1UEAwwVKi5tYXMtaTgxMC1t
      YW5hZ2Uuc3ZjMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA29Kmx61/
      Y5w1/osGVyRX2tFdtoV5HoFp77TB0gNlGj9fEM3eFhbSOmZNTcmyrJH/Tzb4Xdtv
      4a71jUovCCpJFdIgsOPY7NH1WJJlRXzL10JcF6dumW2BbamgSdx/b45eyOwM7Qk1
      1eomDIterSr6CGH4DpHOOBjxltorzYXwkwFA+3uJK880J8JkxhVExoXTwjK1iudA
      HkmLaWrADfA/HIlWVpjv5s8n5xoAY4clSOf+OZZuzAYFZxiMUQykRZXv3r77xRz8
      Z/LBPV+MguTco30b45p5gOOlGY4lhNBtN1PcuNfHhIHsR4hdI70/uHv6IB/VLaWW
      Nqp4wvIKGXS4hwIDAQABo4GYMIGVMB0GA1UdJQQWMBQGCCsGAQUFBwMBBggrBgEF
      BQcDAjAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFJWW05LV6OCNbS12+TOphzfZ
      Rr40MEUGA1UdEQQ+MDyCFSoubWFzLWk4MTAtbWFuYWdlLnN2Y4IjKi5tYXMtaTgx
      MC1tYW5hZ2Uuc3ZjLmNsdXN0ZXIubG9jYWwwDQYJKoZIhvcNAQELBQADggEBAE36
      XxUxfDDq21CdZi8bidOge4aKbmgg/Blyy+Sgivs4npaq2XJfxHlcrf8EJwWR5wO3
      ZL93ECi6OYHK3b+xNmkk9+WeQy+gg3tL26WSLOOqy/iCD7lLn1dhcZjd5ecUeCqA
      zypgeDx82xJmSUEJv9k62PxRzM377Wq2DRKYms/EOE+oPLCsN28+gCjlwEl0QgcF
      DUghGQT/lMca0qJJG4iqcaQ96HDmqqW9G0rGcgjLFvYz49iT07yMnmT1aSXQL243
      uGTuU4hht3uCgQdcUygkk0yeMDx6xuViBs5bQwCdlFe4a3WAwPRHjfWckfiddWW4
      ySizXgbGFMQdJmL6v/A=
      -----END CERTIFICATE-----

  # Private key
  /etc/pki/tls/certs/server.key:
    mode: "000400"
    owner: root
    group: root
    content: |
      -----BEGIN PRIVATE KEY-----
      MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDb0qbHrX9jnDX+
      iwZXJFfa0V22hXkegWnvtMHSA2UaP18Qzd4WFtI6Zk1NybKskf9PNvhd22/hrvWN
      Si8IKkkV0iCw49js0fVYkmVFfMvXQlwXp26ZbYFtqaBJ3H9vjl7I7AztCTXV6iYM
      i16tKvoIYfgOkc44GPGW2ivNhfCTAUD7e4krzzQnwmTGFUTGhdPCMrWK50AeSYtp
      asAN8D8ciVZWmO/mzyfnGgBjhyVI5/45lm7MBgVnGIxRDKRFle/evvvFHPxn8sE9
      X4yC5NyjfRvjmnmA46UZjiWE0G03U9y418eEgexHiF0jvT+4e/ogH9UtpZY2qnjC
      8goZdLiHAgMBAAECggEAe7gKX/WY7eIcTX/HeMIepCwTt0dVzMJ/cgbk0yNQAr7J
      7XZAWWRMu8uaFH7Bjfi5ncbNcF2xkW5cC0JGSC0sZxiVh5klHNxqXog+cSZ0v1k8
      5Z85UybbonHGoet02b9iP2kDOWFmqFeZRJQoHARFJvSknOaZDt+TERkHcCvDI6iU
      xgOQzE6VXyJzp7FnkDgJYWDbOSctVvM/vzKFeplpScjsvH49Zv4VZTz3j25td9MK
      oXskL3K0uytOgoaG46vOeZgxLFadfbC3+Pab15tv+uXnB/N+TAHFzOcfxRiDKzA8
      CT8nOsZ0T6RSb1LriQoiK7uSWk0GQ5QzEXa2eoUP8QKBgQD2e039GwJg55Mai+XF
      epnBmbM7cTTSRiJ1aCYGqpHAysY5OlNjyVS4iMjcrU4m2e8EdHUTbRbgBfzphdCI
      G2vmmoQih5sF1oEHBY//lFFzecd3bXAav3EiQjLkdHYVTKNAafz2+TQenbZoE9+i
      7Y0uAoxmrmp2I02VwJnlnRzzqQKBgQDkT8zjYoPJI/Aq6vBID1BJlsmjjIZOEeS6
      4xiluZPk9DlD16h8GEpT6qMkjke6r4P9k6VsQojQbbfzrfWls/oI5VlG/lY36u0x
      s2ViF/6ms7G6feEzptgLfOUGcWZbBzZmnre4XvTVJsd/Vv8Nf+bFzPT6yTiNqzJd
      ZKajzrHorwKBgQCGSO1XXPSxyNiLGPbI3R+Rq2RLmUSW3uD9KA23igw4hgDywsUV
      02VV2Cfemoi+f/NJcQ0r1T4mNPzyWRYgYRgrHbmpeknKJ3BKXAx0yOCLUiiK91Oh
      LvW7HWfOsWh3+DXk7RmFkIWFvJTZJSKtCFDnZToK4zFbRaaKVNFDmJJiaQKBgCNL
      Hr4r1Xl7f73ysZBCJcQqdiM9LunUpEornvzHclBbXJrbNKVyT9MnG28kO3xlc374
      mwLgSiwGOgCGbMnfx5AmfCWYXZWpUF91fv+p++ijMJ9GpFCKwy0LcpALaao6tDMq
      5/WtYCWvJ/nMZgMdOXxsUZaPEpn70/5R99yssB3rAoGBAKIygxn16woSy4eKe/W2
      +Zcy6N0VcvlCMe2S5LYkbst72i+YbfNOvm2aqjjwRvACE12nW7pFyEc7P77pANog
      48EVotJh6VQ5UA1wp4WtAj8RiUZ3iEaKnehofKQ0sSBsfH8cY7XPZjQz7dYNcZnS
      +UOQLugh6UUIxRXcn5crOgvv
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

