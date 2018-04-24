# Python运行时动态查看进程内部信息

接前两篇“运行时查看线程信息”的博客，我在想，既然我可以随时打印线程信息，那么我是不是可以随时打印进程内部的其它信息呢？比如，实时查看一些对象属性等，这样可以帮助我们在不重新启动应用程序的情况下就可以观察进程的执行状态。（这里暂时不考虑那些使用第三方库或工具的情况）

根据这个想法，查看了一下python的动态加载模块的方法，感觉这个想法还是比较靠谱，应该可以实现，所以动手写了个小测试验证了一把。（这里说明一下，只是验证性的，生产环境要使用的话，还是有不少问题需要考虑的。）

下面就是测试时考虑要做到的
- 还是使用 SIGQUIT 信号即“kill -3”来触发执行打印进程内部对象属性.
- foo.py主程序，包括注册信号处理函数，创建一个全局的对象用来保存一些属性，启动一个线程让主线程不退出。
- foo.py主程序中的信号处理函数动态加载一个指定路径的下的模块，这里我们就假定这个模块路径是"/tmp/my_modules"，可以根据需要修改。然后调用这个模块中的方法来打印一些进程信息。
- /tmp/my_modules/bar.py需要动态加载的模块，其中访问foo模块中的一个对象，并打印对象属性。
- 要能随时动态修改要查看的进程状态，即在不重启进程的情况下，通过修改bar.py文件修改要实现查看的内容。

## 主程序 foo.py
``` python
#!/usr/bin/env /usr/bin/python3.4
# -*- coding: utf-8 -*-
import sys
import threading
import signal
from datetime import datetime
import time


class MyObject(object):
    def __init__(self):
        self.data = {}
        self.data['a'] = 'aaa'
        self.data['b'] = 'bbb'
        self.data['c'] = 'ccc'


def test():
    while True:
        print(datetime.now())
        time.sleep(2)

# 信号处理函数
def signal_handler(signum, frame):
    try:
        # 动态加载模块
        sys.path.append("/tmp/my_modules")

        # 导入bar模块
        bar = __import__('bar')

        # 重新加载模块，为的是可以随时重新加载模块
        reload(bar)

        # 调用动态加载模块的方法
        bar.execute()
    except BaseException as e:
        print(e)


my_object = MyObject()

if __name__ == "__main__":
    try:
        signal.signal(signal.SIGQUIT, signal_handler)

        threading.Thread(target=test).start()

        while True:
            time.sleep(60)
    except KeyboardInterrupt:
        sys.exit(1)
```

## 需要动态加载的模块 /tmp/my_modules/bar.py
``` python
#!/usr/bin/env /usr/bin/python3.4
# -*- coding: utf-8 -*-
import foo


def execute():
    # 打印foo模块中的对象
    print "my_object: %s " % foo.my_object.data

```

## 测试

首先运行foo.py
``` shell
$ python foo.py
```

然后找到foo.py的进程号，然后使用“kill -3”来触发打印内存对象的方法
``` python
$ kill -3 <pid>
```

此时应该可以看到foo.py进程打印my_object的属性。

修改一下 /tmp/my_modules/bar.py 文件，然后再次运行“$ kill -3 <pid>”，可以看到模块被重新加载了，然后打印的新的内容。
