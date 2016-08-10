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
            raise JobServiceException(code=1, message='Unknown error.')

    def close(self, operation_id):
        try:
            self.manager.drop_instance(operation_id)
        except Exception as e:
            print e
            raise JobServiceException(code=1, message='Unknown error.')

    def getJobs(self, operation_id, offset, size):
        instance = self.manager.get_instance(operation_id)
        if instance:
            try:
                return instance.get_jobs(offset, size)
            except Exception as e:
                raise JobServiceException(code=1, message='Unknown error.')
        else:
            raise JobServiceException(code=1, message='Invalid operation id.')

class JobServiceInstance:
    def __init__(self):
        self.jobs = []
        for i in range(0, 100):
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
