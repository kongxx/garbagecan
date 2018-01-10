# Caffe Python开发环境设置

## 安装 Python 依赖的库

由于安装 Python 的第三方库的时候需要编译，所以需要安装下面两个库
``` shell
$ sudo yum install python-devel numpy
```

设置virtualenv环境
``` shell
$ virtualenv caffeenv
$ cd caffeenv
$ . bin/activate
```

安装 Python 第三方库
``` shell
cd <path_to_caffe>/caffe-1.0/python
pip install -r requirements.txt
```

## 编译 pycaffe
``` shell
$ make pycaffe
```

## 测试

首先需要设置环境变量 PYTHONPATH，如下：
``` shell
$ export PYTHONPATH=<path_to_caffe>/caffe-1.0/python:$PYTHONPATH
```

运行python，进入交互模式，然后使用 “import caffe” 来测试环境。
``` shell
import caffe
```

## 问题

在搭建环境的时候也碰到了一些问题，现总结如下：

### 问题一
在 “import caffe” 的时候出现下面错误
``` shell
ImportError: No module named _caffe
```

问题原因是没有将caffe加入到 PYTHONPATH 环境变量里。

解决办法是在运行 python 之前，设置 PYTHONPATH 环境变量。
``` shell
export PYTHONPATH=/home/jhadmin/Downloads/caffe-1.0/python:$PYTHONPATH
```


### 问题二
在 “import caffe” 的时候出现下面错误

``` shell
AttributeError: 'module' object has no attribute 'bool_'
```

解决办法
``` shell
$ make pycaffe
```

### 问题三
在 “import caffe” 的时候出现下面错误
``` shell
CXX/LD -o python/caffe/_caffe.so python/caffe/_caffe.cpp
python/caffe/_caffe.cpp:10:31: fatal error: numpy/arrayobject.h: No such file or directory
 #include <numpy/arrayobject.h>
```

解决办法
``` shell
sudo yum install numpy
```