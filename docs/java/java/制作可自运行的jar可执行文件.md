# 制作可自运行的jar可执行文件

今天看到有人把Java的jar包制作成一个可执行文件，于是乎我也试了一把，确实也挺简单的，记录一下。

首先创建一个java类，比如放的my这个package下，java代码如下：

``` java
package my;

public class Greeting {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

编译一下这个Java文件

``` shell
$ javac my/Greeting.java
```

先打包运行一下试试

``` shell
$ jar -cef my.Greeting greeting.jar my
$ java -jar greeting.jar
Hello World!
```

创建一个脚本文件 greeting，内容如下：

``` shell
#!/bin/sh

exec java -jar $0 "$@"
```

将上面创建的jar文件的内容追加到 greeting 脚本中并赋予 greeting 脚本文件可执行权限，如下：

``` shell
$ cat ./greeting.jar >> ./greeting
$ chmod +x ./greeting
```

运行最终文件测试一下
``` shell
$ ./greeting
Hello World!
```