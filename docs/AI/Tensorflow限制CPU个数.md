# Tensorflow限制CPU个数

## 安装

这里使用 Pip 来安装 Tensorflow CPU 版

``` shell
$ sudo pip install https://storage.googleapis.com/tensorflow/linux/cpu/tensorflow-0.5.0-cp27-none-linux_x86_64.whl
```

安装完成后运行库中自带的手写识别例子来检查安装是否成功

``` shell
$ cd /usr/lib/python2.7/site-packages/tensorflow/models/image/mnist
$ python convolutional.py
...
```

或者运行

``` shell
$ python -m tensorflow.models.image.mnist.convolutional
...
```

## 限制CPU个数

对于上面用到的手写识别例子来说，需要修改文件 /usr/lib/python2.7/site-packages/tensorflow/models/image/mnist/convolutional.py 中创建 Session 部分

``` python
修改前
    with tf.Session(config=config) as s:
修改后
    cpu_num = int(os.environ.get('CPU_NUM', 1))
    config = tf.ConfigProto(device_count={"CPU": cpu_num},
                inter_op_parallelism_threads = cpu_num,
                intra_op_parallelism_threads = cpu_num,
                log_device_placement=True)

    with tf.Session(config=config) as s:
```

修改完成后，使用环境变量 CPU_NUM 来指定需要使用的 CPU 个数，然后再次运行手写识别例子

``` shell
$ export CPU_NUM=2
$ python -m tensorflow.models.image.mnist.convolutional
```

运行后，使用 top 命令来查看程序的 CPU 使用情况。
