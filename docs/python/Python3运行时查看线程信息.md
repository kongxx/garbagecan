# Python3运行时查看线程信息

前一篇文章说了一下在是用Python2的情况下怎样查看运行时线程信息，今天查资料的时候发现，原来在Python3中引入了 faulthandler 模块，可以简化我们很多工作，甚至，如果不需要保持进程继续运行的情况下可以不用修改我们已有的代码。具体 faulthandler 模块的使用，可以参考：

> faulthandler https://docs.python.org/3/library/faulthandler.html

先准备一个小程序，就是周期性的往命令行终端输出一下时间，如下：

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
import sys
import threading
import traceback
import signal
import tempfile
from datetime import datetime
import time


def test():
    while True:
        print(datetime.now())
        time.sleep(2)


if __name__ == "__main__":
    try:
        threading.Thread(target=test).start()

        while True:
            time.sleep(60)
    except KeyboardInterrupt:
        sys.exit(1)
```

要启用 dump thread 的功能，可以通过三种方式来打开：

1. 使用 PYTHONFAULTHANDLER=true 环境变量
2. 使用 python3 -q -X faulthandler 来起用
3. 代码方式来启用

这里我们通过设置环境变量 “PYTHONFAULTHANDLER=true” 来启用，用下面方式运行程序：

``` shell
$ PYTHONFAULTHANDLER=true ./test.py
```

然后在另开一个终端运行下面的命令

``` shell
$ kill -SIGABRT `ps -ef | grep test.py | grep -v 'grep' | awk '{print $2}'`
```

此时我们可以在运行 test.py 的终端中看到如下线程信息：

``` shell
Fatal Python error: Aborted

Thread 0x00007f8298430700 (most recent call first):
  File "./test3.py", line 16 in test
  File "/usr/lib64/python3.4/threading.py", line 859 in run
  File "/usr/lib64/python3.4/threading.py", line 911 in _bootstrap_inner
  File "/usr/lib64/python3.4/threading.py", line 879 in _bootstrap

Current thread 0x00007f82a2fcf740 (most recent call first):
  File "./test3.py", line 24 in <module>
Aborted (core dumped)
```

这里我们是通过发送 SIGABRT 信号来触发的，其实也可以使用 SIGSEGV, SIGFPE, SIGABRT, SIGBUS 和 SIGILL 信号来出发。



运行上面的程序，我们发现一个问题，一旦dump threads后，进程就退出了，而如果我们不想让进程退出，该怎么办呢，这就要用到前一篇文章中说提到的复写信号处理函数了。

看一下下面的代码，我们和前一篇博客中一样都是复写 “SIGQUIT” 信号处理函数，在接受到 SIGQUIT 信号后，输出线程信息到 /tmp 目录下，然后程序继续运行。

``` python
#!/usr/bin/env /usr/bin/python3.4
# -*- coding: utf-8 -*-
import os
import sys
import threading
import traceback
import signal
import tempfile
from datetime import datetime
import time
import faulthandler


def test():
    while True:
        print(datetime.now())
        time.sleep(2)


def signal_handler(signum, frame):
    try:
        file = os.path.join(tempfile.gettempdir(), datetime.now().strftime('%Y%m%d%H%M%S') + ".log")
        with open(file, 'w+') as f:
            faulthandler.dump_traceback(file=f, all_threads=True)
    except BaseException as e:
        print(e)


if __name__ == "__main__":
    try:
        signal.signal(signal.SIGQUIT, signal_handler)

        threading.Thread(target=test).start()

        while True:
            time.sleep(60)
    except KeyboardInterrupt:
        sys.exit(1)

```

运行上面的程序，并使用下面的命令来给进程发 SIGQUIT  信号
``` shell
$ kill -SIGQUIT `ps -ef | grep test.py | grep -v 'grep' | awk '{print $2}'`
```

然后可以在 /tmp 目录下找到输出的线程日志，内容如下，同时我们也可以看到程序会继续执行下去而不会推出。

``` shell
Thread 0x00007f13d75d2700 (most recent call first):
  File "./test3.py", line 17 in test
  File "/usr/lib64/python3.4/threading.py", line 859 in run
  File "/usr/lib64/python3.4/threading.py", line 911 in _bootstrap_inner
  File "/usr/lib64/python3.4/threading.py", line 879 in _bootstrap

Current thread 0x00007f13e2171740 (most recent call first):
  File "./test3.py", line 24 in signal_handler
  File "./test3.py", line 36 in <module>

```

