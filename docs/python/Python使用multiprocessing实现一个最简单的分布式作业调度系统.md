
# Python使用multiprocessing实现一个最简单的分布式作业调度系统

## 介绍
Python的multiprocessing模块不但支持多进程，其中managers子模块还支持把多进程分布到多台机器上。一个服务进程可以作为调度者，将任务分布到其他多个机器的多个进程中，依靠网络通信。

想到这，就在想是不是可以使用此模块来实现一个简单的作业调度系统。

## 实现

### Job

首先创建一个Job类，为了测试简单，只包含一个job id属性

job.py
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-

class Job:
    def __init__(self, job_id):
        self.job_id = job_id
```

### Master

Master用了派发作业和显示运行完成的作业信息

master.py
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-

from Queue import Queue
from multiprocessing.managers import BaseManager
from job import Job


class Master:

    def __init__(self):
        # 派发出去的作业队列
        self.dispatched_job_queue = Queue()
        # 完成的作业队列
        self.finished_job_queue = Queue()

    def get_dispatched_job_queue(self):
        return self.dispatched_job_queue

    def get_finished_job_queue(self):
        return self.finished_job_queue

    def start(self):
        # 把派发作业队列和完成作业队列注册到网络上
        BaseManager.register('get_dispatched_job_queue', callable=self.get_dispatched_job_queue)
        BaseManager.register('get_finished_job_queue', callable=self.get_finished_job_queue)

        # 监听端口和启动服务
        manager = BaseManager(address=('0.0.0.0', 8888), authkey='jobs')
        manager.start()

        # 使用上面注册的方法获取队列
        dispatched_jobs = manager.get_dispatched_job_queue()
        finished_jobs = manager.get_finished_job_queue()

        # 这里一次派发10个作业，等到10个作业都运行完后，继续再派发10个作业
        job_id = 0
        while True:
            for i in range(0, 10):
                job_id = job_id + 1
                job = Job(job_id)
                print('Dispatch job: %s' % job.job_id)
                dispatched_jobs.put(job)

            while not dispatched_jobs.empty():
                job = finished_jobs.get(60)
                print('Finished Job: %s' % job.job_id)

        manager.shutdown()

if __name__ == "__main__":
    master = Master()
    master.start()


```

### Slave

Slave用来运行master派发的作业并将结果返回

slave.py
``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time
from Queue import Queue
from multiprocessing.managers import BaseManager
from job import Job


class Slave:

    def __init__(self):
        # 派发出去的作业队列
        self.dispatched_job_queue = Queue()
        # 完成的作业队列
        self.finished_job_queue = Queue()

    def start(self):
        # 把派发作业队列和完成作业队列注册到网络上
        BaseManager.register('get_dispatched_job_queue')
        BaseManager.register('get_finished_job_queue')

        # 连接master
        server = '127.0.0.1'
        print('Connect to server %s...' % server)
        manager = BaseManager(address=(server, 8888), authkey='jobs')
        manager.connect()

        # 使用上面注册的方法获取队列
        dispatched_jobs = manager.get_dispatched_job_queue()
        finished_jobs = manager.get_finished_job_queue()

        # 运行作业并返回结果，这里只是模拟作业运行，所以返回的是接收到的作业
        while True:
            job = dispatched_jobs.get(timeout=1)
            print('Run job: %s ' % job.job_id)
            time.sleep(1)
            finished_jobs.put(job)
        
if __name__ == "__main__":
    slave = Slave()
    slave.start()

```

## 测试
分别打开三个linux终端，第一个终端运行master，第二个和第三个终端用了运行slave，运行结果如下

master
``` shell
$ python master.py 
Dispatch job: 1
Dispatch job: 2
Dispatch job: 3
Dispatch job: 4
Dispatch job: 5
Dispatch job: 6
Dispatch job: 7
Dispatch job: 8
Dispatch job: 9
Dispatch job: 10
Finished Job: 1
Finished Job: 2
Finished Job: 3
Finished Job: 4
Finished Job: 5
Finished Job: 6
Finished Job: 7
Finished Job: 8
Finished Job: 9
Dispatch job: 11
Dispatch job: 12
Dispatch job: 13
Dispatch job: 14
Dispatch job: 15
Dispatch job: 16
Dispatch job: 17
Dispatch job: 18
Dispatch job: 19
Dispatch job: 20
Finished Job: 10
Finished Job: 11
Finished Job: 12
Finished Job: 13
Finished Job: 14
Finished Job: 15
Finished Job: 16
Finished Job: 17
Finished Job: 18
Dispatch job: 21
Dispatch job: 22
Dispatch job: 23
Dispatch job: 24
Dispatch job: 25
Dispatch job: 26
Dispatch job: 27
Dispatch job: 28
Dispatch job: 29
Dispatch job: 30

```

slave1
``` shell
$ python slave.py 
Connect to server 127.0.0.1...
Run job: 1 
Run job: 2 
Run job: 3 
Run job: 5 
Run job: 7 
Run job: 9 
Run job: 11 
Run job: 13 
Run job: 15 
Run job: 17 
Run job: 19 
Run job: 21 
Run job: 23 

```

slave2
``` shell
$ python slave.py 
Connect to server 127.0.0.1...
Run job: 4 
Run job: 6 
Run job: 8 
Run job: 10 
Run job: 12 
Run job: 14 
Run job: 16 
Run job: 18 
Run job: 20 
Run job: 22 
Run job: 24 

```
