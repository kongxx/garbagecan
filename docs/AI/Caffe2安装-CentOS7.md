# Caffe2安装 - CentOS7

## 安装epel

``` shell
$ sudo yum install epel-release
```

## 安装依赖的系统库

``` shell
$ sudo yum update
$ sudo yum install -y \
automake \
cmake3 \
gcc \
gcc-c++ \
git \
kernel-devel \
leveldb-devel \
lmdb-devel \
libtool \
protobuf-devel \
python-devel \
python-pip \
snappy-devel \
gflags-devel \
glog-devel
```

## 安装依赖的 Python 库

``` shell
$ sudo pip install --upgrade pip
$ sudo pip install \
flask \
future \
graphviz \
hypothesis \
jupyter \
matplotlib \
numpy \
protobuf \
pydot \
python-nvd3 \
pyyaml \
requests \
scikit-image \
scipy \
setuptools \
six \
tornado
```

## 编译安装 caffe2

``` shell
$ git clone --recursive https://github.com/caffe2/caffe2
$ cd caffe2 && mkdir build
$ cd build && cmake3 ..
$ sudo make install
```

## 测试

首先使用下面的命令来检查 caffe2 是否安装成功

```shell
$ cd build
$ python -c 'from caffe2.python import core' 2>/dev/null && echo "Success" || echo "Failure"
```

如果 caffe2 安装成功，上面的命令应该打印 Success。

其次使用下面的命令来运行一个测试

``` shell
$ cd build
$ python -m caffe2.python.operator_test.relu_op_test
$ python -m caffe2.python.examples.resnet50_trainer --train_data null --use_cpu 
```

## 问题

### 问题一

运行 “python -m caffe2.python.operator_test.relu_op_tes” 报错

``` shell
...
Traceback (most recent call last):
  File "/usr/lib64/python2.7/runpy.py", line 162, in _run_module_as_main
    "__main__", fname, loader, pkg_name)
  File "/usr/lib64/python2.7/runpy.py", line 72, in _run_code
    exec code in run_globals
  File "/home/jhadmin/Downloads/caffe2/build/caffe2/python/operator_test/relu_op_test.py", line 22, in <module>
    from hypothesis import given
  File "/usr/lib/python2.7/site-packages/hypothesis/__init__.py", line 31, in <module>
    from hypothesis.core import given, find, example, seed, reproduce_failure, \
  File "/usr/lib/python2.7/site-packages/hypothesis/core.py", line 34, in <module>
    from coverage.files import canonical_filename
ImportError: cannot import name canonical_filename
```

问题原因是 Python 的 coverage 版本问题，升级 coverage 库

``` shell
sudo pip install coverage --upgrade
```

### 问题二

运行 “python -m caffe2.python.operator_test.relu_op_tes” 报错

``` shell
AttributeError: 'module' object has no attribute 'full'
```

问题原因是 numpy 版本问题，升级 numpy 库

``` shell
sudo pip install numpy --upgrade
```
