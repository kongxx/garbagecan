# react-native工程执行bundler install挂死或超时问题

react-native工程默认执行 “bundler install” 命令时，会出现挂死或超时问题，原因懂的都懂。

解决办法：

先查看一下 gem 源

```shell
$ gem sources -l
*** CURRENT SOURCES ***

https://rubygems.org/
```

修改 gem 源

```shell
gem sources --remove https://rubygems.org/ 

gem sources --add https://gems.ruby-china.com/
gem sources --add https://mirrors.tuna.tsinghua.edu.cn/rubygems/
```

查看一下修改

```shell
gem sources -l
*** CURRENT SOURCES ***

https://gems.ruby-china.com/
https://mirrors.tuna.tsinghua.edu.cn/rubygems/
```

修改 bundle 配置（这一步是不是必须待验证）

```shell
bundle config mirror.https://rubygems.org https://gems.ruby-china.com
或
bundle config mirror.https://rubygems.org https://mirrors.tuna.tsinghua.edu.cn/rubygems
```

然后重新运行 “bundler install”。