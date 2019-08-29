# 使用Kind搭建本地k8s集群

## 介绍

Kind 是 Kubernetes In Docker 的缩写，是使用 Docker 容器部署 Kubernetes 的工具。也是官方推荐的搭建本地集群的工具。

## 安装 Kind

``` shell
$ curl -Lo ./kind https://github.com/kubernetes-sigs/kind/releases/download/v0.5.1/kind-$(uname)-amd64
$ chmod +x ./kind
$ sudo mv ./kind /usr/bin/
```

## 安装 Docker

步骤略

## 安装 kubectl

``` shell
$ sudo yum install kubernetes-client

或

$ curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
$ chmod +x ./kubectl
$ sudo mv ./kubectl /usr/bin/kubectl
```

## 单节点集群

### 创建集群

``` shell
$ sudo kind create cluster
Creating cluster "kind" ...
 ? Ensuring node image (kindest/node:v1.15.3)
 ? Preparing nodes
 ? Creating kubeadm config
 ? Starting control-plane
 ? Installing CNI
 ? Installing StorageClass
Cluster creation complete. You can now use the cluster with:

$ export KUBECONFIG="$(sudo kind get kubeconfig-path --name="kind")"

$ sudo kubectl cluster-info
Kubernetes master is running at https://127.0.0.1:34059
KubeDNS is running at https://127.0.0.1:34059/api/v1/proxy/namespaces/kube-system/services/kube-dns

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

也可以使用下面命令来在创建集群的时候制定集群名称。

``` shell
$ sudo kind create cluster --name moelove
```

### 查看集群

查看集群信息

``` shell
$ export KUBECONFIG="$(kind get kubeconfig-path --name="kind")"
$ sudo kubectl cluster-info
Kubernetes master is running at https://127.0.0.1:34059
KubeDNS is running at https://127.0.0.1:34059/api/v1/proxy/namespaces/kube-system/services/kube-dns

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

查看集群节点信息

``` shell
$ sudo kubectl get nodes
NAME                 STATUS    AGE
kind-control-plane   Ready     1h
```

查看系统上的集群

``` shell
$ sudo kind get clusters
kind
```

### 删除集群

``` shell
$ sudo kind delete cluster
Deleting cluster "kind" ...
$KUBECONFIG is still set to use /root/.kube/kind-config-kind even though that file has been deleted, remember to unset it
```

## 多节点集群

对于多节点集群的创建，需要通过使用配置文件的方式来创建。

### 配置文件

先创建一个 yaml 格式文件，文件名就叫 kind-config.yaml，内容如下：

``` yaml
kind: Cluster
apiVersion: kind.sigs.k8s.io/v1alpha3
kubeadmConfigPatches:
- |
  apiVersion: kubeadm.k8s.io/v1beta2
  kind: ClusterConfiguration
  metadata:
    name: config
  networking:
    serviceSubnet: 10.0.0.0/16
- |
  apiVersion: kubeadm.k8s.io/v1beta2
  kind: InitConfiguration
  metadata:
    name: config
  networking:
    serviceSubnet: 10.0.0.0/16
nodes:
- role: control-plane
- role: control-plane
- role: control-plane
- role: worker
- role: worker
- role: worker
```

### 创建集群

通过配置文件创建集群

``` shell
$ sudo kind create cluster --name mycluster --config kind-config.yaml
Creating cluster "mycluster" ...
 ✓ Ensuring node image (kindest/node:v1.15.3)
 ✓ Preparing nodes
 ✓ Configuring the external load balancer
 ✓ Creating kubeadm config
 ✓ Starting control-plane
 ✓ Installing CNI
 ✓ Installing StorageClass
 ✓ Joining more control-plane nodes
 ✓ Joining worker nodes
Cluster creation complete. You can now use the cluster with:

export KUBECONFIG="$(kind get kubeconfig-path --name="mycluster")"
kubectl cluster-info
```

### 查看集群

查看集群信息

``` shell
$ export KUBECONFIG="$(sudo kind get kubeconfig-path --name="mycluster")"
$ sudo kubectl cluster-info
Kubernetes master is running at https://127.0.0.1:33483
KubeDNS is running at https://127.0.0.1:33483/api/v1/proxy/namespaces/kube-system/services/kube-dns

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

查看集群节点

``` shell
$ sudo kubectl get nodes
NAME                       STATUS    AGE
mycluster-control-plane    Ready     6m
mycluster-control-plane2   Ready     5m
mycluster-control-plane3   Ready     4m
mycluster-worker           Ready     4m
mycluster-worker2          Ready     4m
mycluster-worker3          Ready     4m
​``` shell

查询集群容器

​``` shell
$ sudo docker ps
CONTAINER ID        IMAGE                                                                                          COMMAND                  CREATED             STATUS              PORTS                                  NAMES
93bb0d8df106        kindest/node:v1.15.3@sha256:27e388752544890482a86b90d8ac50fcfa63a2e8656a96ec5337b902ec8e5157   "/usr/local/bin/en..."   9 minutes ago       Up 9 minutes        42008/tcp, 127.0.0.1:42008->6443/tcp   mycluster-control-plane
2a1b67d11463        kindest/node:v1.15.3@sha256:27e388752544890482a86b90d8ac50fcfa63a2e8656a96ec5337b902ec8e5157   "/usr/local/bin/en..."   9 minutes ago       Up 9 minutes                                               mycluster-worker3
d7a8f74a9e95        kindest/node:v1.15.3@sha256:27e388752544890482a86b90d8ac50fcfa63a2e8656a96ec5337b902ec8e5157   "/usr/local/bin/en..."   9 minutes ago       Up 9 minutes                                               mycluster-worker
8d2aaa019a21        kindest/haproxy:2.0.0-alpine                                                                   "/docker-entrypoin..."   9 minutes ago       Up 9 minutes        33483/tcp, 127.0.0.1:33483->6443/tcp   mycluster-external-load-balancer
dd2fca2b2401        kindest/node:v1.15.3@sha256:27e388752544890482a86b90d8ac50fcfa63a2e8656a96ec5337b902ec8e5157   "/usr/local/bin/en..."   9 minutes ago       Up 9 minutes                                               mycluster-worker2
f47d3c836f85        kindest/node:v1.15.3@sha256:27e388752544890482a86b90d8ac50fcfa63a2e8656a96ec5337b902ec8e5157   "/usr/local/bin/en..."   9 minutes ago       Up 9 minutes        39892/tcp, 127.0.0.1:39892->6443/tcp   mycluster-control-plane3
5feeee745b06        kindest/node:v1.15.3@sha256:27e388752544890482a86b90d8ac50fcfa63a2e8656a96ec5337b902ec8e5157   "/usr/local/bin/en..."   9 minutes ago       Up 9 minutes        36154/tcp, 127.0.0.1:36154->6443/tcp   mycluster-control-plane2
4213abeebc6f        kindest/haproxy:2.0.0-alpine         
```
