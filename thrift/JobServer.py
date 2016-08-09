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

    def getJobs(self, size):
        return self.jobs[0:size]

handler = JobServiceHandler()
processor = JobService.Processor(handler)
transport = TSocket.TServerSocket(port=9090)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print 'Starting the server...'
server.serve()
print 'done.'
