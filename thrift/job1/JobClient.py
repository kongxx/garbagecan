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

    total = 0
    for i in range(0, 1):
        jobs = client.getJobs(100000)
        total += len(jobs)
        # for job in jobs:
        #     print job
    print 'total: %s' % total

    transport.close()
except Thrift.TException, tx:
    print '%s' % (tx.message)
