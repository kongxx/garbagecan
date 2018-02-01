# Windows上使用Python绑定CPU

在 Windows 平台上，有时候我们想让自己的程序在指定的CPU上运行，此时我看可以通过下面几个API来实现

- win32process.SetProcessAffinityMask(hProcess, mask) - 绑定进程到CPU
- win32api.GetSystemInfo()[5] - 查询当前机器有几个CPU
- win32api.GetCurrentProcess() - 获取当前进程信息

其中绑定CPU是mask的值如下：
``` shell
0x0001 1
0x0002 2
0x0003 1 or 2
0x0004 3
0x0005 1 or 3
0x0006 2 or 3
0x0007 1, 2, or 3
0x000F 1, 2, 3, or 4
...
```

示例

``` shell
import win32process
import win32api

# 绑定到CPU 1
win32process.SetProcessAffinityMask(win32api.GetCurrentProcess(), 0x0001)

# 绑定到CPU 2
win32process.SetProcessAffinityMask(win32api.GetCurrentProcess(), 0x0002)

# 绑定到CPU 1 or 2
win32process.SetProcessAffinityMask(win32api.GetCurrentProcess(), 0x0003)
```
