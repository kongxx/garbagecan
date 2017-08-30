# Python进程间通信之共享内存

前一篇博客说了怎样通过命名管道实现进程间通信，但是要在windows是使用命名管道，需要使用python调研windows api，太麻烦，于是想到是不是可以通过共享内存的方式来实现。查了一下，Python中可以使用mmap模块来实现这一功能。

Python中的mmap模块是通过映射同一个普通文件实现共享内存的。文件被映射到进程地址空间后，进程可以像访问内存一样对文件进行访问。

不过，mmap在linux和windows上的API有些许的不一样，具体细节可以查看mmap的文档。

下面看一个例子：

## server.py

这个程序使用 test.dat 文件来映射内存，并且分配了1024字节的大小，每隔一秒更新一下内存信息。

``` python
import mmap
import contextlib
import time

with open("test.dat", "w") as f:
    f.write('\x00' * 1024)

with open('test.dat', 'r+') as f:
    with contextlib.closing(mmap.mmap(f.fileno(), 1024, access=mmap.ACCESS_WRITE)) as m:
        for i in range(1, 10001):
            m.seek(0)
            s = "msg " + str(i)
            s.rjust(1024, '\x00')
            m.write(s)
            m.flush()
            time.sleep(1)
```

## client.py

这个程序从上面映射的文件 test.dat 中加载数据到内存中。

``` python
import mmap
import contextlib
import time

while True:
    with open('test.dat', 'r') as f:
        with contextlib.closing(mmap.mmap(f.fileno(), 1024, access=mmap.ACCESS_READ)) as m:
            s = m.read(1024).replace('\x00', '')
            print s
    time.sleep(1)
```

上面的代码可以在linux和windows上运行，因为我们明确指定了使用 test.dat 文件来映射内存。如果我们只需要在windows上实现共享内存，可以不用指定使用的文件，而是通过指定一个tagname来标识，所以可以简化上面的代码。如下：

## server.py

``` python
import mmap
import contextlib
import time

with contextlib.closing(mmap.mmap(-1, 1024, tagname='test', access=mmap.ACCESS_WRITE)) as m:
    for i in range(1, 10001):
        m.seek(0)
        m.write("msg " + str(i))
        m.flush()
        time.sleep(1)
```

## client.py

``` python
import mmap
import contextlib
import time

while True:
    with contextlib.closing(mmap.mmap(-1, 1024, tagname='test', access=mmap.ACCESS_READ)) as m:
        s = m.read(1024).replace('\x00', '')
        print s
    time.sleep(1)
```
