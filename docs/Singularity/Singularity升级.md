# Singularity升级

## 卸载旧版本

以前装的Singularity版本比较低，最近要用新功能，只能升级了。因为以前Singularity安装的时候是使用自己编译的rpm安装的，所以要安装新版本，需要先卸载已安装的老版本。

查询一下老版本的singularity包

``` shell
$ sudo rpm -qa | grep -i singularity
singularity-2.5.2-1.el7.x86_64
singularity-debuginfo-2.5.2-1.el7.x86_64
singularity-runtime-2.5.2-1.el7.x86_64
singularity-devel-2.5.2-1.el7.x86_64
```

卸载rpm包

``` shell
$ sudo rpm -e singularity-2.5.2-1.el7.x86_64 singularity-debuginfo-2.5.2-1.el7.x86_64 singularity-runtime-2.5.2-1.el7.x86_64 singularity-devel-2.5.2-1.el7.x86_64
```

## 安装新版本

下载源码包

``` shell
$ export VERSION=3.2.1
$ wget https://github.com/sylabs/singularity/releases/download/v${VERSION}/singularity-${VERSION}.tar.gz
```

编译rpm包

注意：由于新版本需要使用Go语言来编译，因此需要安装Go语言开发环境，可以参考后面给的链接来安装依赖包。

``` shell
$ export VERSION=3.2.1
$ rpmbuild -tb singularity-${VERSION}.tar.gz (会提示安装缺少的rpm包)
$ ls -al ~/rpmbuild/RPMS/x86_64/
total 45820
drwxr-xr-x 2 admin admin       98 Aug 10 01:30 .
drwxr-xr-x 3 admin admin       19 Aug 10 01:29 ..
-rw-rw-r-- 1 admin admin 27133928 Aug 10 01:30 singularity-3.2.1-1.el7.x86_64.rpm
-rw-rw-r-- 1 admin admin 19780504 Aug 10 01:30 singularity-debuginfo-3.2.1-1.el7.x86_64.rpm
```

安装

``` shell
$ cd ~/rpmbuild/RPMS/x86_64/
$ sudo yum install *.rpm
```

安装完成后重新开一个终端，运行singularity命令测试安装是否成功。

## 参考

- https://sylabs.io/guides/3.2/user-guide/installation.html#install-on-linux

