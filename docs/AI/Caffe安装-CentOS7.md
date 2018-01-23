# CentOS7安装Caffe

## 安装依赖包
``` shell
sudo yum install epel-release
sudo yum install gcc-c++ protobuf-devel leveldb-devel snappy-devel opencv-devel boost-devel hdf5-devel
sudo yum install gflags-devel glog-devel lmdb-devel
sudo yum install openblas-devel
```

## 安装caffe

``` shell
wget -c https://github.com/BVLC/caffe/archive/1.0.tar.gz
tar zxvf 1.0.tar.gz
cd caffe-1.0
cp Makefile.config.example Makefile.config
```

由于我只是测试一下，机器没有GPU，所以需要使用CPU_ONLY模式，编辑Makefile.config，打开下面行
``` shell
CPU_ONLY := 1
```

然后，编译安装
``` shell
make all
make test
make runtest
```

## 问题

### 问题一

编译的时候出现下面的错误
```shell
./include/caffe/util/mkl_alternate.hpp:14:19: fatal error: cblas.h: No such file or directory
```

解决办法
```shell
sudo yum install liblas-devel atlas-devel
```

### 问题二

编译的时候出现下面的错误
```shell
/usr/bin/ld: cannot find -lcblas
/usr/bin/ld: cannot find -latlas
```

此问题对于Ubuntu开始可以通过安装 libatlas-base-dev 包解决，如下：
```shell
sudo apt install libatlas-base-dev
```

但是对于CentOS7，没有这个包，那么可以通过修改编译参数使用openblas解决，编辑 Makefile.config 文件，
```shell
修改前
BLAS := atlas
修改后
BLAS := open
```

然后，重新执行
``` shell
make all
make test
make runtest
```
