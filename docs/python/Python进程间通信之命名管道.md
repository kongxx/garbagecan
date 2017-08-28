# Python进程间通信之命名管道

管道是一种简单的FIFO通信信道，它是单向通信的。 通常启动进程创建一个管道，然后这个进程创建一个或者多个进程子进程接受管道信息，由于管道是单向通信，所以经常需要创建两个管道来实现双向通信。

命名管道是对传统管道的扩展，默认的管道是匿名管道，只在程序运行时存在；而命名管道是持久化的，当不需要时需要删除它。

命名管道使用文件系统，由mkfifo()方法创建。一旦创建了，两个独立的进程都可以访问它，一个读，另外一个写。

命名管道支持阻塞读和阻塞写操作： 如果一个进程打开文件读，它会阻塞直到另外一个进程写。 但是我们可以指定O_NONBLOCK选项来启用非阻塞模式。

命名管道必须以只读或者只写的模式打开，它不能以读+写的模式打开，因为它时单向通信。如果要实现双向通信，必须打开两个命名管道。

下面是一个 Python 使用命名管道来实现进程间通信的例子

## Server 端
``` python
import os, time

read_path = "/tmp/pipe.in"
write_path = "/tmp/pipe.out"

if os.path.exists(read_path):
    os.remove(read_path)
if os.path.exists(write_path):
    os.remove(write_path)

os.mkfifo(write_path)
os.mkfifo(read_path)

rf = os.open(read_path, os.O_RDONLY)
wf = os.open(write_path, os.O_SYNC | os.O_CREAT | os.O_RDWR)

while True:
    s = os.read(rf, 1024)
    print "received msg: %s" % s
    if len(s) == 0:
        time.sleep(1)
        continue

    if "exit" in s:
        break

    os.write(wf, s)

os.close(rf)
os.close(wf)
```

## Client 端
``` python
import os
import time

write_path = "/tmp/pipe.in"
read_path = "/tmp/pipe.out"

wf = os.open(write_path, os.O_SYNC | os.O_CREAT | os.O_RDWR)
rf = None

for i in range(1, 11):
    msg = "msg " + str(i)
    len_send = os.write(wf, msg)
    print "sent msg: %s" % msg

    if rf is None:
        rf = os.open(read_path, os.O_RDONLY)

    s = os.read(rf, 1024)
    if len(s) == 0:
        break
    print "received msg: %s" % s

    time.sleep(1)

os.write(wf, 'exit')

os.close(rf)
os.close(wf)
```

## 测试
- 首先运行server.py
- 然后运行client.py

---
