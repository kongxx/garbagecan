# Windows通过schtasks设置开机自启动脚本

## 脚本

首先创建一个启动脚本 init.bat，内容如下：

``` shell
time /t >> c:\test\test.log
echo %COMPUTERNAME% >> c:\test\test.log
echo %USERNAME% >> c:\test\test.log
```

## 创建任务

``` shell
schtasks.exe /create /tn "init" /ru SYSTEM /sc ONSTART /tr "C:\test\init.bat"
```

## 删除任务

``` shell
schtasks /delete /tn init
```

## 查询任务

``` shell
schtasks /query /fo TABLE
schtasks /query /fo TABLE /tn init

schtasks /query /fo LIST
schtasks /query /fo LIST /tn init
```

## 手动运行任务

``` shell
schtasks /run /tn init
```

## 注意

这里有个特别需要注意的地方，在 cmd 窗口中运行上面的命令的时候，记得先使用 “chcp 437” 切换一下 cmd 窗口的活动代码页编号，否则如果是中文操作系统可能会出错。
