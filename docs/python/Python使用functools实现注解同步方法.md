# Python使用functools实现注解同步方法

在 Python 中没有类似 Java 中使用的 synchronized 关键字来同步方法，因此在 Python 中要实现同步方法，通常我们是使用 threading.Lock() 来实现。在进入函数的地方获取锁，出函数的时候释放锁，这样实现代码看起好非常不好看。另外网上也有人给出了其它几种实现方式，但看起来都不美气。

今天我在做项目的时候突然想到是不是可以通过 functools 来实现通过注解来标注方法为同步方法。

首先要求自己的类中有一个锁对象并且在类初始化的时候初始化这个锁对象，比如：

``` python
class MyWorker(object):

    def __init__(self):
        self.lock = threading.Lock()
        ...

    ...
```

然后创建一个 synchronized 函数，这个函数装饰具体对象的具体方法，将方法放到获取/释放锁之间来运行，如下

``` python
def synchronized(func):
    @functools.wraps(func)
    def wrapper(self, *args, **kwargs):
        with self.lock:
            return func(self, *args, **kwargs)
    return wrapper
```

最后在需要使用同步的方法上使用 @synchronized 来标准方法是同步方法，比如：

``` python
@synchronized
def test(self):
    ...
```

下面是一个完整例子，仅供参考：

``` python
import threading
import functools
import time


def synchronized(func):
    @functools.wraps(func)
    def wrapper(self, *args, **kwargs):
        with self.lock:
            return func(self, *args, **kwargs)
    return wrapper


class MyWorker(object):

    def __init__(self):
        self.lock = threading.Lock()
        self.idx = 0

    @synchronized
    def test1(self):
        for i in range(1, 11):
            self.idx = self.idx + 1
            print "Test1: " + str(self.idx)
            time.sleep(1)

    @synchronized
    def test2(self):
        for i in range(1, 11):
            self.idx = self.idx + 1
            print "Test2: " + str(self.idx)
            time.sleep(1)

    @synchronized
    def test3(self):
        for i in range(1, 11):
            self.idx = self.idx + 1
            print "Test3: " + str(self.idx)
            time.sleep(1)

worker = MyWorker()

threading.Thread(target=worker.test1).start()
threading.Thread(target=worker.test2).start()
threading.Thread(target=worker.test3).start()
```
