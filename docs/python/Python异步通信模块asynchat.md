# Python异步通信模块asynchat

## 介绍
**文档上说**：这个模块建立在 asyncore 基础结构上，简化了异步客户端和服务器，使得处理带有以任意字符串终止或者可变长度的元素的协议更加容易。asynchat 定义了你子类化的抽象类 async_chat ，提供了 collect_incoming_data() 和 found_terminator() 方法的实现。它使用了和 asyncore一样的异步循环，和两种类型的通道， asyncore.dispatcher 和 asynchat.async_chat，它们可以被自由地混合在通道映射中。当接收传入的连接请求时，一个 asyncore.dispatcher 服务器通道往往会产生新的 asynchat.async_chat 通道对象。

模块主要包括：

- asynchat.async_chat类 - 这个类是asyncore.dispatcher的抽象子类。一般使用其collect_incoming_data()和found_terminator()方法。
  - collect_incoming_data() - 接收数据。
  - found_terminator() - 当输入数据流符合由 set_terminator() 设置的终止条件时被调用。
  - set_terminator() - 设置终止条件。
  - push() - 向通道压入数据以确保其传输。


## 例子

下面看一个简单的例子来实现一个简单的问候功能

``` python
import asyncore
import asynchat
import socket


class HTTPChannel(asynchat.async_chat):

    def __init__(self, sock, addr):
        asynchat.async_chat.__init__(self, sock)
        self.set_terminator('\r\n')
        self.request = None
        self.response = None
        self.data = None
        self.shutdown = 0

    def collect_incoming_data(self, data):
        self.data = data

    def found_terminator(self):
        if not self.request:
            self.request = self.data.split(None, 2)
            if len(self.request) != 3:
                self.shutdown = 1
            else:
                name = self.request[1].replace('/', '', 1)
            self.response = 'Hello %s! %s' % (name, '\r\n')
            self.set_terminator('\r\n\r\n')
        else:
            self.push('HTTP/1.0 200 OK\r\n')
            self.push('Content-type: text/html\r\n')
            self.push('\r\n')
            
            self.push('<html>\r\n')
            self.push('<body>\r\n')
            self.push(self.response)
            self.push('</body>\r\n')
            self.push('</html>\r\n')
            self.close_when_done()

class HTTPServer(asyncore.dispatcher):

    def __init__(self):
        asyncore.dispatcher.__init__(self)
        
        self.port = 8888
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.bind(("", self.port))
        self.listen(5)
        print 'Serving at port', self.port

    def handle_accept(self):
        conn, addr = self.accept()
        HTTPChannel(conn, addr)


s = HTTPServer()
asyncore.loop()
```

- HTTPChannel- asynchat.async_chat类的子类。
  - 构造函数中首先设置了初始终止字符串为'\r\n'。用来接收http请求。
  - collect_incoming_data() - 接收数据并保存。
  - found_terminator() - 处理数据。比如请求“http://localhost:8888/kongxx”，当第一次接受到数据时，数据内容类似“GET /kongxx HTTP/1.1”，此时会将其中的kongxx当作用户信息保留。并设置响应文本为“Hello kongxx!”，然后设置终止字符串为'\r\n\r\n'。此时当继续有新数据接收到，这里是http的header信息，直到碰到http到header的结束'\r\n\r\n'时，触发响应结果逻辑，然后程序push了响应头和内容。




- HTTPServer - 用了启动服务来监听端口。



## 测试

运行上面的程序，然后在浏览器地址栏输入http://localhost:8888/kongxx，就会得到下面的响应结果。
``` shell
Hello kongxx！ 
```

