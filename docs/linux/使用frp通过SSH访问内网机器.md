# 使用frp通过SSH访问内网机器

frp是一个开源的内网穿透反向代理工具，支持传输层的tcp/udp协议，也支持应用层的http/https协议。

## 服务端

### 服务端下载安装

在有公网地址的机器上下载并解压。

``` shell
wget -c https://github.com/fatedier/frp/releases/download/v0.58.0/frp_0.58.0_linux_amd64.tar.gz 
tar zxvf frp_0.58.0_linux_amd64.tar.gz
```

### 服务端配置

编辑配置文件 frps.toml

``` shell
bindAddr = "0.0.0.0"
bindPort = 7000
```

### 启动服务端

``` shell
./frps -c frps.toml
```

## 客户端

### 客户端下载安装

客户端和服务端使用相同的安装包。在内网机器上下载并解压。

### 客户端配置

编辑配置文件 frpc.toml

``` shell
serverAddr = "xx.xx.xx.xx"
serverPort = 7000

[[proxies]]
name = "ssh"
type = "tcp"
localIP = "127.0.0.1"
localPort = 22
remotePort = 6000
```

其中 serverAddr = "xx.xx.xx.xx" 配置的是公网地址。

### 启动客户端

``` shell
./frpc -c frpc.toml
```

### 验证

``` shell
ssh xx.xx.xx.xx -p 6000
```
