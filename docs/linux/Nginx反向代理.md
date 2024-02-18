# Nginx反向代理

Nginx的一个常见使用场景是反向代理，可以用来隐藏后端服务地址或者做负载均衡使用。下面就来看看怎么实现一个最简单的反向代理。

## 安装Nginx

``` shell
$ sudo apt install nginx
```

安装完成后，可以使用下面命令查看服务状态

``` shell
$ sudo service nginx status
```

如果服务状态是运行中，可以通过访问 http://localhost 来检查效果。

## 配置反向代理

这里假定我们有一个后端的服务，这里的服务可以是java、python或nodejs的web app，这里使用python3的http server做模拟，创建一个空目录，并在其下创建一个index.html文件，内容随意，然后使用下面命令启动服务

``` shell
$ python3 -m http.server 8000
```

然后访问 “http://localhost:8000” 来验证模拟服务状态。

修改 /etc/nginx/nginx.conf 文件，添加反向代理配置

``` shell
    server {
        listen 80;
        server_name localhost;
        location / {
            proxy_pass http://localhost:8000;
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

    include /etc/nginx/conf.d/*.conf;
    include /etc/nginx/sites-enabled/*;
}
```

重新启动服务使配置生效

``` shell
$ sudo service nginx restart
```

最后通过访问 "http://localhost" 地址检查请求是否被代理到后端的python应用上了。
