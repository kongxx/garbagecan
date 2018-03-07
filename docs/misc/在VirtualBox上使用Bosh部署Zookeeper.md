
# 在VirtualBox上使用Bosh部署Zookeeper

## 安装 VirtualBox

略

``` shell
$ VBoxManage --version
5.1.22r115126
```

## 安装Bosh

``` shell
$ wget -c https://s3.amazonaws.com/bosh-cli-artifacts/bosh-cli-2.0.48-linux-amd64
$ chmod +x bosh-cli-*
$ sudo mv bosh-cli-* /usr/local/bin/bosh
```

安装完成后运行下面命令确认安装成功

``` shell
$ bosh -v
version 2.0.48-e94aeeb-2018-01-09T23:08:07Z

Succeeded
```

``` shell
$ bosh environments
URL  Alias

0 environments

Succeeded
```

## 安装依赖库

``` shell
$ sudo yum install gcc gcc-c++ ruby ruby-devel mysql-devel postgresql-devel postgresql-libs sqlite-devel libxslt-devel libxml2-devel patch openssl
$ gem install yajl-ruby
```

安装后检查安装是否成功
``` shell
$ ruby -v
ruby 2.0.0p648 (2015-12-16) [x86_64-linux]
```

## 创建Director

``` shell
$ mkdir vbox
$ cd vbox
$ git clone https://github.com/cloudfoundry/bosh-deployment
```

使用 “bosh create-env” 创建Director

``` shell
$ bosh create-env bosh-deployment/bosh.yml \
  --state state.json \
  --vars-store ./creds.yml \
  -o bosh-deployment/virtualbox/cpi.yml \
  -o bosh-deployment/virtualbox/outbound-network.yml \
  -o bosh-deployment/bosh-lite.yml \
  -o bosh-deployment/jumpbox-user.yml \
  -v director_name=vbox \
  -v internal_ip=192.168.50.6 \
  -v internal_gw=192.168.50.1 \
  -v internal_cidr=192.168.50.0/24 \
  -v network_name=vboxnet0 \
  -v outbound_network_name=NatNetwork

...
```

如果要删除上面创建的Director，可以运行下面的命令

``` shell
$ bosh delete-env bosh-deployment/bosh.yml \
  --state state.json \
  --vars-store ./creds.yml \
  -o bosh-deployment/virtualbox/cpi.yml \
  -o bosh-deployment/virtualbox/outbound-network.yml \
  -o bosh-deployment/bosh-lite.yml \
  -o bosh-deployment/jumpbox-user.yml \
  -v director_name=vbox \
  -v internal_ip=192.168.50.6 \
  -v internal_gw=192.168.50.1 \
  -v internal_cidr=192.168.50.0/24 \
  -v network_name=vboxnet0 \
  -v outbound_network_name=NatNetwork

...
```

给上面创建的Director设置别名

``` shell
$ bosh alias-env vbox -e 192.168.50.6 --ca-cert <(bosh int ./creds.yml --path /director_ssl/ca)
Using environment '192.168.50.6' as anonymous user

Name      vbox  
UUID      91478351-44a7-4bbb-b7b3-fd55ed1c19cd  
Version   264.7.0 (00000000)  
CPI       warden_cpi  
Features  compiled_package_cache: disabled  
          config_server: disabled  
          dns: disabled  
          snapshots: disabled  
User      (not logged in)  

Succeeded
```

设置用户并登录

``` shell
$ export BOSH_CLIENT=admin
$ export BOSH_CLIENT_SECRET=`bosh int ./creds.yml --path /admin_password`
$ bosh -e vbox env
Using environment '192.168.50.6' as client 'admin'

Name      vbox  
UUID      91478351-44a7-4bbb-b7b3-fd55ed1c19cd  
Version   264.7.0 (00000000)  
CPI       warden_cpi  
Features  compiled_package_cache: disabled  
          config_server: disabled  
          dns: disabled  
          snapshots: disabled  
User      admin  

Succeeded
```

为了后面我们可以使用ssh登录虚拟机，我们还需要使用下面的命令建立路由

``` shell
$ sudo ip route add   10.244.0.0/16 via 192.168.50.6
或
$ sudo route add -net 10.244.0.0/16 gw  192.168.50.6
```

## 部署Zookeeper

### 更新cloud-config

``` shell
$ bosh -e vbox update-cloud-config bosh-deployment/warden/cloud-config.yml
Using environment '192.168.50.6' as client 'admin'

+ azs:
+ - name: z1
+ - name: z2
+ - name: z3
  
+ vm_types:
+ - name: default
  
+ compilation:
+   az: z1
+   network: default
+   reuse_compilation_vms: true
+   vm_type: default
+   workers: 5
  
+ networks:
+ - name: default
+   subnets:
+   - azs:
+     - z1
+     - z2
+     - z3
+     dns:
+     - 8.8.8.8
+     gateway: 10.244.0.1
+     range: 10.244.0.0/24
+     reserved: []
+     static:
+     - 10.244.0.34
+   type: manual
  
+ disk_types:
+ - disk_size: 1024
+   name: default

Continue? [yN]: y

Succeeded
```

### 更新虚拟机模板

首先使用 “bosh -e vbox stemcells” 查看一下，如下，可以看到目前没有任何虚拟机模板。

``` shell
$ bosh -e vbox stemcells
Using environment '192.168.50.6' as client 'admin'

Name  Version  OS  CPI  CID  

(*) Currently deployed

0 stemcells

Succeeded
```

