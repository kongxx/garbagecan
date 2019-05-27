# Nvidia Docker使用小节

## 安装Nvidia驱动

### 设置系统启动级别

```shell
$ sudo systemctl get-default graphical.target 

$ sudo systemctl set-default multi-user.target 
Removed symlink /etc/systemd/system/default.target. 
Created symlink from /etc/systemd/system/default.target to /usr/lib/systemd/system/multi-user.target. 

$ sudo systemctl get-default multi-user.target
```

### 运行安装程序

运行下面的程序，按提示输入就可以。

```shell
# 如果可以，安装前可以先把系统升级到最新版
# $ sudo yum update

$ sudo ./NVIDIA-Linux-x86_64-418.56.run
```

### 使用 nvidia-smi 检查安装

```shell
$ nvidia-smi
Wed May  8 09:57:55 2019       
+-----------------------------------------------------------------------------+
| NVIDIA-SMI 418.56       Driver Version: 418.56       CUDA Version: 10.1     |
|-------------------------------+----------------------+----------------------+
| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |
| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |
|===============================+======================+======================|
|   0  Quadro M4000        Off  | 00000000:02:00.0 Off |                  N/A |
| 44%   33C    P0    44W / 120W |      0MiB /  8126MiB |      2%      Default |
+-------------------------------+----------------------+----------------------+
                                                                               
+-----------------------------------------------------------------------------+
| Processes:                                                       GPU Memory |
|  GPU       PID   Type   Process name                             Usage      |
|=============================================================================|
|  No running processes found                                                 |
+-----------------------------------------------------------------------------+
```

## 安装Cuda ToolKit

安装系统库、头文件和依赖库 （后面使用rpm包安装cuda toolkit，不确定这一步是不是必须）。

```shell
$ sudo yum install kernel-devel-$(uname -r) kernel-headers-$(uname -r)
$ sudo yum groupinstall "Development Tools"
```

从 https://developer.nvidia.com/cuda-downloads 地址下载 cuda toolkit 安装包，这里选用 rpm 包，然后安装。
```shell
$ sudo yum install cuda-repo-rhel7-10-1-local-10.1.168-418.67-1.0-1.x86_64.rpm
```

## Cuda 例子程序

### 安装 

运行下面命令来安装例子程序，命令运行完后，会在家目录下产生 NVIDIA_CUDA-10.1_Samples 目录，

```shell
$ cuda-install-samples-10.1.sh ~
```

安装完成后，修改PATH和LD_LIBRARY_PATH，如下：
```shell
export PATH=/usr/local/cuda-10.1/bin:${PATH}
export LD_LIBRARY_PATH=/usr/local/cuda-10.1/lib64:${LD_LIBRARY_PATH}
```

### 编译

```shell
$ cd ~/NVIDIA_CUDA-10.1_Samples
$ make
```

### 运行

```shell
$ cd ~/NVIDIA_CUDA-10.1_Samples

$ bin/x86_64/linux/release/UnifiedMemoryPerf

$ bin/x86_64/linux/release/nbody
```

## Nvidia Docker

### 安装 Docker 服务

``` shell
$ sudo yum install docker
```

### 安装 nvidia-container-runtime-hook

``` shell
$ distribution=$(. /etc/os-release;echo $ID$VERSION_ID)

# Add the package repositories
$ cd /etc/yum.repos.d
$ sudo wget -c https://nvidia.github.io/nvidia-container-runtime/$distribution/nvidia-container-runtime.repo

$ sudo yum install -y nvidia-container-runtime-hook
```

安装完成后，需要重新启动 docker 服务

``` shell
$ sudo systemctl start docker
```

# 测试 nvidia docker

``` shell
# 获取测试用的镜像
$ sudo docker pull nvidia/cuda

# 检查容器中是否可以看到GPU
$ sudo docker run --rm nvidia/cuda nvidia-smi

# 运行测试程序
$ sudo docker run --rm -v ~/NVIDIA_CUDA-10.1_Samples:/NVIDIA_CUDA-10.1_Samples  nvidia/cuda /NVIDIA_CUDA-10.1_Samples/bin/x86_64/linux/release/UnifiedMemoryPerf

# 运行图形应用程序
$ sudo docker run --rm -v ~/NVIDIA_CUDA-10.1_Samples:/NVIDIA_CUDA-10.1_Samples -v /usr/lib64:/usr/lib64 -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY nvidia/cuda /NVIDIA_CUDA-10.1_Samples/bin/x86_64/linux/release/nbody
```

## 参考

- 驱动下载地址： https://www.nvidia.cn/Download/index.aspx?lang=cn
- Cuda Toolkit下载地址: https://developer.nvidia.com/cuda-downloads
- Cuda 其它平台 Dockerfile： https://gitlab.com/nvidia/cuda/tree/centos7/10.1
- 参考： https://docs.nvidia.com/cuda/cuda-quick-start-guide/index.html#redhat-x86_64-run
- 参考： https://docs.nvidia.com/cuda/cuda-installation-guide-linux/index.html

