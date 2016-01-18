# Python 实现简单 Web 服务器

最近有个需求，就是要创建一个简到要多简单就有多简单的web服务器，目的就是需要一个后台进程用来接收请求然后处理并返回结果，因此就想到了使用python来实现。

首先创建一个myapp.py文件，其中定义了一个方法，所有的请求都会经过此方法，可以在此方法里处理传递的url和参数，并返回结果。
``` python
def myapp(environ, start_response):
    status = '200 OK'
    headers = [('Content-type', 'text/html')]
    start_response(status, headers)
    if len(environ['PATH_INFO']) == 1:
        return "Hello World!"
    else:
        return "Hello {name}!".format(name=environ['PATH_INFO'][1:])
```

然后创建一个 server.py文件，其中启动了一个Http服务，并且使用上面创建的app来接收请求并处理
``` python
from wsgiref.simple_server import make_server

from myapp import myapp

httpd = make_server('', 8000, myapp)
print "Serving HTTP on port 8000..."

httpd.serve_forever()
```

最后运行“python server.py”来启动服务。

在浏览器里分别输入下面的url来测试一下结果
http://localhost:8000
http://localhost:8000/kongxx


# 使用 pyinstaller 打包 Python 程序为可执行文件

首先安装 pyinstaller 工具包
``` bash
pip install pyinstaller
```

这里假定打包前一篇文章中的web服务器，此时运行下面命令
``` bash
pyinstaller -n myapp -F server.py myapp.py
```
运行完成后就会在当前目录下创建出build和dist目录，在dist目录下就会有一个myapp的可执行文件。

最后运行myapp这个可执行文件就可以了。

# APScheduler: LookupError: No trigger by the name "interval" was found

## 环境
python: 2.6.6
PyInstaller: 2.1
APScheduler: 开始是3.0.1，后来是3.0.5

## 问题一

### 问题描述

以前在别的机器上开发的python程序（python2.7)，在新的机器上运行时报错
``` python
LookupError: No trigger by the name "interval" was found
```

程序代码
``` python
import os, time
from datetime import datetime
from apscheduler.schedulers.background import BackgroundScheduler

def myjob():
    print('myjob: %s' % datetime.now())
    time.sleep(5)

if __name__ == '__main__':
    scheduler = BackgroundScheduler()
    scheduler.add_job(myjob, 'interval', seconds=1)
    scheduler.start()

    try:
        while True:
            time.sleep(5)
    except (KeyboardInterrupt, SystemExit):
        scheduler.shutdown()
```

### 原因
是由于低版本的setuptools导致

### 解决办法
``` bash
sudo pip install --upgrade setuptools
sudo pip install --ignore-installed apscheduler
```

然后再次运行上面的python代码，问题解决。

## 问题二

### 问题描述

第一个问题解决后，在运行使用pyinstaller打包生成的可执行文件的时候报错
``` python
Traceback (most recent call last):
  File "<string>", line 11, in <module>
  File ".../out00-PYZ.pyz/apscheduler.schedulers.base", line 330, in add_job
  File ".../out00-PYZ.pyz/apscheduler.schedulers.base", line 782, in _create_trigger
  File ".../out00-PYZ.pyz/apscheduler.schedulers.base", line 766, in _create_plugin_instance
LookupError: No trigger by the name "interval" was found
```

### 原因
感觉好像是由于pyinstaller打包的时候使用了错误版本的APScheduler。（不确定）？？？

### 解决办法

不要在add_job方法中使用“'interval', seconds=1”做trigger，而是先创建一个IntervalTrigger对象，然后add_job的时候使用这个对象，即：

修改原来代码中
```
    scheduler.add_job(myjob, 'interval', seconds=1)
```
为
```
    trigger = IntervalTrigger(seconds=1)
    scheduler.add_job(myjob, trigger)
```

完整代码如下
``` python
def myjob():
    print('myjob: %s' % datetime.now())
    time.sleep(5)

if __name__ == '__main__':
    scheduler = BackgroundScheduler()
    trigger = IntervalTrigger(seconds=1)
    scheduler.add_job(myjob, trigger)
    scheduler.start()

    try:
        while True:
            time.sleep(5)
    except (KeyboardInterrupt, SystemExit):
        scheduler.shutdown()
 

```

然后用PyInstaller重新打包，此时再运行可执行文件的时候就不会报错了。
