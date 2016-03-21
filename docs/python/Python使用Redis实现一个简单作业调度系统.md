# Python使用Redis实现一个简单作业调度系统

## 概述

Redis作为内存数据库的一个典型代表，已经在很多应用场景中被使用，这里仅就Redis的pub/sub功能来说说怎样通过此功能来实现一个简单的作业调度系统。这里只是想展现一个简单的想法，所以还是有很多需要考虑的东西没有包括在这个例子中，比如错误处理，持久化等。

下面是实现上的想法

- MyMaster：集群的master节点程序，负责产生作业，派发作业和获取执行结果。
- MySlave：集群的计算节点程序，每个计算节点一个，负责获取作业并运行，并将结果发送会master节点。
- channel CHANNEL_DISPATCH：每个slave节点订阅一个channel，比如“CHANNEL_DISPATCH_<idx或机器名>”，master会向此channel中publish被dispatch的作业。
- channel CHANNEL_RESULT：用来保存作业结果的channel，master和slave共享此channel，master订阅此channel来获取作业运行结果，每个slave负责将作业执行结果发布到此channel中。

## Master代码
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import time
import threading
import random
import redis


REDIS_HOST = 'localhost'
REDIS_PORT = 6379
REDIS_DB = 0
CHANNEL_DISPATCH = 'CHANNEL_DISPATCH'
CHANNEL_RESULT = 'CHANNEL_RESULT'


class MyMaster():
    def __init__(self):
        pass

    def start(self):
        MyServerResultHandleThread().start()
        MyServerDispatchThread().start()


class MyServerDispatchThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        for i in range(1, 100):
            channel = CHANNEL_DISPATCH + '_' + str(random.randint(1, 3))
            print("Dispatch job %s to %s" % (str(i), channel))
            ret = r.publish(channel, str(i))
            if ret == 0:
                print("Dispatch job %s failed." % str(i))
            time.sleep(5)


class MyServerResultHandleThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        p = r.pubsub()
        p.subscribe(CHANNEL_RESULT)
        for message in p.listen():
            if message['type'] != 'message':
                continue
            print("Received finished job %s" % message['data'])


if __name__ == "__main__":
    MyMaster().start()
    time.sleep(10000)

```

说明

- MyMaster类 - master主程序，用来启动dispatch和resulthandler的线程
- MyServerDispatchThread类 - 派发作业线程，产生作业并派发到计算节点
- MyServerResultHandleThread类 - 作业运行结果处理线程，从channel里获取作业结果并显示

## Slave代码
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
from datetime import datetime
import time
import threading
import random
import redis


REDIS_HOST = 'localhost'
REDIS_PORT = 6379
REDIS_DB = 0
CHANNEL_DISPATCH = 'CHANNEL_DISPATCH'
CHANNEL_RESULT = 'CHANNEL_RESULT'


class MySlave():
    def __init__(self):
        pass

    def start(self):
        for i in range(1, 4):
            MyJobWorkerThread(CHANNEL_DISPATCH + '_' + str(i)).start()


class MyJobWorkerThread(threading.Thread):

    def __init__(self, channel):
        threading.Thread.__init__(self)
        self.channel = channel

    def run(self):
        r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        p = r.pubsub()
        p.subscribe(self.channel)
        for message in p.listen():
            if message['type'] != 'message':
                continue
            print("%s: Received dispatched job %s " % (self.channel, message['data']))
            print("%s: Run dispatched job %s " % (self.channel, message['data']))
            time.sleep(2)
            print("%s: Send finished job %s " % (self.channel, message['data']))
            ret = r.publish(CHANNEL_RESULT, message['data'])
            if ret == 0:
                print("%s: Send finished job %s failed." % (self.channel, message['data']))


if __name__ == "__main__":
    MySlave().start()
    time.sleep(10000)

```
说明

- MySlave类 - slave节点主程序，用来启动MyJobWorkerThread的线程
- MyJobWorkerThread类 - 从channel里获取派发的作业并将运行结果发送回master

## 测试
- 首先运行MySlave来定义派发作业channel。
- 然后运行MyMaster派发作业并显示执行结果。