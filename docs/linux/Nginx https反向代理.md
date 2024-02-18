# Nginx https反向代理

接前一篇文章，今天看看https的反向代理怎么配置。

## 生成自签名证书和私钥

要使用https，首先需要有证书和私钥，这里创建一个测试用的自签名证书和私钥。

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

## 配置反向代理

这里还是使用前一篇文章中使用的python3的 http server 作为后端应用服务。

``` shell
$ python3 -m http.server 8000
```

修改 /etc/nginx/nginx.conf 文件，添加反向代理配置

``` shell
    server {
		listen       443 ssl;
		server_name  localhost;
		
		ssl_certificate      /etc/nginx/server.crt;
		ssl_certificate_key  /etc/nginx/server.key;
		
		ssl_session_timeout  5m;
		
		ssl_protocols  TLSv1 TLSv1.1 TLSv1.2;
		ssl_prefer_server_ciphers   on;
		
		location / {
			proxy_pass              http://localhost:8000;
			proxy_http_version      1.1;
			proxy_set_header        Connection "";
			proxy_set_header        Host            $host;
			proxy_set_header        X-Real-IP       $remote_addr;
			proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
			proxy_set_header        X-Forwarded-Proto https;
		}
	}
```

修改后完整的 /etc/nginx/nginx.conf 文件内容如下：

``` shell
user www-data;
worker_processes auto;
pid /run/nginx.pid;
include /etc/nginx/modules-enabled/*.conf;

events {
    worker_connections 768;
}

http {
    sendfile on;
    tcp_nopush on;
    types_hash_max_size 2048;

    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3;
    ssl_prefer_server_ciphers on;

    access_log /var/log/nginx/access.log;
    error_log /var/log/nginx/error.log;

    gzip on;

    server {
        listen 80;
        server_name localhost;
        location / {
            proxy_pass http://localhost:8000;
        }
    }

    server {
		listen       443 ssl;
		server_name  localhost;
		
		ssl_certificate      /etc/nginx/server.crt;
		ssl_certificate_key  /etc/nginx/server.key;
		
		ssl_session_timeout  5m;
		
		ssl_protocols  TLSv1 TLSv1.1 TLSv1.2;
		ssl_prefer_server_ciphers   on;
		
		location / {
			proxy_pass              http://localhost:8000;
			proxy_http_version      1.1;
			proxy_set_header        Connection "";
			proxy_set_header        Host            $host;
			proxy_set_header        X-Real-IP       $remote_addr;
			proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
			proxy_set_header        X-Forwarded-Proto https;
		}
	}

    include /etc/nginx/conf.d/*.conf;
    include /etc/nginx/sites-enabled/*;
}
```

重新启动服务使配置生效

``` shell
$ sudo service nginx restart
```

最后通过访问 "https://localhost" 地址检查请求是否被代理到后端的python应用上了。
