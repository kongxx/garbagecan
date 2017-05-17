# Python Socket编程

在使用Python做socket编程时，由于需要使用阻塞（默认）的方式来读取数据流，此时对于数据的结束每次都需要自己处理，太麻烦。并且网上也没找到太好的封装，所以就自己写了个简单的封装。

封装思路
1. 客户端每次请求均发送一个 SocketRequest 对象，其中封装具体的数据，这里使用json。对于要发送的数据，会自动添加一个结束符标识（EOF = '0x00'）。
2. 服务器端接收数据时，根据结束符标识来生成完整的数据，并解包成 SocketRequest 对象。
3. 服务器端根据 SocketRequest 的内容，来生成 SocketResponse 对象，这里使用了一个 SimpleRequestHandler 类来处理，例子中就是没有做任何处理，然后原样返回。
4. 服务器端发送 SocketResponse 给客户端。其中也需要对包做一个封装，会自动添加一个结束符标识（EOF = '0x00'）。
5. 客户接收数据时，根据结束符标识来生成完整的数据，并解包成 SocketResponse 对象，然后返回。

## 封装类

### sockets.py

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket
import pickle
import thread


PORT = 12345
EOF = '0x00'


class SocketServer(object):

    def __init__(self, port=None):
        self.port = port

    def startup(self):
        sock_server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock_server.bind(('0.0.0.0', self.port))
        sock_server.listen(0)

        while True:
            sock, address = sock_server.accept()
            thread.start_new_thread(self.__invoke, (sock, address))

    def shutdown(self):
        pass

    def __invoke(self, sock, address):
        try:
            full_data = ''
            while True:
                data = sock.recv(1024)
                if data is None:
                    return

                full_data += data
                if full_data.endswith(EOF):
                    full_data = full_data[0:len(full_data) - len(EOF)]
                    request = pickle.loads(full_data)
                    response = SimpleRequestHandler().handle(request)
                    sock.sendall(pickle.dumps(response) + EOF)
                    return
        except Exception as e:
            print e
        finally:
            sock.close()


class SocketClient(object):

    def __init__(self, host, port):
        self.host = host
        self.port = port

    def execute(self, request):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((self.host, self.port))

        try:
            sock.sendall(pickle.dumps(request) + EOF)
            full_data = ''
            while True:
                data = sock.recv(1024)
                if data:
                    full_data += data
                    if full_data.endswith(EOF):
                        full_data = full_data[0:len(full_data) - len(EOF)]
                        response = pickle.loads(full_data)
                        return response
                else:
                    return None
        except Exception as e:
            print e
            return None
        finally:
            sock.close()


class SocketRequest(object):
    def __init__(self, data):
        self.data = data

    def __repr__(self):
        return repr(self.__dict__)


class SocketResponse(object):
    def __init__(self, data):
        self.data = data

    def __repr__(self):
        return repr(self.__dict__)


class SimpleRequestHandler(object):
    def __init__(self):
        pass

    def __repr__(self):
        return repr(self.__dict__)

    def handle(self, request):
        return SocketResponse(request.data)

```

## 测试

### socket_server.py

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
from agent.sockets import *

ss = SocketServer(PORT)
ss.startup()
```

### socket_client.py

``` python
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pickle
from agent.sockets import *


sc = SocketClient('localhost', PORT)
request = SocketRequest('abc')
response = sc.execute(request)
print request
print response
```

### 运行测试

``` shell
首先，运行 socket_server.py

然后，运行 socket_client.py
```
