Linux释放内存

在Linux上我们通常可以使用下面的命令来查看系统内存使用情况
``` shell
$ free -m
$ cat /proc/meminfo
```

通常我们可以根据具体情况使用下面的命令来释放内存（需要root权限来执行）

- Freeing Up the Page Cache
``` shell
# echo 1 > /proc/sys/vm/drop_caches
# sync
```

- Freeing Up the Dentries and Inodes
``` shell
# echo 2 > /proc/sys/vm/drop_caches
# sync
```

- Freeing Up the Page Cache, Dentries and Inodes
``` shell
# echo 3 > /proc/sys/vm/drop_caches
# sync
```

也可以使用下面一条长命令
``` shell
sudo su -c 'free -m && sync && echo 3 > /proc/sys/vm/drop_caches && sync && free -m'
```

在我的虚拟机上运行结果如下

``` shell
              total        used        free      shared  buff/cache   available
Mem:           2848        2094          82          29         671         471
Swap:          2047         914        1133

              total        used        free      shared  buff/cache   available
Mem:           2848        2081         294          29         471         491
Swap:          2047         914        1133
```
