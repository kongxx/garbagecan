# AWS Elastic Beanstalk通过应用负载均衡配置https

接上一篇，今天说说怎么通过AWS Elastic Beanstalk提供的应用负载均衡配置https。

首先创建应用和环境，这里应用可以使用上一篇文章中使用的demo应用（只需要package.json和app.js文件）

创建环境的时候，确认下面两个参数配置，其它参数按需要设置就好
``` shell
预设资源：高可用性
负载均衡器类型：负载均衡器类型，专用
```

环境创建好之后，可以先访问验证一下环境的http是否工作正常。

## 导入证书
要配置https，需要选择证书，所以需要现在 AWS Certificate Manager (ACM) 中导入证书和私钥。

## 配置https

修改环境配置 -> 实例流量和扩缩 -> 侦听器 -> 添加侦听器
侦听器端口: 443
侦听器协议: HTTPS
SSL 证书: <选择上一步导入的证书>
SSL 策略: <选择一个较新的>
默认进程: default

保存后，等到环境更新成功后，访问 https://xxx.elasticbeanstalk.com/ 进行验证。

另外，AWS Elastic Beanstalk也可以通过配置文件来配置安全侦听器，这个后面有时间我再试试。
