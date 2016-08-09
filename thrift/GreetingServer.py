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
        self.log = {}

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
