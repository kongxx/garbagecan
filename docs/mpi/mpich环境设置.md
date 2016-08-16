# mpich环境设置

## 下载与安装

``` shell
wget -c http://www.mpich.org/static/downloads/3.2/mpich-3.2.tar.gz
tar zxvf mpich-3.2.tar.gz
./configure --prefix=/opt/mpich
make
sudo make install
```

## 设置环境变量

``` shell
$ export PATH=/opt/mpich/bin:$PATH
$ export LD_LIBRARY_PATH=/opt/mpich/lib/:$LD_LIBRARY_PATH
```

并且修改 ~/.bashrc 文件

``` shell
PATH=/opt/mpich/bin:$PATH
LD_LIBRARY_PATH=/opt/mpich/lib/:$LD_LIBRARY_PATH
export PATH LD_LIBRARY_PATH
```

## 运行MPI作业

在mpich源代码目录下的examples目录下，运行

``` shell
$ mpiexec -f hosts -n 4 cpi
Process 0 of 4 is on test1
Process 1 of 4 is on test1
Process 2 of 4 is on test2
Process 3 of 4 is on test2
pi is approximately 3.1415926544231239, Error is 0.0000000008333307
wall clock time = 0.014642
```

或者运行

``` shell
$ mpicc -o hello hellow.c
$ mpirun -np 3 ./hello

$ mpiexec -hosts test1,test2 -np 4 ./hello
$ mpiexec -hosts test1:2,test2:2 -np 4 ./hello

$ mpiexec -f <hosts file> -n 4 ./hello
```

其中 hosts file 格式如下：

``` shell
test1:2
test2:2
```
