# Windows AD域通过组策略设置域用户登录和注销脚本

首先准备一个测试脚本 test.bat，输出当前用户名和机器名到一个文件里，内容如下

``` shell
echo %COMPUTERNAME% >> c:\test\test.log
echo %USERNAME% >> c:\test\test.log
```

运行 “gpmc.msc” 命令来启动“组策略管理编辑器”.

在“组策略管理编辑器”左侧导航树上选择 “Default Domain Policy” -> 用户配置 -> 策略 -> Windows 设置 -> 脚本（登录/注销）

双击 “登录”，在 “登录” 属性中添加上面的脚本。这里可以先在属性窗口的下部使用“显示文件”来查看默认脚本文件都放在什么地方，比如，在我的环境下是：

``` shell
登录脚本路径
\\<domain>\sysvol\<domain>\Policies\{31B2F340-016D-11D2-945F-00C04FB984F9}\User\Scripts\Logon

注销脚本路径
\\<domain>\sysvol\<domain>\Policies\{31B2F340-016D-11D2-945F-00C04FB984F9}\User\Scripts\Logoff
```

将上面的脚本放入上面的位置，然后确定。

“注销” 脚本和 “登录” 脚本类似。

最后，在客户机上使用任意域用户登录系统，就可以看到在c:\test\目录下产生的日志文件。
