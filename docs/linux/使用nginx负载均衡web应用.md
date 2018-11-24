# 使用nginx负载均衡web应用

## 安装

首先需要在系统上安装nginx软件，这里使用Ubuntu的apt来安装。

``` shell
sudo apt-get install nginx
```

## 配置

安装完成后，我们首先需要修改一个ngins的配置。

这里假定我们已经有了个两个相同的web应用，分别使用的 8081 和 8082 端口。我们通过nginx的8080端口代理这两个web应用。

修改 /etc/nginx/nginx.conf 文件，在其中的 http 部分最后做如下配置：


``` shell
...

http {
    ...
    ########################################
    # 注释部分
    # include /etc/nginx/sites-enabled/*;	
    ########################################

    ########################################
    # 增加部分
    upstream myhost {
            server localhost:8081 weight=1;
            server localhost:8082 weight=1;
    }
    server {
            listen 8080;
            location / {
                    proxy_pass http://myhost;
            }
    }
    ########################################
}
```

## 运行

启动 nginx 服务

``` shell
sudo /etc/init.d/nginx start
```

然后通过浏览器访问 http://<ip>:8080 地址来验证配置是否生效。