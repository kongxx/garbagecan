# 使用frp通过http访问内网web服务

frp 是一个可用于内网穿透的高性能的反向代理应用，支持 tcp, udp 协议，为 http 和 https 应用协议提供了额外的能力。

## 服务端

### 配置服务端

编辑 frps.toml 文件

``` shell
bindAddr = "0.0.0.0"
bindPort = 7000
vhostHTTPPort = 8080
```

### 启动服务端

``` shell
./frps -c frps.toml
```

## 客户端

### 配置客户端

``` shell
serverAddr = "xx.xx.xx.xx"
serverPort = 7000

[[proxies]]
name = "web"
type = "http"
localPort = 8080
customDomains = ["xx.xx.xx.xx"]
```

其中 serverAddr = "xx.xx.xx.xx" 配置的是公网地址。

### 启动客户端

首先在本地启动一个web服务，这里使用python的http.server。

``` shell
python3 -m http.server 8080
```

然后启动frp客户端

``` shell
./frpc -c frpc.toml
```

### 验证

通过浏览器访问 http://xx.xx.xx.xx:8080/
