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
