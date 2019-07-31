# Singularity入门之安装

## 下载

``` shell
$ VERSION=2.5.2
$ wget https://github.com/singularityware/singularity/releases/download/$VERSION/singularity-$VERSION.tar.gz
```

## 编译rpm包

Singularity 提供了几种安装方式，这里选择使用编译 rpm 包的方式来安装。

``` shell
$ tar xvf singularity-$VERSION.tar.gz
$ cd singularity-$VERSION
$ ./autogen.sh
$ ./configure
$ make dist

$ rpmbuild -ta singularity-*.tar.gz

$ ls -al ~/rpmbuild/RPMS/x86_64/
-rw-rw-r-- 1 admin admin 267796 Jul 30 22:03 singularity-2.5.2-1.el7.x86_64.rpm
-rw-rw-r-- 1 admin admin 508580 Jul 30 22:03 singularity-debuginfo-2.5.2-1.el7.x86_64.rpm
-rw-rw-r-- 1 admin admin  73040 Jul 30 22:03 singularity-devel-2.5.2-1.el7.x86_64.rpm
-rw-rw-r-- 1 admin admin 187680 Jul 30 22:03 singularity-runtime-2.5.2-1.el7.x86_64.rpm
```

## 安装

``` shell
$ cd ~/rpmbuild/RPMS/x86_64/
$ sudo yum install *.rpm
```

## 测试

``` shell
# 查看一下帮助
$ singularity

# 获取一个镜像
$ singularity -d build lolcow.simg shub://GodloveD/lolcow

# 以交互模式运行容器
$ singularity shell lolcow.simg
Singularity: Invoking an interactive shell within container...

Singularity lolcow.simg:~> pwd
/home/admin
Singularity lolcow.simg:~> id
uid=1000(admin) gid=1000(admin) groups=1000(admin),10(uucp)
```