# Python信号处理模块signal

Python中对信号处理的模块主要是使用signal模块，但signal主要是针对Unix系统，所以在Windows平台上Python不能很好的发挥信号处理的功能。

要查看Python中的信号量，可以使用dir(signal)来查看。

## signal.signal()

在signal模块中，主要是使用signal.signal()函数来预设信号处理函数

```python
singnal.signal(signalnum, handler)
```

其中第一个参数是信号量，第二个参数信号处理函数。

下面看个简单的例子，其中

- 定义了一个信号处理函数signal_handler()，用来处理程序接收到信号时执行的操作
- 一段循环等待发送信号

```python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import signal
import time

def signal_handler(signum, frame):
    print('Received signal: ', signum)

while True:
    signal.signal(signal.SIGHUP, signal_handler) # 1
    signal.signal(signal.SIGINT, signal_handler) # 2
    signal.signal(signal.SIGQUIT, signal_handler) # 3
    signal.signal(signal.SIGALRM, signal_handler) # 14
    signal.signal(signal.SIGTERM, signal_handler) # 15
    signal.signal(signal.SIGCONT, signal_handler) # 18
    while True:
        print('waiting')
        time.sleep(1)
```

运行上面的程序

```bash
python test.py
```

然后另外开一个终端，找到对应的进程，并执行下面的kill操作

```
kill -1 <pid>
kill -2 <pid>
kill -3 <pid>
kill -14 <pid>
kill -15 <pid>
kill -18 <pid>
kill -9 <pid> # 最后杀死进程
```

此时可以看到test.py的输出，打印的就是具体接收到的信号。

这里注意一点就是程序中注册了SIGINT信号，所以在运行程序后使用CTRL+C并不能结束进程，而是仍然打印进程接收到的信号。

## signal.alarm()

另外，signal模块提供了一个很有用的函数signal.alarm()，它用于在一定时间后向进程自身发送SIGALRM信号，比如下面的例子设置5秒后向自己发送一个SIGALRM信号。

```python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import signal
import time

def signal_handler(signum, frame):
    print('Received signal: ', signum)

while True:
    signal.signal(signal.SIGALRM, signal_handler) # 14
    signal.alarm(5)
    while True:
        print('waiting')
        time.sleep(1)
```

## 参考

- http://man7.org/linux/man-pages/man7/signal.7.html
- https://docs.python.org/2/library/signal.html#module-signal