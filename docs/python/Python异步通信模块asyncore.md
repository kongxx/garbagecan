# Python异步通信模块asyncore

## 介绍

Python的asyncore模块提供了以异步的方式写入套接字服务的客户端和服务器的基础结构。

模块主要包括：

- asyncore.loop(...) -  用于循环监听网络事件。loop()函数负责检测一个字典，字典中保存dispatcher的实例。

- asyncore.dispatcher类 - 一个底层套接字对象的简单封装。这个类有少数由异步循环调用的，用来事件处理的函数。
  dispatcher类中的writable()和readable()在检测到一个socket可以写入或者数据到达的时候被调用，并返回一个bool值，决定是否调用handle_read或者handle_write。

- asyncore.dispatcher_with_send类 - 一个 dispatcher的子类，添加了简单的缓冲输出能力，对简单的客户端很有用。


## 例子

下面看一个简单的例子

``` python
import time
import asyncore
import socket
import threading


class EchoHandler(asyncore.dispatcher_with_send):

    def handle_read(self):
        data = self.recv(1024)
        if data:
            self.send(data)

class EchoServer(asyncore.dispatcher):

    def __init__(self, host, port):
        asyncore.dispatcher.__init__(self)
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.set_reuse_addr()
        self.bind((host, port))
        self.listen(5)

    def handle_accept(self):
        conn, addr = self.accept()
        print 'Incoming connection from %s' % repr(addr)
        self.handler = EchoHandler(conn)

class EchoClient(asyncore.dispatcher):

    def __init__(self, host, port):
        asyncore.dispatcher.__init__(self)
        self.messages = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10']
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.connect((host, port))

    def handle_connect(self):
        pass

    def handle_close(self):
        self.close()

    def handle_read(self):
        print self.recv(1024)

    def writable(self):
        return (len(self.messages) > 0)

    def handle_write(self):
        if len(self.messages) > 0: 
            self.send(self.messages.pop(0))

class EchoServerThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        server = EchoServer('localhost', 9999)
        asyncore.loop()
        
class EchoClientThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        client = EchoClient('localhost', 9999)
        asyncore.loop()

EchoServerThread().start()
time.sleep(2)
EchoClientThread().start()
```

- EchoServer - 响应服务器端程序，负责监听一个端口，并响应客户端发送的消息然后原样返回给客户端。其中handle_accept()方法定义当一个连接到来的时候要执行的操作，这里指定了使用一个Handler来出来发送来的数据。


- EchoHandler - 服务器端数据响应类，接收数据并把数据原样发回。


- EchoClient - 响应服务客户端程序，负责连接响应服务器。其中

  - messages - 定义了一个要发送的消息列表，每次发送一个消息，知道列表为空为止。

  - handle_read() - 处理接收到的数据，这里把收到的数据打印的终端上。

  - writable() - 判断是否有数据可以向服务器端发送。

  - handle_write() - 当writable()函数返回True时，写入数据。


- EchoServerThread - 用来启动服务器端程序的线程。
- EchoClientThread - 用来启动客户端端程序的线程。

## 测试

运行上面的测试代码，可以看到服务器和客户端建立了连接后，响应了客户端发送来的10个数字，然后关闭了连接。

``` shell
Incoming connection from ('127.0.0.1', 51424)
1
2
3
4
5
6
7
8
9
10
```

