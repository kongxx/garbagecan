
# Thrift获取大量数据

## 说明

在Thrift中，方法调用操作都是使用的短连接来实现，即每次调用都是独立的，用完连接就关闭，所以对于类似需要获取大量数据的情况，我们一次方法调用获取的数据量太大，会导致性能很差，此时我们期望能有一种类似socket编程中的用法，即获取一个长连接，每次只取一部分数据，然后循环获取，直到数据都取完后再一次关闭连接。而这种使用长连接的方式在thrift中是没有默认实现的，下面我们就看看怎么通过一些其它方法来实现这种操作效果。

另外说明一下，对于数据保存在数据库中的情况，我们可以依赖数据库的分页功能来实现，所以也就不适用这里的情况，这里仅仅是针对那些数据是通过命令行调用来获取或者从非数据库获取数据的情况。

## 优化前程序

下面的代码是最初的实现，即一次获取全部数据。

### job.thrift

接口定义文件，定义了一个方法来获取服务器上的所有作业信息。

``` python

struct Job {
    1: string id,
    2: string name,
    3: string queue,
    4: string user,
    5: string cmd,
}

service JobService {
    list<Job> getJobs()
}

```

### JobServer.py

Server端代码，模拟在内存中有10万个作业，这里仅仅是模拟用，真实情况下可能是通过调用一个命令行获取了10万个作业记录。

``` python
#!/usr/bin/env python
import sys, glob
sys.path.append('gen-py')

from job import JobService
from job.ttypes import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

class JobServiceHandler:
    def __init__(self):
        self.jobs = []
        for i in range(0, 100000):
            self.jobs.append(Job(id=str(i), name='job_' + str(i), queue='normal', user='kongxx', cmd='sleep 1'))

    def getJobs(self):
        return self.jobs

handler = JobServiceHandler()
processor = JobService.Processor(handler)
transport = TSocket.TServerSocket(port=9090)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print 'Starting the server...'
server.serve()
print 'done.'

```

### JobClient.py

客户端代码，建立到服务端的连接，然后一次获取所有数据。

``` python
#!/usr/bin/env python
import sys, glob
sys.path.append('gen-py')

from job import JobService
from job.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

try:
    transport = TSocket.TSocket('localhost', 9090)
    transport = TTransport.TBufferedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    client = JobService.Client(protocol)

    transport.open()

    client.getJobs(100000)

    transport.close()
except Thrift.TException, tx:
    print '%s' % (tx.message)

```

### 测试
- 使用"thrift -r --gen py job.thrift"生成代码
- 运行"python JobServer.py"
- 运行"python JobClient.py"

## 优化后程序

- 优化后的程序，改优化前程序的一次获取所有数据为分批获取部分数据。
- 增加了open和close方法来定义一个查询操作的完整性，有点类是数据库操作的获取一个连接和关闭一个连接。

### job.thrift

接口定义文文件
- open() - 模拟建立一个操作事务，返回一个操作编号，需要保证此操作ID唯一，这里仅仅是测试，所以简单使用了系统时间作操作ID，生产环境需要保证测ID唯一。例子中open方法会在服务器端模拟产生了10万作业数据。
- close() - 关闭一个操作事务，清理服务器端的数据。
- getJobs() - 分批获取一个操作事务中的数据。getJobs函数必须在open和close之间调用，否则会报错。

``` python

struct Job {
    1: string id,
    2: string name,
    3: string queue,
    4: string user,
    5: string cmd,
}

exception JobServiceException {
  1: i32 code,
  2: string message
}

service JobService {
    string open() throws (1: JobServiceException e),
    void close(1: string operationId) throws (1: JobServiceException e),
    list<Job> getJobs(1: string operationId, 2: i32 offset, 3: i32 size) throws (1: JobServiceException e)
}

```

### JobServer.py

