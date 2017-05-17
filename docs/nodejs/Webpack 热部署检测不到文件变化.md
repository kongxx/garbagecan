# Webpack 热部署检测不到文件变化

今天在用Webpack开发的时候，突然发现文件变动后热部署功能不工作了，感觉好像是webpack检测不到文件的修改了。折腾了半天，开始一直以为是自己的代码有问题了，结果一次无意识的重启了一下机器后发现又可以热部署了，感觉像是见鬼了。于是继续观察。

一天后，不幸再次降临，问题又出现了。

调研了一下，原来 Webpack 的热部署功能是使用 inotify 来监视文件变化，其中 fs.inotify.max_user_watches 表示同一用户同时可以添加的watch数目（watch一般是针对目录，决定了同时同一用户可以监控的目录数量）

因此，查看了一下系统当前的 max_user_watches 值

``` shell
$ cat /proc/sys/fs/inotify/max_user_watches
8192
```

8192是默认值，可能是这个值太小，而我的app下的文件目录太多，于是试着修改一下

``` shell
echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p
```

修改后查看一下修改结果

``` shell
$ cat /proc/sys/fs/inotify/max_user_watches
524288
```

好了，试试修改结果吧，再次测试 webpack 的热部署功能，一切正常了。
