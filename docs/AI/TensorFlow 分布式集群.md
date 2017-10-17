# TensorFlow 分布式集群

上一篇博客说了怎样创建一个 Local Server 的集群，今天说说怎样创建一个真正的分布式集群。

我们准备了两个机器，如下：

``` shell
192.168.0.192
192.168.0.193
```

我们将使用这两个机器来组成一个集群，然后把 tensorflow task 扔到其中的某个节点上运行。

- 我们准备了两个 server 程序，用来分别在两个机器上启动来组成一个集群，并接收task。
- 创建一个 client 程序，用来向集群提交 task。

## server1.py

``` python
import sys
import time
import tensorflow as tf

try:
    worker1 = "192.168.0.192:8881"
    worker2 = "192.168.0.193:8881"
    worker_hosts = [worker1, worker2]
    cluster_spec = tf.train.ClusterSpec({ "worker": worker_hosts})
    server = tf.train.Server(cluster_spec, job_name="worker", task_index=0)
    time.sleep(600)
except KeyboardInterrupt:
    sys.exit()
```

## server2.py

``` python
import sys
import time
import tensorflow as tf

try:
    worker1 = "192.168.0.192:8881"
    worker2 = "192.168.0.193:8881"
    worker_hosts = [worker1, worker2]
    cluster_spec = tf.train.ClusterSpec({ "worker": worker_hosts})
    server = tf.train.Server(cluster_spec, job_name="worker", task_index=1)
    time.sleep(600)
except KeyboardInterrupt:
    sys.exit()
```

## client.py

``` python
import tensorflow as tf
with tf.Session("grpc://192.168.0.192:8881") as session:
    with tf.device("/job:worker/task:0"):
        matrix1 = tf.constant([[3., 3.]])
        matrix2 = tf.constant([[2.],[2.]])
        product = tf.matmul(matrix1, matrix2)
        result = session.run(product)
        print result
```

## 测试
- 在 192.168.0.192 上运行 “python server1.py”
- 在 192.168.0.193 上运行 “python server2.py”
- 在任意一台机器上运行 “python client.py”
