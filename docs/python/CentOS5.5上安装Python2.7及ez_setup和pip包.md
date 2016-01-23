# CentOS5.5上安装Python2.7及ez_setup和pip包

## 下载

首先从Python官方下载源码包[下载](https://www.python.org/ftp/python/2.7.10/Python-2.7.10.tgz)

## 编译安装

这里将python安装到/opt/python27目录下

``` bash
tar xvf Python-2.7.10.tgz
./configure --prefix=/opt/python27 --enable-shared
make
sudo make install
```

安装完成后，为了避免有时候运行python报下面的错误
``` bash
/opt/python27/bin/python: error while loading shared libraries: libpython2.7.so.1.0: cannot open shared object file: No such file or directory
```

需要在/etc/ld.so.conf.d/目录下新建python27.conf文件，将python2.7的库目录写入此文件，内容如下：
``` bash
/opt/python27/lib
```

然后运行
``` bash
sudo /sbin/ldconfig
```

此时可以运行下面的命令来测试安装是否成功，也可以将其加到自己环境变量PATH里，这样就不用每次输入完整路径了。
``` bash
/opt/python27/bin/python
```

## 安装ez_setup
``` bash
wget http://peak.telecommunity.com/dist/ez_setup.py
sudo /opt/python27/bin/python ez_setup.py
```

## 安装pip
``` bash
wget https://bootstrap.pypa.io/get-pip.py
sudo /opt/python27/bin/python get-pip.py
```

安装完成pip之后，可以使用下面的命令来第三方包
``` bash
sudo /opt/python27/bin/pip install pyinstaller
```
