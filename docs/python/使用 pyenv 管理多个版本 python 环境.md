# 使用 pyenv 管理多个版本 python 环境

随着同时开发的项目越来越多，需要不停的在各个不同版本的 python 环境之间切换，所以想到了pyenv。以前一直使用的 virtualenv只能管理同一个 python 版本下第三方库的版本，但是对于这种需要在多个不同版本之间切换的 case，就只能使用 pyenv 了。

## 安装

运行下面的命令会自动下载安装

``` shell
$ curl -L https://raw.githubusercontent.com/pyenv/pyenv-installer/master/bin/pyenv-installer | bash
```

安装完成后，需要修改 ~/.bashrc 文件，把pyenv加到 PATH 里。这里是添加如下行到 ~/.bashrc 文件中。

``` bash
export PATH="/home/kongxx/.pyenv/bin:$PATH"
eval "$(pyenv init -)"
eval "$(pyenv virtualenv-init -)"
```

然后 source 一下环境
``` bash
$ .~/.bashrc
```

## 使用 pyenv 管理 python 版本

### 首先查看当前系统上已经安装和正在使用的 python 版本。

``` shell
$ pyenv versions
* system (set by /home/kongxx/.pyenv/version)
```

其中 system 表明是系统安装的包。* 表示当前正在使用的 python 环境。

### 查看当前可以被安装的 python 版本。

``` shell
$ pyenv install -l
Available versions:
  2.1.3
  2.2.3
  2.3.7
  2.4
  2.4.1
...
```

###  安装指定版本的 python
``` shell
$ pyenv install 2.7.10
Downloading Python-2.7.10.tar.xz...
-> https://www.python.org/ftp/python/2.7.10/Python-2.7.10.tar.xz
Installing Python-2.7.10...
patching file ./Lib/site.py
Installed Python-2.7.10 to /home/kongxx/.pyenv/versions/2.7.10

$ pyenv install 3.2.1
...
```

安装后查看现在已经安装了的 python 版本。

``` shell
$ pyenv versions
* system (set by /home/kongxx/.pyenv/version)
  2.7.10
  3.2.1
```

### 使用制定版本的 python
``` shell
$ pyenv global 3.2.1
$ python -V
Python 3.2.1
```

使用后查看当前使用的 python 版本。
``` shell
$ pyenv version
3.2.1 (set by /home/kongxx/.pyenv/version)

$ pyenv versions
  system
  2.7.10
* 3.2.1 (set by /home/kongxx/.pyenv/version)
```

## 使用 pyenv 管理 virtualenv

### 创建一个 virtualenv 环境

这里使用 python 3.2.1 来创建一个 virtualenv 环境

``` shell
$ pyenv virtualenv 3.2.1 myenv

$ pyenv versions
  system
  2.7.10
* 3.2.1 (set by /home/kongxx/.pyenv/version)
  3.2.1/envs/myenv
  myenv
```

### 激活当前需要使用的 virtualenv

``` shell
$ pyenv activate myenv

$ pyenv versions
  system
  2.7.10
  3.2.1
  3.2.1/envs/myenv
* myenv (set by PYENV_VERSION environment variable)
```

### 去除当前需要使用的 virtualenv
``` shell
$ pyenv deactivate
```

### 删除当前需要使用的 virtualenv
``` shell
$ pyenv virtualenv-delete myenv
```
