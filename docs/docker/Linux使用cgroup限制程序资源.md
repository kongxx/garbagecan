# Linux使用cgroup限制程序资源

以前一直在使用Docker来封装并限制容器资源，从而实现限制进程资源的目的。但Linux Docker底层是基于cgroup来实现的，于是乎今天就想起来试试直接使用cgroup来限制进程资源。

下面就以要限制一个程序的内存为例，来看看怎么实现限制资源。对于其它的资源限制都可以使用类似方法。

为了测试程序对内存的占用，先准备个python程序来消耗内存，代码如下：

```shell
#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import re
import time

def print_help():
    print 'Usage: '
    print '  python mem.py 100MB'
    print '  python mem.py 1GB'

if __name__ == "__main__":
    time.sleep(5)
    if len(sys.argv) == 2:
        pattern = re.compile('^(\d*)([M|G]B)$')
        match = pattern.match(sys.argv[1].upper())
        if match:
            num = int(match.group(1))
            unit = match.group(2)
            if unit == 'MB':
                s = ' ' * (num * 1024 * 1024)
            else:
                s = ' ' * (num * 1024 * 1024 * 1024)

            time.sleep(10000)
        else:
            print_help()
    else:
        print_help()
```

使用方法

```shell
./mem.py 500MB
./mem.py 5GB
```

下面进入系统的 /sys/fs/cgroup 目录下，看看我们可以限制哪些资源。在这个目录下，基本上每个目录代表一类资源。这里以memory资源为例，memory目录下包括下面这些文件

```shell
-rw-r--r--   1 root root   0 May  4 21:11 cgroup.clone_children
--w--w--w-   1 root root   0 May  4 21:11 cgroup.event_control
-rw-r--r--   1 root root   0 May  4 21:11 cgroup.procs
-r--r--r--   1 root root   0 May  4 21:11 cgroup.sane_behavior
drwxr-xr-x   2 root root   0 May  4 23:30 machine.slice
-rw-r--r--   1 root root   0 May  4 21:11 memory.failcnt
--w-------   1 root root   0 May  4 21:11 memory.force_empty
-rw-r--r--   1 root root   0 May  4 21:11 memory.kmem.failcnt
-rw-r--r--   1 root root   0 May  4 21:11 memory.kmem.limit_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.kmem.max_usage_in_bytes
-r--r--r--   1 root root   0 May  4 21:11 memory.kmem.slabinfo
-rw-r--r--   1 root root   0 May  4 21:11 memory.kmem.tcp.failcnt
-rw-r--r--   1 root root   0 May  4 21:11 memory.kmem.tcp.limit_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.kmem.tcp.max_usage_in_bytes
-r--r--r--   1 root root   0 May  4 21:11 memory.kmem.tcp.usage_in_bytes
-r--r--r--   1 root root   0 May  4 21:11 memory.kmem.usage_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.limit_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.max_usage_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.memsw.failcnt
-rw-r--r--   1 root root   0 May  4 21:11 memory.memsw.limit_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.memsw.max_usage_in_bytes
-r--r--r--   1 root root   0 May  4 21:11 memory.memsw.usage_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.move_charge_at_immigrate
-r--r--r--   1 root root   0 May  4 21:11 memory.numa_stat
-rw-r--r--   1 root root   0 May  4 21:11 memory.oom_control
----------   1 root root   0 May  4 21:11 memory.pressure_level
-rw-r--r--   1 root root   0 May  4 21:11 memory.soft_limit_in_bytes
-r--r--r--   1 root root   0 May  4 21:11 memory.stat
-rw-r--r--   1 root root   0 May  4 21:11 memory.swappiness
-r--r--r--   1 root root   0 May  4 21:11 memory.usage_in_bytes
-rw-r--r--   1 root root   0 May  4 21:11 memory.use_hierarchy
-rw-r--r--   1 root root   0 May  4 21:11 notify_on_release
-rw-r--r--   1 root root   0 May  4 21:11 release_agent
drwxr-xr-x 118 root root   0 May 13 21:00 system.slice
-rw-r--r--   1 root root   0 May  4 21:11 tasks
drwxr-xr-x   2 root root   0 May  4 23:30 user.slice

```

其中
- 带 memsw 的表示虚拟内存，不带 memsw 的表示物理内存；
- memory.limit_in_bytes：是用来限制内存使用的；
- memory.memsw.limit_in_bytes：内存＋swap空间使用的总量限制，memory.memsw.limit_in_bytes 必须大于或等于 memory.limit_in_byte；
- memory.oom_control：内存超限之后的 oom 行为控制，0 为开启此功能；
- memory.use_hierarchy：当设为 1 时，子控制组进程的内存占用也会计入父控制组，并上溯到所有 memory.use_hierarchy = 1 的祖先控制组，默认为 0；

下面就看看怎样使用 cgroup 来限制进程内存。

首先在 /sys/fs/cgroup/memory 目录下创建一个 mem_test 目录，创建后会发下 mem_test 目录下会默认创建床很多文件，大多数和 /sys/fs/cgroup/memory 目录下的文件类似。

```shell
# cd /sys/fs/cgroup/memory/
# mkdir mem_test
# cd mem_test
# echo 1024M > memory.limit_in_bytes
# echo 0 > memory.oom_control
# echo 1 > memory.use_hierarchy
```

然后运行测试程序，同时使用 ps 和 top 命令来监控程序执行，可以发现当程序内存超过1G后，程序就会被 kill 掉。

```shell
# cgexec -g memory:mem_test ./mem.py 2GB
```

这里我们只使用了一种资源限制，如果要组合多种资源限制可以使用类似如下的方法：

```shell
cgexec -g *:<name> <my_programe>

cgexec -g cpu,memory:<name> <my_programe>

cgexec -g cpu,memory:<name1> -g swap:<name2> <my_programe>
```