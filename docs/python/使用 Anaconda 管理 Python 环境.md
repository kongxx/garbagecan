# 使用 Anaconda 管理 Python 环境

## 介绍
Anaconda是一个用于科学计算的Python发行版，支持 Linux, Mac, Windows系统，提供python环境管理和包管理功能，可以很方便在多个版本python之间切换和管理第三方包，Anaconda 使用 conda 来进行 Python 环境管理和包管理。

## 安装

可以从 Anaconda 官方网站下载 https://www.anaconda.com/。

这里安装的是Linux版本
``` shell
./Anaconda2-5.0.0.1-Linux-x86_64.sh
```

安装完成后，Anaconda 会在 ~/.bashrc 文件中把 Anaconda2 的路径加到 PATH 里。如下：
``` bash
# added by Anaconda2 installer
export PATH="/apps/anaconda2/bin:$PATH"
```

## 管理 python 环境

### 查看当前使用的 Python 版本

``` shell
$ python -V
Python 2.7.13 :: Anaconda, Inc.

$ conda info --envs
# conda environments:
#
python27                 /apps/anaconda2/envs/python27
root                  *  /apps/anaconda2
```

### 创建新的 Python 版本环境

这里创建了两个 Python 环境分别使用 2.6。x 和 3.5.x
``` shell
$ conda create --name python35 python=3.5
$ conda create --name python26 python=2.6
```

查看 Python 环境
``` shell
$ conda info --envs
# conda environments:
#
python26                 /apps/anaconda2/envs/python26
python27                 /apps/anaconda2/envs/python27
python35                 /apps/anaconda2/envs/python35
root                  *  /apps/anaconda2
```

其中 * 号表示当前使用的 Python 环境

### 切换 Python 环境

``` shell
$ source activate python35

$ python -V
Python 3.5.4 :: Anaconda, Inc.

$ conda info --envs
# conda environments:
#
python26                 /apps/anaconda2/envs/python26
python27                 /apps/anaconda2/envs/python27
python35              *  /apps/anaconda2/envs/python35
root                     /apps/anaconda2
```

如果不想使用当前版本，而想恢复到默认版面，那么
``` shell
$ source deactivate
```

### 删除 Python 环境
``` shell
$ conda remove --name python26 --all
```

## 管理包

### 查看当前已经安装的包
``` shell
$ conda list
# packages in environment at /apps/anaconda2/envs/python35:
#
ca-certificates           2017.08.26           h1d4fec5_0
certifi                   2017.7.27.1      py35h19f42a1_0
libedit                   3.1                  heed3624_0
libffi                    3.2.1                h4deb6c0_3
libgcc-ng                 7.2.0                h7cc24e2_2
libstdcxx-ng              7.2.0                h7a57d05_2
ncurses                   6.0                  h06874d7_1
openssl                   1.0.2l               h077ae2c_5
pip                       9.0.1            py35haa8ec2a_3
python                    3.5.4               he2c66cf_20
readline                  7.0                  hac23ff0_3
setuptools                36.5.0           py35ha8c1747_0
sqlite                    3.20.1               h6d8b0f3_1
tk                        8.6.7                h5979e9b_1
wheel                     0.29.0           py35h601ca99_1
xz                        5.2.3                h2bcbf08_1
zlib                      1.2.11               hfbfcf68_1
```

### 安装包

``` shell
conda install -n python35 psutil
```

### 删除包

``` shell
$ conda uninstall -n python35 psutil
```