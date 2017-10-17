# TensorFlow 分布式集群 Local Server

在 TensorFlow 中提供了一个简单分布式的服务（Local Server）功能，利用 Local Server 服务，可以运行一个简单的分布式程序。Local Server 实现了与和分布式服务一样的接口，因此对于开发测试还是比较方便的。

下面是最简单的例子

``` python
import tensorflow as tf
server = tf.train.Server.create_local_server()
with tf.Session(server.target) as session:
    matrix1 = tf.constant([[3., 3.]])
    matrix2 = tf.constant([[2.],[2.]])
    product = tf.matmul(matrix1, matrix2)
    result = session.run(product)
    print result
```

其中，tf.train.Server.create_local_server() 方法创建了一个单一的进程集群。

此外，运行这个程序，实际上是 server 和 client 启动在了同一个进程了。
