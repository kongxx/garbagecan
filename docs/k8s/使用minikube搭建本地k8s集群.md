# 使用minikube搭建本地k8s集群

## 安装minikube

我这里使用的是 rpm 包来安装

``` shell
$ wget -c https://storage.googleapis.com/minikube/releases/latest/minikube-1.3.1.rpm
$ sudo yum localinstall minikube-1.3.1.rpm
```

## 安装kubectl

可以通过 yum 或下载可执行文件来安装

``` shell
$ sudo yum install kubernetes-client

或

$ curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
$ chmod +x ./kubectl
$ sudo mv ./kubectl /usr/local/bin/kubectl
```

## 启动 k8s

可以通过下面命令来创建k8s环境

``` shell
$ minikube start --vm-driver=virtualbox
```

但是由于这一步需要在线下载一些包，因此如果不翻墙的话基本不会成功，所以通常我们得使用代理才可以使上面命令运行成功


``` shell
$ minikube start --docker-env HTTP_PROXY=http://<host>:<port> --docker-env HTTPS_PROXY=http://<host>:<port> --docker-env NO_PROXY=127.0.0.1/24
* minikube v1.3.1 on Centos 7.6.1810
* Downloading VM boot image ...
minikube-v1.3.0.iso.sha256: 65 B / 65 B [--------------------] 100.00% ? p/s 0s
minikube-v1.3.0.iso: 131.07 MiB / 131.07 MiB [-------] 100.00% 25.12 MiB p/s 5s
* Creating virtualbox VM (CPUs=2, Memory=2000MB, Disk=20000MB) ...
* Preparing Kubernetes v1.15.2 on Docker 18.09.8 ...
  - env HTTP_PROXY=http://<host>:<port>
  - env HTTPS_PROXY=http://<host>:<port>
  - env NO_PROXY=127.0.0.1/24
E0829 04:28:46.286634   24072 start.go:558] Error caching images:  Caching images for kubeadm: caching images: caching image /home/jhadmin/.minikube/cache/images/gcr.io/k8s-minikube/storage-provisioner_v1.8.1: fetching remote image: Get https://gcr.io/v2/: dial tcp 74.125.204.82:443: i/o timeout
* Unable to load cached images: loading cached images: loading image /home/jhadmin/.minikube/cache/images/gcr.io/k8s-minikube/storage-provisioner_v1.8.1: stat /home/jhadmin/.minikube/cache/images/gcr.io/k8s-minikube/storage-provisioner_v1.8.1: no such file or directory
* Downloading kubeadm v1.15.2
* Downloading kubelet v1.15.2
* Pulling images ...
* Launching Kubernetes ... 
* Waiting for: apiserver proxy etcd scheduler controller dns
* Done! kubectl is now configured to use "minikube"
```

minikube 默认使用 VirtualBox 虚拟机来跑 k8s，上面命令执行成功后，会在 VirtualBox 中创建一个名叫 minikube 的虚拟机，k8s 就跑在这个虚拟机中，我们也可以设置默认的虚拟化

``` shell
$ minikube config set vm-driver virtualbox
```

## 查看集群

### 集群状态

可以通过 kubectl cluster-info 来查看集群状态

``` shell
$ kubectl cluster-info
Kubernetes master is running at https://192.168.99.100:8443
KubeDNS is running at https://192.168.99.100:8443/api/v1/proxy/namespaces/kube-system/services/kube-dns

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

### 查看集群信息

``` shell
# 首先登录到 minikube 虚拟机中
$ minikube ssh 

# 查看镜像
$ docker images
REPOSITORY                                TAG                 IMAGE ID            CREATED             SIZE
k8s.gcr.io/kube-apiserver                 v1.15.2             34a53be6c9a7        3 weeks ago         207MB
k8s.gcr.io/kube-controller-manager        v1.15.2             9f5df470155d        3 weeks ago         159MB
k8s.gcr.io/kube-scheduler                 v1.15.2             88fa9cb27bd2        3 weeks ago         81.1MB
k8s.gcr.io/kube-proxy                     v1.15.2             167bbf6c9338        3 weeks ago         82.4MB
k8s.gcr.io/kube-addon-manager             v9.0                119701e77cbc        7 months ago        83.1MB
k8s.gcr.io/coredns                        1.3.1               eb516548c180        7 months ago        40.3MB
k8s.gcr.io/etcd                           3.3.10              2c4adeb21b4f        9 months ago        258MB
k8s.gcr.io/pause                          3.1                 da86e6ba6ca1        20 months ago       742kB
gcr.io/k8s-minikube/storage-provisioner   v1.8.1              4689081edb10        22 months ago       80.8MB

# 查看运行的容器
$ docker ps
...
```

### 启动 Dashboard

minikube 也可以通过命令行来启动 Kubernetes 的 Dashboard，运行下面的命令，即可启动 Kubernetes Dashboard，并且会在本地弹出浏览器并打开 Kubernetes 的 Dashboard。

``` shell
$ minikube dashboard
...
```

## 删除 k8s

``` shell
$ minikube delete
```
