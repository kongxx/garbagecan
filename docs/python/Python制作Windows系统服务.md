# Python制作Windows系统服务

最近有个Python程序需要安装并作为Windows系统服务来运行，过程中碰到一些坑，整理了一下。

## Python服务类

首先Python程序需要调用一些Windows系统API才能作为系统服务，具体内容如下：

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
import time

import win32api
import win32event
import win32service
import win32serviceutil
import servicemanager


class MyService(win32serviceutil.ServiceFramework):

    _svc_name_ = "MyService"
    _svc_display_name_ = "My Service"
    _svc_description_ = "My Service"

    def __init__(self, args):
        self.log('init')
        win32serviceutil.ServiceFramework.__init__(self, args)
        self.stop_event = win32event.CreateEvent(None, 0, 0, None)

    def SvcDoRun(self):
        self.ReportServiceStatus(win32service.SERVICE_START_PENDING)
        try:
            self.ReportServiceStatus(win32service.SERVICE_RUNNING)
            self.log('start')
            self.start()
            self.log('wait')
            win32event.WaitForSingleObject(self.stop_event, win32event.INFINITE)
            self.log('done')
        except BaseException as e:
            self.log('Exception : %s' % e)
            self.SvcStop()

    def SvcStop(self):
        self.ReportServiceStatus(win32service.SERVICE_STOP_PENDING)
        self.log('stopping')
        self.stop()
        self.log('stopped')
        win32event.SetEvent(self.stop_event)
        self.ReportServiceStatus(win32service.SERVICE_STOPPED)

    def start(self):
        time.sleep(10000)

    def stop(self):
        pass

    def log(self, msg):
        servicemanager.LogInfoMsg(str(msg))

    def sleep(self, minute):
        win32api.Sleep((minute*1000), True)

if __name__ == "__main__":
    if len(sys.argv) == 1:
        servicemanager.Initialize()
        servicemanager.PrepareToHostSingle(MyService)
        servicemanager.StartServiceCtrlDispatcher()
    else:
        win32serviceutil.HandleCommandLine(MyService)

```

## pyinstaller打包

``` shell
pyinstaller -F MyService.py
```

## 测试

``` shell
# 安装服务
dist\MyService.exe install

# 启动服务
sc start MyService

# 停止服务
sc stop MyService

# 删除服务
sc delete MyService
```