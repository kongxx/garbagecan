# Prometheus学习之Blackbox

## 简介

Prometheus 的探针监控可以在应用程序的外部对应用程序进行探测，比如：探测机器的 http 服务是否工作正常等。 这里就看看怎样使用 Prometheus 的 Blackbox Exporter 来实现这个功能。

Prometheus 的 Blackbox Exporter 允许通过 HTTP/HTTPS，TCP 和 ICMP 等来探测端点。

## 安装

首先从 https://prometheus.io/download/#blackbox_exporter 地址下载 blackbox_exporter 安装包，我这里使用的是 Linux 安装包 blackbox_exporter-0.16.0.linux-amd64.tar.gz。

``` shell
$ wget -c https://github.com/prometheus/blackbox_exporter/releases/download/v0.16.0/blackbox_exporter-0.16.0.linux-amd64.tar.gz
$ tar zxvf blackbox_exporter-0.16.0.linux-amd64.tar.gz
$ cd blackbox_exporter-*
$ ./blackbox_exporter --version
blackbox_exporter, version 0.16.0 (branch: HEAD, revision: 991f89846ae10db22a3933356a7d196642fcb9a9)
  build user:       root@64f600555645
  build date:       20191111-16:27:24
  go version:       go1.13.4
```

## 运行blackbox_exporter

修改安装目录下的 blackbox.yml 文件，如下：

``` yml
modules:
  http_2xx:
    prober: http
    timeout: 5s
    http:
      valid_status_codes: [200]
      method: GET
  icmp:
    prober: icmp
    timeout: 5s
```

其中：
- http_2xx 配置检查 http 服务是否运行，并返回 2xx 状态码。
- icmp 配置检查 ICMP ping 是否正常。

现在运行使用此配置文件运行 blackbox_exporter

``` shell
$ sudo ./blackbox_exporter --config.file=blackbox.yml
level=info ts=2020-02-16T09:09:46.523Z caller=main.go:212 msg="Starting blackbox_exporter" version="(version=0.16.0, branch=HEAD, revision=991f89846ae10db22a3933356a7d196642fcb9a9)"
level=info ts=2020-02-16T09:09:46.523Z caller=main.go:213 msg="Build context" (gogo1.13.4,userroot@64f600555645,date20191111-16:27:24)=(MISSING)
level=info ts=2020-02-16T09:09:46.523Z caller=main.go:225 msg="Loaded config file"
level=info ts=2020-02-16T09:09:46.524Z caller=main.go:369 msg="Listening on address" address=:9115
```

服务启动后，可以通过浏览器访问 http://localhost:9115/ 来查看状态。

服务启动后，我们可以做一下简单测试，分别访问项目两个链接地址
http://localhost:9115/probe?target=localhost&module=http_2xx
http://localhost:9115/probe?target=localhost&module=icmp

然后在 http://localhost:9115/ 页面查看结果，可以看到类似如下的内容。

``` shell
Module	Target	Result	Debug
http_2xx	localhost	Success	Logs
icmp	localhost	Success	Logs
```

## 启动Prometheus

现在 blackbox_exporter 服务已经运行了，下面就需要添加两个作业 blackbox_http 和 blackbox_icmp 来收集这些指标了，下面是完整的 prometheus.yml 文件内容：

``` yml
global:
  scrape_interval:     15s
  evaluation_interval: 15s

alerting:
  alertmanagers:
  - static_configs:
    - targets:
      # - alertmanager:9093

rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
    - targets: ['localhost:9090']

  - job_name: 'node'
    static_configs:
    - targets: ['localhost:9100']

  - job_name: 'blackbox_http'
    metrics_path: /probe
    static_configs:
      - targets:
        - http://localhost 
        - https://localhost
    relabel_configs:
      - source_labels: [__address__]
        target_label: __param_target
      - source_labels: [__param_target]
        target_label: instance
      - target_label: __address__
        replacement: localhost:9115

  - job_name: 'blackbox_icmp'
    metrics_path: /probe
    static_configs:
      - targets:
        - localhost 
    relabel_configs:
      - source_labels: [__address__]
        target_label: __param_target
      - source_labels: [__param_target]
        target_label: instance
      - target_label: __address__
        replacement: localhost:9115
```

启动 prometheus 服务

``` shell
$ sudo ./prometheus --config.file=prometheus.yml
```

服务启动后，通过 prometheus 监控页面查询 {job='blackbox_http'} 和 {job='blackbox_icmp'} 指标即可看到指标结果。
