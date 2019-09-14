# Singularity入门之资源限制

在 Singularity 中如果需要限制容器的资源，也可以通过 Linux 的 cgroup 来实现。Singularity 对资源的限制是通过一个 .toml 文件来定义的，Singularity 提供了一个样例配置文件 /etc/singularity/cgroups/cgroups.toml，后面做资源限制的时候可以参考。

如果要使用 Singularity 来限制资源，需要在启动容器的时候使用 --apply-cgroups 选项来指定定义的 .toml 文件，且必须使用 root 用户来执行，比如：

``` shell
$ sudo singularity shell --apply-cgroups cgroups.toml centos.sif
```

如果要在容器里使用普通用户可以 "--security uid:1000" 选项来运行容器。

## 限制CPU资源

在 Singularity 中可以通过三种方式来限制CPU资源
- shares
- quota/period
- cpus/mems

这里只说说比较好演示的第三种方法，先看看我限制测试机器的cpu情况，使用 lscpu 来查看，共有4个cpu，如下：

``` shell
$ lscpu -e
CPU NODE SOCKET CORE L1d:L1i:L2:L3 ONLINE MAXMHZ    MINMHZ
0   0    0      0    0:0:0:0       yes    2401.0000 1600.0000
1   0    0      1    1:1:1:0       yes    2401.0000 1600.0000
2   0    0      2    2:2:2:0       yes    2401.0000 1600.0000
3   0    0      3    3:3:3:0       yes    2401.0000 1600.0000
```

下面创建一个 cpu.toml 文件，来指定只使用第0和1个 CPU 内容如下：

``` shell
[cpu]
  cpus = "0-1"
```

然后我们使用 cpu.toml 来启动容器

``` shell
$ sudo singularity shell --security uid:1000 --apply-cgroups cpu.toml centos.simg
```

然后在容器里运行一个应用程序，比如 sleep 命令，然后在容器里查看这个 sleep 命令可以使用的CPU信息，由于容器被限制了最多只能使用两个CPU，所以其内部运行的容器也最多只能使用两个CPU。

``` shell
Singularity centos.simg:/singularity-study> sleep 12345 &
[1] 21390

Singularity centos.simg:/singularity-study> cat /proc/21390/status | grep Cpus_allowed_list
Cpus_allowed_list:      0-1
```

下面我们修改 cpu.toml 文件，来使用机器的全部CPU，修改后内容如下

``` shell
[cpu]
  cpus = "0-3"
```

再次在容器中运行应用程序并查看CPU情况

``` shell
$ sudo singularity shell --security uid:1000 --apply-cgroups cpu.toml centos.simg

Singularity centos.simg:/singularity-study> sleep 12345 &
[1] 31539

Singularity centos.simg:/singularity-study> cat /proc/31539/status | grep Cpus_allowed_list
Cpus_allowed_list:      0-3
```

## 限制内存资源

和限制CPU类似，要限制内存也可以通过一个 .toml 文件来限制，比如：

``` shell
[memory]
  limit = 524288000
  swap = 524288000
  disableOOMKiller = false
```

这里

- limit - 指定了内存的限制，单位是字节
- swap - 指定了 mem + swap 的限制，单位是字节
- disableOOMKiller - 指定当内存超限后，是否启用kill容器策略

下面启动一个容器，来看看对内存的限制

``` shell
$ sudo singularity shell --security uid:1000 --apply-cgroups mem.toml centos.simg
```

然后在宿主节点上查看一下内存限制

``` shell
$ cat /sys/fs/cgroup/memory/singularity/*/memory.limit_in_bytes
524288000
```

* 这里说一下，本来想在容器里启动一个程序来演示的，后来测试了发现，容器里运行的应用程序并不能限制住内存，不知道这是不是一个Bug。

## 其它

上面只是演示了怎样显示cpu和mem，其实 singularity 也可以限制其它资源，具体可以参考 /etc/singularity/cgroups/cgroups.toml 文件来配置。


## 参考

- https://sylabs.io/guides/3.2/user-guide/cgroups.html
