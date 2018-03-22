# Python运行时查看线程信息

今天遇见一个 Python 问题，在测试环境中发现用 Python 写的程序有时候会慢慢变慢，在使用 "pstack <pid>" 查看进程的时候发现起了很多的线程，并且越来越多，这肯定是程序有问题，但是使用 pstack 命令看不到具体每个线程是在做什么，于是我就想是不是可以在不影响进程运行的情况下随时查看每个线程都在干什么。

于是乎，我大致想了一下
1. 可以使用 signal 模块在处理程序接收 kill 信号，这样我就可以使用 “kill -3 <pid>” 来给进程发信号，然后输出线程信息而不影响进程继续运行。这里具体信号可以根据情况换成别的信号。
2. 可以使用 sys._current_frames() 和 threading.enumerate() 来获取进程的线程信息。

好，有想法了，开工干活，看下面一个具体例子

``` python
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
        print datetime.now()
        time.sleep(2)


def signal_handler(signum, frame):
    try:
        print '---'
        result = []
        result.append("*** STACKTRACE - START ***\n")

        threads = threading.enumerate()
        for thread in threads:
            stack = sys._current_frames()[thread.ident]
            result.append("\nThread ID: %s, Name: %s\n" % (thread.ident, thread.name))
            for filename, line_no, name, line in traceback.extract_stack(stack):
                result.append('  "%s", line %d, in %s\n' % (filename, line_no, name))
                if line:
                    result.append("    %s\n" % (line.strip()))

        result.append("\n*** STACKTRACE - END ***\n")

        file = os.path.join(tempfile.gettempdir(), datetime.now().strftime('%Y%m%d%H%M%S') + ".log")
        with open(file, 'w+') as f:
            f.writelines(result)
    except BaseException as e:
        print e


if __name__ == "__main__":
    try:
        signal.signal(signal.SIGQUIT, signal_handler)

        threading.Thread(target=test).start()

        while True:
            time.sleep(60)
    except KeyboardInterrupt:
        sys.exit(1)
```

运行上面的代码，然后使用 “kill -3 <pid>” 给进程发 SIGQUIT 信号，此时进程并不会退出而是会继续运行，然后在 “/tmp” 下查看进程的详细输出文件，内容大致如下。

``` shell
*** STACKTRACE - START ***

Thread ID: 140093508736832, Name: MainThread
  "test2.py", line 48, in <module>
    time.sleep(60)
  "test2.py", line 27, in signal_handler
    for filename, line_no, name, line in traceback.extract_stack(stack):

Thread ID: 140093324805888, Name: Thread-1
  "/usr/lib64/python2.7/threading.py", line 784, in __bootstrap
    self.__bootstrap_inner()
  "/usr/lib64/python2.7/threading.py", line 811, in __bootstrap_inner
    self.run()
  "/usr/lib64/python2.7/threading.py", line 764, in run
    self.__target(*self.__args, **self.__kwargs)
  "test2.py", line 14, in test
    time.sleep(2)

*** STACKTRACE - END ***
```
从日志文件中可以看到每个线程名和ID，并且可以看到每个线程都正在做什么。