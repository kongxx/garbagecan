# Windows通过组策略设置开机自启动脚本

首先创建一个启动脚本 init.bat，内容如下：

``` shell
time /t >> c:\test\test.log
echo %COMPUTERNAME% >> c:\test\test.log
echo %USERNAME% >> c:\test\test.log
```

然后将此文件放入 C:\Windows\System32\GroupPolicy\Machine\Scripts\Startup 目录下。

在 开始菜单 -> 运行中输入 gpedit.msc 启动本地组策略编辑器

选择 Local Computer Policy -> Computer Configuration -> Windows Settings -> Scripts (Startup/Shutdown) -> Startup

在弹出窗口中选择上面的脚本文件，然后确定。也可以在 “Show Files” 的弹出窗口中查看具体的脚本的目录，这个目录应该和上面存放 init.bat 脚本的目录一致。
