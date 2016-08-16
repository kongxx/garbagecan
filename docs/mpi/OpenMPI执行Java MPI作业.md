# OpenMPI执行Java MPI作业

接上一篇博客（[设置Open MPI集群环境](http://blog.csdn.net/kongxx/article/details/？？？)）

## 安装

### 安装java环境

``` shell
sudo yum install -y java-1.8.0-openjdk.x86_64 java-1.8.0-openjdk-devel.x86_64
```

### 编译安装openmpi

``` shell
$ ./configure --prefix=/opt/openmpi --enable-mpi-java
$ make
$ sudo make install
```
** 注意：要使用“--enable-mpi-java”选项 **

### 安装验证

安装完成后，检查/opt/openmpi/bin/mpijavac文件是否存在，如果文件存在则说明安装成功。


## 执行Java MPI作业

###  Hello.java文件

``` java
import java.net.InetAddress;
import mpi.*;

class Hello {
    static public void main(String[] args) throws Exception {

	MPI.Init(args);
	String hostname = InetAddress.getLocalHost().getHostName();
	int myrank = MPI.COMM_WORLD.getRank();
	int size = MPI.COMM_WORLD.getSize() ;
	System.out.println("<" + hostname + ">: " + "Hello world from rank " + myrank + " of " + size);

	MPI.Finalize();
    }
}
```

### hosts文件

``` shell
test1    slots=2
test2    slots=2
```

### 编译java代码

``` shell
$ mpijavac Hello.java
```

### 运行Java类

``` shell
$ mpiexec --hostfile hosts -np 4 java Hello
<test1>: Hello world from rank 0 of 4
<test1>: Hello world from rank 1 of 4
<test2>: Hello world from rank 2 of 4
<test2>: Hello world from rank 3 of 4
```
