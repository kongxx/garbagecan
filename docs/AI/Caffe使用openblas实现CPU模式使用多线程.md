Caffe使用openblas实现CPU模式使用多线程

在Caffe的默认编译安装使用的是ATLAS库，但是这个版本的BLAS不能利用多核CPU，要使用多核CPU并行计算来加速Caffe则需要使用OpenBLAS。下面就说说怎样来使用OpenBLAS。

在默认编译Caffe后，我们使用“ldd build/tools/caffe”命令查看时可以看到使用的是openblas的单线程版本，如下：

``` shell
$ ldd build/tools/caffe | grep openblas
	libopenblas.so.0 => /lib64/libopenblas.so.0 (0x00007f1fe656f000)
```

如果要使用openblas的多线程版本，此时应该看到类似下面的结果，其中so文件最后的“p”即表示是多线程版本。

``` shell
$ ldd build/tools/caffe | grep openblas
  libopenblasp.so.0 => /lib64/libopenblasp.so.0 (0x00007f0854b90000)
```

下面我们就看看应该怎样编译使用多线程版本OpenBLAS来编译caffe。

## 编译

首先，修改 “Makefile.config” 文件，将其中

``` shell
BLAS := atlas

改为

BLAS := open
```

同时修改其中 BLAS_INCLUDE 和 BLAS_LIB 参数，修改如下：

``` shell
BLAS_INCLUDE := /usr/include/openblas
BLAS_LIB := /usr/lib64/libopenblasp.so
```

然后，修改 “Makefile”  文件，将其中

``` shell
	LIBRARIES += openblas
改为
	LIBRARIES += openblasp
```

修改完上面两个文件后，重新编译caffe

``` shell
make clean
make all
make test
make runtest
```

编译完成后，使用ldd检查caffe文件，可以看到已经使用多线程版本的的openblas了，如下：

``` shell
$ ldd build/tools/caffe | grep openblas
	libopenblasp.so.0 => /lib64/libopenblasp.so.0 (0x00007f0854b90000)
```

## 测试

我们跑个训练模型来验证一下，要让caffe使用指定的CPU个数，我们可以通过设置环境变量 OPENBLAS_NUM_THREADS 来实现。如下：

``` shell
$ export OPENBLAS_NUM_THREADS=2
```

然后我们需要先下载一下训练用的数据，在caffe的根目录下运行下面的命令来准备数据。

``` shell
$ ./data/mnist/get_mnist.sh
$ ./examples/mnist/create_mnist.sh
```

修改配置使其使用CPU模式运行训练模型，编辑 examples/mnist/lenet_solver.prototxt 文件。

``` shell
将其中

solver_mode: GPU

修改为

solver_mode: CPU
```

运行训练模型

``` shell
$ ./examples/mnist/train_lenet.sh
```

待训练程序启动后，使用 top 命令观察进程的 CPU 使用情况，由于上面我设置了 OPENBLAS_NUM_THREADS=2 ， 所以此时进程的CPU利用率大约会是200%左右。
