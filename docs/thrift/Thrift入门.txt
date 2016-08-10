# Thrift入门

## 介绍

今天工作需要，看了一下Apache的Thrift，Thrift是Facebook的核心技术框架之一，使不同语言开发的系统可以通过该框架进行通信。开发者使用thrift提供的格式来定义数据和服务脚本。thrift可以通过定义的脚本自动生成不同语言的代码以支持不同语言之间的通信。thrift支持多种数据通信协议，比如json, binnary等。

下面写了个小程序，由于只是测试用，暂时不需要跨语言，所以就使用python来实现。

## 安装

``` shell
sudo apt-get install thrift-compiler
sudo pip install thrift
```

## 测试程序

### 定义Thrift接口

- greeting.thrift
``` python
service GreetingService {

    string hello(1:string name)

}

```

### 根据接口生成代码

``` python
thrift -r --gen py greeting.thrift
```

运行上面命令后，会在当前目录下生成一个gen-py目录，下面就是生成的python代码。

### 测试服务端

- GreetingServer.py （将代码放在和greeting.thrift相同的位置）
``` python
#!/usr/bin/env python
import sys, glob
sys.path.append('gen-py')

from greeting import GreetingService
from greeting.ttypes import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

class GreetingServiceHandler:
    def __init__(self):
        pass

    def hello(self, name):
        msg = 'Hello ' + name + '!'
        print msg
        return msg

handler = GreetingServiceHandler()
processor = GreetingService.Processor(handler)
transport = TSocket.TServerSocket(port=9090)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print 'Starting the server...'
server.serve()
print 'done.'

```

### 测试客户端

- GreetingClient.py （将代码放在和greeting.thrift相同的位置）
``` python
#!/usr/bin/env python
import sys, glob
sys.path.append('gen-py')

from greeting import GreetingService
from greeting.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

try:
    transport = TSocket.TSocket('localhost', 9090)
    transport = TTransport.TBufferedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    client = GreetingService.Client(protocol)

    transport.open()

    print client.hello('Kongxx')
    print client.hello('Mandy')

    transport.close()
except Thrift.TException, tx:
    print '%s' % (tx.message)

```

### 测试

- 首先运行服务器端代码
``` shell
python GreetingServer.py
```

- 然后运行客户端代码
``` shell
python GreetingClient.py
```
