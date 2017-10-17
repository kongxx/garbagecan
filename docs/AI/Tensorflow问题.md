# pip不能安装Tensorflow问题

在 Linux 上使用 pip 安装 tensorflow 的时候，提示找不到匹配的包
``` shell
$ pip install tensorflow
Downloading/unpacking tensorflow
  Could not find any downloads that satisfy the requirement tensorflow
Cleaning up...
No distributions at all found for tensorflow
Storing complete log in /home/kongxx/.pip/pip.log
```

检查 pip.log 文件，是由于 pip 不认识 .whl 类型的文件。

$ cat /home/kongxx/.pip/pip.log
``` shell
...
Skipping link https://pypi.python.org/packages/fe/dd/8764ae59e8ff74421d615ddb9c86a1b404c27708dfde3caa8f17c183788d/tensorflow-1.3.0-cp27-cp27mu-manylinux1_x86_64.whl#md5=e82e309e6af0996f2083f59cf21d392c (from https://pypi.python.org/simple/tensorflow/); unknown archive format: .whl
Could not find any downloads that satisfy the requirement tensorflow
```

这个问题的原因是 pip 版本太低，所以需要升级 pip
``` shell
$ pip install pip -U
```

然后再次安装
``` shell
$ pip install tensorflow
```
