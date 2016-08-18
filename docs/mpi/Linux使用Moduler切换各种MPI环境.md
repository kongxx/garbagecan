# Linux使用Moduler切换各种MPI环境

## 安装不同版本的mpi库

```shell
sudo yum install -y openmpi-1.10.x86_64 openmpi-1.10-devel.x86_64
sudo yum install -y openmpi-1.8.x86_64 openmpi-1.8-devel.x86_64
sudo yum install -y mpich2.x86_64 mpich2-devel.x86_64
```

## 安装modules

```shell
sudo yum install -y environment-modules
```

## 使用module管理库

### 使用 module avail 查看当前可用的库

```shell
$ module avail

--------------------------------- /usr/share/Modules/modulefiles ----------------------------------
dot         module-git  module-info modules     null        use.own

---------------------------------------- /etc/modulefiles -----------------------------------------
mpich-x86_64        openmpi-1.10-x86_64 openmpi-1.8-x86_64  openmpi-x86_64
```

### 使用 module list 查看当前已经加载的库

```shell
$ module list

# 如果已经使用 module load 加载了库，查看结果如下：
$ module load openmpi-1.10-x86_64
$ module list
Currently Loaded Modulefiles:
  1) openmpi-1.10-x86_64

```

### 使用 module load/unload <module> 加载和卸载库

```shell
$ module load openmpi-1.10-x86_64

$ module list
Currently Loaded Modulefiles:
  1) openmpi-1.10-x86_64

$ module unload openmpi-1.10-x86_64
```

### 添加自定义模块

从 "module avail" 命令结果可以看到在 /etc/modulefiles 目录下保存着各个mpi的配置信息，下面是openmpi-1.10-x86_64的配置内容。

```shell
#%Module 1.0
#
#  OpenMPI module for use with 'environment-modules' package:
#
conflict		mpi
prepend-path 		PATH 		/usr/lib64/openmpi-1.10/bin
prepend-path 		LD_LIBRARY_PATH /usr/lib64/openmpi-1.10/lib
prepend-path		PYTHONPATH	/usr/lib64/python2.6/site-packages/openmpi-1.10
prepend-path		MANPATH		/usr/share/man/openmpi-1.10-x86_64
setenv 			MPI_BIN		/usr/lib64/openmpi-1.10/bin
setenv			MPI_SYSCONFIG	/etc/openmpi-1.10-x86_64
setenv			MPI_FORTRAN_MOD_DIR	/usr/lib64/gfortran/modules/openmpi-1.10-x86_64
setenv			MPI_INCLUDE	/usr/include/openmpi-1.10-x86_64
setenv	 		MPI_LIB		/usr/lib64/openmpi-1.10/lib
setenv			MPI_MAN		/usr/share/man/openmpi-1.10-x86_64
setenv			MPI_PYTHON_SITEARCH	/usr/lib64/python2.6/site-packages/openmpi-1.10
setenv			MPI_COMPILER	openmpi-x86_64
setenv			MPI_SUFFIX	_openmpi
setenv	 		MPI_HOME	/usr/lib64/openmpi-1.10
```

因此，对于自己手动安装的mpi版本，可以根据上面的配置来写一个新的版本，比如我新加一个/etc/modulefiles/myopenmpi文件，将其中的路径指定我自己安装的openmpi路径/opt/openmpi上，文件看起来如下：

```shell
#%Module 1.0
#
#  OpenMPI module for use with 'environment-modules' package:
#
conflict                mpi
prepend-path            PATH            /opt/openmpi/bin
prepend-path            LD_LIBRARY_PATH /opt/openmpi/lib
prepend-path            PYTHONPATH      /usr/lib64/python2.6/site-packages/openmpi-1.10
prepend-path            MANPATH         /opt/openmpi/share/man
setenv                  MPI_BIN         /opt/openmpi/bin
setenv                  MPI_SYSCONFIG   /etc/myopenmpi-1.10-x86_64
setenv                  MPI_FORTRAN_MOD_DIR     /usr/lib64/gfortran/modules/openmpi-1.10-x86_64
setenv                  MPI_INCLUDE     /opt/openmpi/include
setenv                  MPI_LIB         /opt/openmpi/lib
setenv                  MPI_MAN         /opt/openmpi/share/man
setenv                  MPI_PYTHON_SITEARCH     /usr/lib64/python2.6/site-packages/openmpi-1.10
setenv                  MPI_COMPILER    openmpi-x86_64
setenv                  MPI_SUFFIX      _openmpi
setenv                  MPI_HOME        /opt/openmpi
```

然后就可以在 “module avail” 中看到这个新添加的版本了。
