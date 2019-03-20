# 设置Docker允许非root用户操作

以前写过一篇博客介绍过怎样允许非root用户来访问Docker，当时的方法是将用户加入的docker组里，这样做安全是安全了，但是实在是太麻烦了，对于我这种安全要求不太高的情况，实在是不合适。于是想能不能有啥办法一劳永逸的解决这个问题。你还别说，还真有办法，你们说神奇不。呵呵，就是这么神奇。

解决办法就是修改 /var/run/docker.sock 文件的权限，让普通用户也可以访问。

先查看一下默认这个文件的权限是啥
``` shell
$ sudo ls -al /var/run/docker.sock
srw-rw---- 1 root root 0 Feb 13 02:14 /var/run/docker.sock
```

修改文件权限
``` shell
sudo chmod 666 /var/run/docker.sock
```

修改后，再查看一下这个文件的权限
``` shell
$ sudo ls -al /var/run/docker.sock
srw-rw-rw- 1 root root 0 Feb 13 02:14 /var/run/docker.sock
```

现在，随便找个用户试试吧。