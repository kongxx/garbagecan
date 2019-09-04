# Singularity入门之运行容器

## 下载已存在镜像

和Docker类似，要运行 Singularity 也需要先获取镜像，Singularity 可以从Singularity Hub 或者 Docker Hub 来获取预编译的镜像，或者编译新镜像。

``` shell
# 查询镜像
$ singularity search ubuntu

# 获取预编译镜像 （会在当前目录下下载 ubuntu_<tag>.sif 文件）
$ singularity pull ubuntu
$ singularity pull docker://ubuntu

# 从 Singularity Hub build 镜像
$ singularity -d build lolcow.simg shub://GodloveD/lolcow

# 从 Docker Hub build 镜像
$ singularity -d build lolcow.simg docker://godlovedc/lolcow
$ singularity -d build centos.simg docker://centos
$ singularity -d build ubuntu.simg docker://ubuntu
```

## 交互模式运行

``` shell
$ singularity shell ubuntu_latest.sif
或
$ singularity shell ubuntu.simg
Singularity: Invoking an interactive shell within container...

Singularity ubuntu.simg:~> pwd
/home/admin
Singularity ubuntu.simg:~> id
uid=1000(admin) gid=1000(admin) groups=1000(admin),10(wheel)

```

## 执行一个命令并退出

``` shell
$ singularity exec ubuntu_latest.sif bash -c "pwd && id"
或
$ singularity exec ubuntu.simg bash -c "pwd && id"
/home/admin
uid=1000(admin) gid=1000(admin) groups=1000(admin),10(wheel)
```

## 运行一个容器

``` shell
$ singularity run ubuntu_latest.sif
或
$ singularity run ubuntu.simg
admin@bdmaster:~$ pwd
/home/admin
admin@bdmaster:~$ id
uid=1000(admin) gid=1000(admin) groups=1000(admin),10(wheel)
```

## 后台运行容器实例

### 启动实例

``` shell
$ singularity instance start ubuntu.simg test1
$ singularity instance start ubuntu.simg test2
```

### 查看实例

``` shell
$ singularity instance list
DAEMON NAME      PID      CONTAINER IMAGE
test1            14172    /home/admin/ubuntu.simg
test2            14239    /home/admin/ubuntu.simg
```

### 操作实例

可以通过 shell, exec, run 命令来连到容器中运行命令

使用 shell 命令连入容器

``` shell
$ singularity shell instance://test1
Singularity ubuntu.simg:~> ps -ef
UID        PID  PPID  C STIME TTY          TIME CMD
admin      1     0  0 03:14 ?        00:00:00 singularity-instance: admin [test1]
admin      3     0  3 03:14 pts/0    00:00:00 /bin/bash --norc
admin      4     3  0 03:14 pts/0    00:00:00 ps -ef
```

使用 exec 执行命令

``` shell
$ singularity exec instance://test1 ps -ef
UID        PID  PPID  C STIME TTY          TIME CMD
admin      1     0  0 03:14 ?        00:00:00 singularity-instance: admin [test1]
admin      6     0  0 03:15 pts/0    00:00:00 ps -ef
```

### 停止实例

``` shell
$ singularity instance stop test1
$ singularity instance stop test1
```

## 绑定目录

在 Singularity 中也可以在 shell, run, instance.start 等命令中通过 "-B" 选项来实现 Docker 中 “-v” 选项提供挂载卷的功能，比如：

``` shell
$ singularity shell -B /apps:/apps ubuntu.simg
```
