# 使用frp的token认证

frp在客户端的连接服务端时支持 token 和 oidc 认证，如果使用 token 认证，需要在服务端和客户端添加如下配置内容。

``` shell
auth.method = "token"
auth.token = "Letmein"
```

完整服务端配置类似

``` shell
bindAddr = "0.0.0.0"
bindPort = 7000
vhostHTTPPort = 8080

auth.method = "token"
auth.token = "Letmein"
```

完整客户端配置类似

``` shell
serverAddr = "xx.xx.xx.xx"
serverPort = 7000

auth.method = "token"
auth.token = "Letmein"

[[proxies]]
name = "web"
type = "http"
localPort = 8080
customDomains = ["xx.xx.xx.xx"]
```
