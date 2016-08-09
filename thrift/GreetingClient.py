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
