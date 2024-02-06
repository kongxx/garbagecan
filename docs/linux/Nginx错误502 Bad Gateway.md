# Nginx错误502 Bad Gateway

使用Nginx配置的反向代理，浏览器访问的时候出现 “502 Bad Gateway” 错误，检查了一下后台error文件，发现有类似下面的错误

``` shell
2024/02/05 14:21:00 [error] 166605#166605: *11 upstream sent too big header while reading response header from upstream, client: 127.0.0.1, server: , request: "GET /callback?code=oqzcbdgWRQLLeqruzoMZHXa43eYj9d&state=eyJyZXR1cm5UbyI6Imh0dHBzOi8vZmFuYmluLWV6bW0tc2VydmVyLTIuYXdzbWFzODEwLmV6bWF4Y2xvdWQuY29tIn0 HTTP/1.1", upstream: "http://127.0.0.1:8080/callback?code=oqzcbdgWRQLLeqruzoMZHXa43eYj9d&state=eyJyZXR1cm5UbyI6Imh0dHBzOi8vZmFuYmluLWV6bW0tc2VydmVyLTIuYXdzbWFzODEwLmV6bWF4Y2xvdWQuY29tIn0", host: "..."
```

其中 “upstream sent too big header while reading response header from upstream” 说明可能是nginx代理的缓冲区不够，因此需要调整一下缓冲区的配置，主要包括下面几个参数

``` shell
proxy_buffer_size       1024k;
proxy_buffers           64 64k;
proxy_busy_buffers_size 1024k;
```

配置类似如下：

``` shell
...
location / {
    proxy_pass          http://127.0.0.1:8080;
    proxy_http_version  1.1;

    proxy_set_header    Connection          $connection_upgrade;
    proxy_set_header    Upgrade             $http_upgrade;
    proxy_set_header    Host                $host;
    proxy_set_header    X-Real-IP           $remote_addr;
    proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;

    proxy_buffer_size       1024k;
    proxy_buffers           64 64k;
    proxy_busy_buffers_size 1024k;
}
...
```
