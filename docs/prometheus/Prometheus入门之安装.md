# Prometheus入门之安装

##　简介

Prometheus,一个开源的监控系统，它通过获取应用程序中实时时间序列数据，然后根据强大的规则引擎，帮助用户监控机器和应用程序。

## 安装

首先从　Prometheus　的官网下载安装包，这里下载的是　Linux 安装包。
下载地址： https://prometheus.io/download/
安装包： prometheus-2.15.2.linux-amd64.tar.gz

Prometheus 的安装很简单，解压压缩包就可以了，其中包含一个可执行文件 prometheus。

``` shell
$ tar zxvf prometheus-2.15.2.linux-amd64.tar.gz
$ cd prometheus-2.15.2.linux-amd64
$ ./prometheus --version
prometheus, version 2.15.2 (branch: HEAD, revision: d9613e5c466c6e9de548c4dae1b9aabf9aaf7c57)
  build user:       root@688433cf4ff7
  build date:       20200106-14:50:51
  go version:       go1.13.5
```

##　配置

在安装目录下包含一个默认的配置文件 prometheus.yml，启动 Prometheus 需要指定一个配置文件。

``` shell
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration
alerting:
  alertmanagers:
  - static_configs:
    - targets:
      # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'

    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.

    static_configs:
    - targets: ['localhost:9090']
```

配置文件包含四个部分
- global: 包含了控制 Prometheus 服务器行为的全局配置。
  - scrape_interval: 用来指定应用程序或服务抓取数据的时间间隔。
  - evaluation_interval:用来指定Prometheus评估规则的频率。
- alerting：　设置　Prometheus　的警报。
- rule_files：　指定包含记录规则或警报规则的文件列表。
- scrape_config：　指定Prometheus抓取的所有目标。

上面的配置文件只有一个监控目标，即监控　Prometheus　服务器自身。它从本地的9090端口抓取数据并返回
服务器的健康指标。启动　Prometheus　后，也可以通过　http://localhost:9090/metrics　查看　Prometheus　查看其暴露的所有指标。

## 运行

``` shell
$ sudo ./prometheus --config.file=prometheus.yml
```

服务启动后，在浏览器通过访问 http://localhost:9090 来查看监控信息。

我们选择一个指标来看一下，在指标下拉框里选择 go_gc_duration_seconds 可以查看 Prometheus 服务 Go 程序做垃圾收集的时间。

``` shell
Element     Value
go_gc_duration_seconds{instance="localhost:9090",job="prometheus",quantile="0"} 0.00000491
go_gc_duration_seconds{instance="localhost:9090",job="prometheus",quantile="0.25"}  0.000009715
go_gc_duration_seconds{instance="localhost:9090",job="prometheus",quantile="0.5"}   0.000010274
go_gc_duration_seconds{instance="localhost:9090",job="prometheus",quantile="0.75"}  0.000010814
go_gc_duration_seconds{instance="localhost:9090",job="prometheus",quantile="1"} 0.000083706
```

指标中 quantile 标签表示这衡量的是百分位数，后面的数字是这个指标的值。

我们可以通过切换 Console 和 Graph 标签来选择表格和图标方式显示监控信息。