Server端代码：
- JobServiceInstance类 - 维护一个作业查询实例，在构造函数中模拟产生10万个作业。
- JobServiceInstanceManager类 - 一个管理作业查询实例的类，用来创建，销毁和获取作业查询实例。
- JobServiceHandler类 - 处理具体的查询操作，会根据需要返回结果或抛出异常。所有的操作都是通过JobServiceInstanceManager类和JobServiceInstance类来实现。

``` python
#!/usr/bin/env python
import sys, glob, time
sys.path.append('gen-py')

from job import JobService
from job.ttypes import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

class JobServiceHandler:
    def __init__(self):
        self.manager = JobServiceInstanceManager()

    def open(self):
        try:
            operation_id = str(time.time())
            self.manager.create_instance(operation_id)
            return operation_id
        except Exception as e:
            raise JobServiceException(message=e.message)

    def close(self, operation_id):
        try:
            self.manager.drop_instance(operation_id)
        except Exception as e:
            raise JobServiceException(message=e.message)

    def getJobs(self, operation_id, offset, size):
        instance = self.manager.get_instance(operation_id)
        if instance:
            try:
                return instance.get_jobs(offset, size)
            except Exception as e:
                raise JobServiceException(message=e.message)
        else:
            raise JobServiceException(message='Invalid operation id.')

class JobServiceInstance:
    def __init__(self):
        self.jobs = []
        for i in range(0, 100000):
            self.jobs.append(Job(id=str(i), name='job_' + str(i), queue='normal', user='kongxx', cmd='sleep 1'))

    def get_jobs(self, offset, size):
        return self.jobs[offset:offset+size]

    def clean(self):
        self.jobs = []

class JobServiceInstanceManager:
    def __init__(self):
        self.instances = dict()

    def get_instance(self, id):
        return self.instances.get(id)

    def create_instance(self, id):
        instance = JobServiceInstance()
        self.instances.update({id: instance})
        return instance

    def drop_instance(self, id):
        instance = self.instances.get(id)
        if instance:
            instance.clean()
        self.instances.pop(id)

handler = JobServiceHandler()
processor = JobService.Processor(handler)
transport = TSocket.TServerSocket(port=9090)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print 'Starting the server...'
server.serve()
print 'done.'

```

### JobClient.py

客户端类：
- 首先使用client.open()获取一个操作编号。
- 根据操作编号，查询偏移量和每页大小查询部分数据。
- 查询完毕后调用client.close()来关闭一个查询操作。

``` python
#!/usr/bin/env python
import sys, glob
sys.path.append('gen-py')

from job import JobService
from job.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

try:
    transport = TSocket.TSocket('localhost', 9090)
    transport = TTransport.TBufferedTransport(transport)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    client = JobService.Client(protocol)

    transport.open()

    offset = 0
    size = 10
    total = 0

    operation_id = client.open()

    while True:
        jobs = client.getJobs(operation_id, offset, size)
        if len(jobs) == 0:
            break
        offset += size
        total += len(jobs)
        for job in jobs:
            print job
    print 'total: %s' % total
    client.close(operation_id)

    transport.close()
except Thrift.TException, tx:
    print '%s' % (tx.message)

```

### 测试
- 使用"thrift -r --gen py job.thrift"生成代码
- 运行"python JobServer.py"
- 运行"python JobClient.py"

### 优化后问题说明

- 优化前和优化后，如果我们单单看查询时间，似乎变化不大，但是这里仍然是有好处的，由于我们是分批获取的数据，所以我们可以根据需要来异步处理查询到的数据，而不用像优化前，必须等到所有结果都返回才能处理。
- 考虑到有可能client端忘记调用close方法来关闭操作，从而导致服务器端产生垃圾数据的情况，所以我们可以使用设置数据的超时时间来避免。比如：在JobServiceInstanceManager类中增加对JobServiceInstance调用的计时，当每次有调用getJobs()操作就把最后调用时间更新一下，如果检测到一个JobServiceInstance在指定的超时时间内没有调用，就把它从JobServiceInstanceManager类中销毁。
- 测试代码中的操作编号不能保证唯一，生产环境可以根据需要写个算法来生成。
