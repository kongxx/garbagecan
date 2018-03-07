# 在VirtualBox上使用Bosh部署Cloud Foundry

## 安装Bosh

### 安装 VirtualBox

略

``` shell
$ VBoxManage --version
5.1.22r115126
```

### 安装Bosh

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

### 安装依赖库

``` shell
$ sudo yum install gcc gcc-c++ ruby ruby-devel mysql-devel postgresql-devel postgresql-libs sqlite-devel libxslt-devel libxml2-devel patch openssl
$ gem install yajl-ruby
```

安装后检查安装是否成功

``` shell
$ ruby -v
ruby 2.0.0p648 (2015-12-16) [x86_64-linux]
```

## 部署Bosh Director

### 获取Bosh部署文件

``` shell
$ mkdir vbox
$ cd vbox
$ git clone https://github.com/cloudfoundry/bosh-deployment
```

### 部署Director

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

### 设置Director别名

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

### 设置Director用户并登录

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

### 路由配置

为了后面我们可以使用ssh登录虚拟机，我们还需要使用下面的命令建立路由

``` shell
$ sudo ip route add   10.244.0.0/16 via 192.168.50.6
或
$ sudo route add -net 10.244.0.0/16 gw  192.168.50.6
```

## 部署Cloud Foundry

### 获取Cloud Foundry部署文件

``` shell
$ git clone https://github.com/cloudfoundry/cf-deployment 
```

### 解压 stemcell 并上传

``` python
$ export STEMCELL_VERSION=$(bosh int cf-deployment/cf-deployment.yml --path /stemcells/alias=default/version)
$ wget -c https://bosh.io/d/stemcells/bosh-warden-boshlite-ubuntu-trusty-go_agent?v=$STEMCELL_VERSION
$ bosh -e vbox us bosh-warden-boshlite-ubuntu-trusty-go_agent\?v\=$STEMCELL_VERSION
Using environment '192.168.50.6' as client 'admin'

######################################################### 100.00% 23.42 MB/s 15s
Task 1

Task 1 | 07:45:48 | Update stemcell: Extracting stemcell archive (00:00:06)
Task 1 | 07:45:54 | Update stemcell: Verifying stemcell manifest (00:00:00)
Task 1 | 07:45:54 | Update stemcell: Checking if this stemcell already exists (00:00:00)
Task 1 | 07:45:54 | Update stemcell: Uploading stemcell bosh-warden-boshlite-ubuntu-trusty-go_agent/3541.5 to the cloud (00:00:23)
Task 1 | 07:46:17 | Update stemcell: Save stemcell bosh-warden-boshlite-ubuntu-trusty-go_agent/3541.5 (0f580571-e5b2-4d47-6a8c-111a1a838b38) (00:00:00)

Task 1 Started  Wed Mar  7 07:45:48 UTC 2018
Task 1 Finished Wed Mar  7 07:46:17 UTC 2018
Task 1 Duration 00:00:29
Task 1 done

Succeeded
```

上传后可以查看一下

``` shell
$ bosh -e vbox ss
Using environment '192.168.50.6' as client 'admin'

Name                                         Version   OS             CPI  CID  
bosh-warden-boshlite-ubuntu-trusty-go_agent  3541.5    ubuntu-trusty  -    85c765db-926e-4831-4c4c-76de36fcbacf  
~                                            3468.17*  ubuntu-trusty  -    65c18f05-caf1-434e-6f8e-8b735407d17c  

(*) Currently deployed

2 stemcells

Succeeded
```

### 更新cloud-config

``` shell
$ bosh -e vbox update-cloud-config cf-deployment/iaas-support/bosh-lite/cloud-config.yml
```

### 部署cloud foundry

``` shell
$ bosh -e vbox -d cf deploy cf-deployment/cf-deployment.yml -o cf-deployment/operations/bosh-lite.yml --vars-store deployment-vars.yml -v system_domain=bosh-lite.com
```

这一步比较漫长，要等好久好久。

有人说可以使用预编译的版本，命令如下，但是我试了一下中间会出错，就没有继续调研。

``` shell
$ bosh -e vbox -d cf deploy cf-deployment/cf-deployment.yml -o cf-deployment/operations/use-compiled-releases.yml --vars-store deployment-vars.yml -v system_domain=bosh-lite.com
```

### 安装cf命令行

``` shell
$ curl -L "https://packages.cloudfoundry.org/stable?release=linux64-binary&source=github" | tar -zx
$ chmod +x bosh-cli-*
$ mv cf /usr/local/bin
```

> 参考 
>   https://github.com/cloudfoundry/cli#installers-and-compressed-binaries

### 使用cf命令登录

``` shell
$ cf login -a https://api.bosh-lite.com --skip-ssl-validation -u admin -p $(bosh interpolate ./deployment-vars.yml --path /cf_admin_password)
```

### 创建org和space

``` shell
$ cf create-org cloudfoundry
#$  cf target -o cloudfoundry
$ cf create-space development
$ cf target -o cloudfoundry -s development
```

## 部署App

``` shell
$ git clone https://github.com/vchrisb/cf-helloworld 
$ cd cf-helloworld
$ cf push
```

部署完成后，可以访问 “http://cf-helloworld.bosh-lite.com” 来查看我们部署的Python app，比如：

``` shell
$ curl http://cf-helloworld.bosh-lite.com
```
