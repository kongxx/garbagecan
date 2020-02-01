# Prometheus学习之机器监控

Prometheus 使用 exporter 工具来暴露主机和应用程序上的指标。今天我们就使用 node_exporter 来收集各种主机指标数据（如：CPU、 内存和磁盘等）。

## 安装node_exporter

从　Prometheus　的官网下载安装包，这里下载的是　Linux 安装包。
下载地址： https://prometheus.io/download/
安装包： node_exporter-0.18.1.linux-amd64.tar.gz

``` shell
$ tar zxvf node_exporter-0.18.1.linux-amd64.tar.gz
$ cd node_exporter-0.18.1.linux-amd64/
$ ./node_exporter --version
node_exporter, version 0.18.1 (branch: HEAD, revision: 3db77732e925c08f675d7404a8c46466b2ece83e)
  build user:       root@b50852a1acba
  build date:       20190604-16:41:18
  go version:       go1.12.5
```

## 运行node_exporter

直接运行 node_exporter 命令即可启动服务，此时会打印出当前启用了那些指标收集，如下：

``` shell
$ ./node_exporter 
INFO[0000] Starting node_exporter (version=0.18.1, branch=HEAD, revision=3db77732e925c08f675d7404a8c46466b2ece83e)  source="node_exporter.go:156"
INFO[0000] Build context (go=go1.12.5, user=root@b50852a1acba, date=20190604-16:41:18)  source="node_exporter.go:157"
INFO[0000] Enabled collectors:                           source="node_exporter.go:97"
INFO[0000]  - arp                                        source="node_exporter.go:104"
INFO[0000]  - bcache                                     source="node_exporter.go:104"
INFO[0000]  - bonding                                    source="node_exporter.go:104"
INFO[0000]  - conntrack                                  source="node_exporter.go:104"
INFO[0000]  - cpu                                        source="node_exporter.go:104"
INFO[0000]  - cpufreq                                    source="node_exporter.go:104"
INFO[0000]  - diskstats                                  source="node_exporter.go:104"
INFO[0000]  - edac                                       source="node_exporter.go:104"
INFO[0000]  - entropy                                    source="node_exporter.go:104"
INFO[0000]  - filefd                                     source="node_exporter.go:104"
INFO[0000]  - filesystem                                 source="node_exporter.go:104"
INFO[0000]  - hwmon                                      source="node_exporter.go:104"
INFO[0000]  - infiniband                                 source="node_exporter.go:104"
INFO[0000]  - ipvs                                       source="node_exporter.go:104"
INFO[0000]  - loadavg                                    source="node_exporter.go:104"
INFO[0000]  - mdadm                                      source="node_exporter.go:104"
INFO[0000]  - meminfo                                    source="node_exporter.go:104"
INFO[0000]  - netclass                                   source="node_exporter.go:104"
INFO[0000]  - netdev                                     source="node_exporter.go:104"
INFO[0000]  - netstat                                    source="node_exporter.go:104"
INFO[0000]  - nfs                                        source="node_exporter.go:104"
INFO[0000]  - nfsd                                       source="node_exporter.go:104"
INFO[0000]  - pressure                                   source="node_exporter.go:104"
INFO[0000]  - sockstat                                   source="node_exporter.go:104"
INFO[0000]  - stat                                       source="node_exporter.go:104"
INFO[0000]  - textfile                                   source="node_exporter.go:104"
INFO[0000]  - time                                       source="node_exporter.go:104"
INFO[0000]  - timex                                      source="node_exporter.go:104"
INFO[0000]  - uname                                      source="node_exporter.go:104"
INFO[0000]  - vmstat                                     source="node_exporter.go:104"
INFO[0000]  - xfs                                        source="node_exporter.go:104"
INFO[0000]  - zfs                                        source="node_exporter.go:104"
INFO[0000] Listening on :9100                            source="node_exporter.go:170"
```

服务启动后，可以通过浏览器访问 http://<host>:9100 查看收集的指标。

上面这么多参数，如果我们不想收集某个指标，可以在启动服务的时候使用 --no-collector.xxx 来指定。比如 “./node_exporter --no-collector.zfs” 指定不收集 zfs 指标。

## 配置Prometheus

node_exporter 服务启动后，需要将其添加到 Prometheus 的配置中才能使其生效。现修改 prometheus.yml 文件，在 scrape_configs 下添加
``` shell
scrape_configs:
  ...
  - job_name: 'node'
    static_configs:
    - targets: ['localhost:9100']
```

修改后完整的 prometheus.yml 文件内容如下：

``` shell
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
```

## 启动Prometheus

修改配置文件后，需要重新启动 Prometheus 服务。服务启动后，在浏览器通过访问 http://localhost:9090 来查看监控信息。

此时，我们可以通过输入 “{instance="localhost:9100",job="node"}” 来过滤只显示新加入的指标。

比如：输入 node_cpu_seconds_total{instance="localhost:9100",job="node"} 可以查看节点 CPU 监控指标。

``` shell
Element     Value
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="idle"}    3653653.37
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="iowait"}  5653.09
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="irq"} 0
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="nice"}    5.95
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="softirq"} 155.15
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="steal"}   0
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="system"}  14571.01
node_cpu_seconds_total{cpu="0",instance="localhost:9100",job="node",mode="user"}    16084.06
```
