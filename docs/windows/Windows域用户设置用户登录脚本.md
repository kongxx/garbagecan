# Windows域用户设置用户登录脚本

这里只说怎么给域用户设置用户登录脚本，所以前面会跳过
- 安装Windows 2012
- 安装AD域
- 添加一个域用户

好，下面开始

首先创建一个测试脚本 test.bat，输出当前用户名和机器名到一个文件里，内容如下

``` shell
echo %COMPUTERNAME% >> c:\test\test.log
echo %USERNAME% >> c:\test\test.log
```

将上面初始化脚本放到下面目录下，如果按照的时候修改了默认AD域安装路径，适当根据自己的情况调整

``` shell
C:\Windows\SYSVOL\sysvol\<domain>\scripts
```

然后在AD域的“用户和计算机”管理中，选中要使用上面登录脚本的用户 -> 属性 -> 配置文件 -> 登录脚本，输入：test.bat

然后在客户机上重新使用这个域用户登录系统，就可以看到在c:\test目录下产生的日志文件。