更新虚拟机模板

``` shell
$ bosh -e vbox upload-stemcell https://bosh.io/d/stemcells/bosh-warden-boshlite-ubuntu-trusty-go_agent?v=3468.17 --sha1 1dad6d85d6e132810439daba7ca05694cec208ab

或者

$ wget -c https://bosh.io/d/stemcells/bosh-warden-boshlite-ubuntu-trusty-go_agent?v=3468.17
$ bosh -e vbox us bosh-warden-boshlite-ubuntu-trusty-go_agent\?v\=3468.17
```

上面的命令会比较慢，更新再次查询虚拟机模板，可以看到已经下载了新的模板。

``` shell
$ bosh -e vbox stemcells
Using environment '192.168.50.6' as client 'admin'

Name                                         Version  OS             CPI  CID  
bosh-warden-boshlite-ubuntu-trusty-go_agent  3468.17  ubuntu-trusty  -    b1e5983d-ab18-4e36-6877-1ebe5e8582d6  

(*) Currently deployed

1 stemcells

Succeeded
```

### 部署Zookeeper

``` shell
$ wget -c https://raw.githubusercontent.com/cppforlife/zookeeper-release/master/manifests/zookeeper.yml
$ bosh -e vbox -d zookeeper deploy zookeeper.yml
...
```

运行上面部署命令后可以使用下面命令来查看服务状态

``` shell
$ bosh -e vbox -d zookeeper instances
Using environment '192.168.50.6' as client 'admin'

Task 23. Done

Deployment 'zookeeper'

Instance                                          Process State  AZ  IPs  
smoke-tests/bb0b0f87-5a59-489f-b3ca-1bdfd5a8167f  -              z1  10.244.0.7  
zookeeper/40dc99e8-17f0-4c8d-bda1-670cf24490d5    running        z3  10.244.0.6  
zookeeper/48cce3c4-b4c2-46bc-947d-da1b3f255561    running        z1  10.244.0.2  
zookeeper/8926bddb-2986-44a6-b8e6-337fa2ac2c4f    running        z2  10.244.0.4  
zookeeper/970b4886-ca4e-4f91-9a27-6d93e151ae0c    running        z1  10.244.0.3  
zookeeper/eea5d292-1473-4849-956c-25e196de8e0a    running        z2  10.244.0.5  

6 instances

Succeeded
```

查看虚拟机信息

``` shell
$ bosh -e vbox vms
Using environment '192.168.50.6' as client 'admin'

Task 24. Done

Deployment 'zookeeper'

Instance                                        Process State  AZ  IPs         VM CID                                VM Type  Active  
zookeeper/40dc99e8-17f0-4c8d-bda1-670cf24490d5  running        z3  10.244.0.6  dfe4e8f6-376c-4046-4929-58c9ae16a1c0  default  false  
zookeeper/48cce3c4-b4c2-46bc-947d-da1b3f255561  running        z1  10.244.0.2  3ab39e75-9c65-415f-67b1-fb66982750b4  default  false  
zookeeper/8926bddb-2986-44a6-b8e6-337fa2ac2c4f  running        z2  10.244.0.4  46f42f6f-2741-4c02-5330-fdf2e713e2ae  default  false  
zookeeper/970b4886-ca4e-4f91-9a27-6d93e151ae0c  running        z1  10.244.0.3  c211408e-50c5-4daa-4aad-192d037489f2  default  false  
zookeeper/eea5d292-1473-4849-956c-25e196de8e0a  running        z2  10.244.0.5  5339825b-ee47-42dd-402e-4b9da39e1b26  default  false  

5 vms

Succeeded
```

如果要删除 Zookeeper Development，执行下面命令

``` shell
$ bosh -e vbox -d zookeeper delete-deployment
```

### SSH访问虚拟机

如果我们想使用ssh登录虚拟机，可以运行下面命令

``` shell
$ bosh -e vbox -d zookeeper ssh <vm>

比如：

$ bosh -e vbox -d zookeeper ssh zookeeper/40dc99e8-17f0-4c8d-bda1-670cf24490d5
```

### 测试Zookeeper

``` shell
$ bosh -e vbox -d zookeeper run-errand smoke-tests
```

# Cloud Foundry 运行bosh create-env时报错： TLS handshake timeout

``` shell
bosh create-env bosh-deployment/bosh.yml \
  --state state.json \
  --vars-store ./creds.yml \
  -o bosh-deployment/virtualbox/cpi.yml \
  -o bosh-deployment/virtualbox/outbound-network.yml \
  -o bosh-deployment/bosh-lite.yml \
  -o bosh-deployment/jumpbox-user.yml \
  -v director_name=vbox \
  -v internal_ip=192.168.50.6 \
  -v internal_gw=192.168.50.1 \
  -v internal_cidr=192.168.50.0/24 \
  -v network_name=vboxnet0 \
  -v outbound_network_name=NatNetwork

...
Deploying:
  Creating instance 'bosh/0':
    Waiting until instance is ready:
      Post https://mbus:<redacted>@192.168.50.6:6868/agent: net/http: TLS handshake timeout
```


``` shell
$ bosh -e vbox env
Fetching info:
  Performing request GET 'https://vbox:25555/info':
    Performing GET request:
      Retry: Get https://vbox:25555/info: dial tcp 192.168.50.6:25555: getsockopt: connection refused
```

编辑 bosh-deployment/virtualbox/cpi.yml 文件，将其中 memory 由 4096 改成 2048。然后重新运行bosh create-env 命令。
