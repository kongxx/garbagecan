# Linux查看进程栈信息

今天在Linux上调试程序程序的时候发现有时候程序会莫名其妙的hang住，于是就想能不能找到当时程序有那些线程，都在做什么。找了一下linux命令，还真可以满足我的需求。下面看一个小例子。

先准备一段程序，为了简单起见这里使用python来写，其中创建了两个线程来执行各自的任务。

``` python
import threading
import time


def test1():
    while(True):
        time.sleep(1)
        print 'test1'

def test2():
    while(True):
        time.sleep(1)
        print 'test2'


t1 = threading.Thread(target=test1, args=())
t2 = threading.Thread(target=test2, args=())
t1.start()
t2.start()

time.sleep(12345)
```

然后运行这个程序

``` shell
$ python test.py
```

先使用 “pstree -apl <pid>” 查看进程结构

``` shell
$ pstree -apl 26855
python,26855 test.py
  |-{python},26858
  |-{python},26859
```

然后使用 “ps -Lf <pid>” 查看线程信息

``` shell
$ ps -Lf 26855
UID        PID  PPID   LWP  C NLWP STIME TTY      STAT   TIME CMD
jhadmin  26855 25902 26855  0    3 15:15 pts/5    Sl+    0:00 python test.py
jhadmin  26855 25902 26858  0    3 15:15 pts/5    Sl+    0:00 python test.py
jhadmin  26855 25902 26859  0    3 15:15 pts/5    Sl+    0:00 python test.py
```

最后，可以使用 “pstack <pid>” 查看线程的详细信息，如下：

``` shell
$ pstack 26855
Thread 3 (Thread 0x7f8a344f2700 (LWP 26858)):
#0  0x00007f8a3b5387a3 in select () from /lib64/libc.so.6
#1  0x00007f8a344f5070 in time_sleep () from /usr/lib64/python2.7/lib-dynload/timemodule.so
#2  0x00007f8a3c215af0 in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#3  0x00007f8a3c217e3d in PyEval_EvalCodeEx () from /lib64/libpython2.7.so.1.0
#4  0x00007f8a3c1a188d in function_call () from /lib64/libpython2.7.so.1.0
#5  0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0
#6  0x00007f8a3c2104fd in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#7  0x00007f8a3c2154bd in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#8  0x00007f8a3c2154bd in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#9  0x00007f8a3c217e3d in PyEval_EvalCodeEx () from /lib64/libpython2.7.so.1.0
#10 0x00007f8a3c1a1798 in function_call () from /lib64/libpython2.7.so.1.0
#11 0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0
#12 0x00007f8a3c18b8d5 in instancemethod_call () from /lib64/libpython2.7.so.1.0
#13 0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0
#14 0x00007f8a3c20e6f7 in PyEval_CallObjectWithKeywords () from /lib64/libpython2.7.so.1.0
#15 0x00007f8a3c2465c2 in t_bootstrap () from /lib64/libpython2.7.so.1.0
#16 0x00007f8a3bf1ce25 in start_thread () from /lib64/libpthread.so.0
#17 0x00007f8a3b54134d in clone () from /lib64/libc.so.6
Thread 2 (Thread 0x7f8a33cf1700 (LWP 26859)):
#0  0x00007f8a3b5387a3 in select () from /lib64/libc.so.6
#1  0x00007f8a344f5070 in time_sleep () from /usr/lib64/python2.7/lib-dynload/timemodule.so
#2  0x00007f8a3c215af0 in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#3  0x00007f8a3c217e3d in PyEval_EvalCodeEx () from /lib64/libpython2.7.so.1.0
#4  0x00007f8a3c1a188d in function_call () from /lib64/libpython2.7.so.1.0                                                                                           
#5  0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0                                                                                           
#6  0x00007f8a3c2104fd in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0                                                                                      
#7  0x00007f8a3c2154bd in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0                                                                                      
#8  0x00007f8a3c2154bd in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0                                                                                      
#9  0x00007f8a3c217e3d in PyEval_EvalCodeEx () from /lib64/libpython2.7.so.1.0
#10 0x00007f8a3c1a1798 in function_call () from /lib64/libpython2.7.so.1.0
#11 0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0
#12 0x00007f8a3c18b8d5 in instancemethod_call () from /lib64/libpython2.7.so.1.0
#13 0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0
#14 0x00007f8a3c20e6f7 in PyEval_CallObjectWithKeywords () from /lib64/libpython2.7.so.1.0
#15 0x00007f8a3c2465c2 in t_bootstrap () from /lib64/libpython2.7.so.1.0
#16 0x00007f8a3bf1ce25 in start_thread () from /lib64/libpthread.so.0
#17 0x00007f8a3b54134d in clone () from /lib64/libc.so.6
Thread 1 (Thread 0x7f8a3c6f3740 (LWP 26855)):
#0  0x00007f8a3bf22a0b in do_futex_wait.constprop.1 () from /lib64/libpthread.so.0
#1  0x00007f8a3bf22a9f in __new_sem_wait_slow.constprop.0 () from /lib64/libpthread.so.0
#2  0x00007f8a3bf22b3b in sem_wait@@GLIBC_2.2.5 () from /lib64/libpthread.so.0
#3  0x00007f8a3c242535 in PyThread_acquire_lock () from /lib64/libpython2.7.so.1.0
#4  0x00007f8a3c2461c2 in lock_PyThread_acquire_lock () from /lib64/libpython2.7.so.1.0
#5  0x00007f8a3c215af0 in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#6  0x00007f8a3c217e3d in PyEval_EvalCodeEx () from /lib64/libpython2.7.so.1.0
#7  0x00007f8a3c21533c in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#8  0x00007f8a3c217e3d in PyEval_EvalCodeEx () from /lib64/libpython2.7.so.1.0
#9  0x00007f8a3c21533c in PyEval_EvalFrameEx () from /lib64/libpython2.7.so.1.0
#10 0x00007f8a3c217e3d in PyEval_EvalCodeEx () from /lib64/libpython2.7.so.1.0
#11 0x00007f8a3c1a1798 in function_call () from /lib64/libpython2.7.so.1.0
#12 0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0
#13 0x00007f8a3c18b8d5 in instancemethod_call () from /lib64/libpython2.7.so.1.0
#14 0x00007f8a3c17c8e3 in PyObject_Call () from /lib64/libpython2.7.so.1.0
#15 0x00007f8a3c17c9c5 in call_function_tail () from /lib64/libpython2.7.so.1.0
#16 0x00007f8a3c17ccfb in PyObject_CallMethod () from /lib64/libpython2.7.so.1.0
#17 0x00007f8a3c232f29 in Py_Finalize () from /lib64/libpython2.7.so.1.0
#18 0x00007f8a3c244325 in Py_Main () from /lib64/libpython2.7.so.1.0
#19 0x00007f8a3b46ac05 in __libc_start_main () from /lib64/libc.so.6
#20 0x000000000040071e in _start ()
```

这里多说一句，如果要看java程序的栈信息，可以使用 "kill -3 <pid>" 来查看，比如：

``` shell
$ nohub java Test > test.out &

$ kill -3 <pid>
```
