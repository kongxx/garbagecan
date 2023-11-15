## MacOS设置JAVA_HOME环境变量

首先先查看一下，系统当前使用的java是谁，可以使用/usr/libexec/java_home命令

``` shell
% /usr/libexec/java_home
/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
```

检查一下这个路径下的文件，发现这是一个jre的目录。加上-V参数看看当前系统上安装了那些Java版本

``` shell
% /usr/libexec/java_home -V
Matching Java Virtual Machines (2):
    1.8.212.10 (x86_64) "Oracle Corporation" - "Java" /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
    1.8.0_212 (x86_64) "Oracle Corporation" - "Java SE 8" /Library/Java/JavaVirtualMachines/jdk1.8.0_212.jdk/Contents/Home
/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
```

其中 “1.8.0_212 (x86_64) "Oracle Corporation" - "Java SE 8" /Library/Java/JavaVirtualMachines/jdk1.8.0_212.jdk/Contents/Home” 是我需要使用的JDK。

修改 ~/.zshrc 文件，设置JAVA_HOME和PATH

``` shell
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_212.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
```

最后执行 “source ~/.zshrc” 使其生效。

