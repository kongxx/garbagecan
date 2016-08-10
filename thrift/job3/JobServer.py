#!/usr/bin/env python
import sys, glob, time, json, traceback
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
            traceback.print_stack()
            raise JobServiceException(message=e.message)

    def close(self, operation_id):
        try:
            self.manager.drop_instance(operation_id)
        except Exception as e:
            traceback.print_stack()
            raise JobServiceException(message=e.message)

    def getJobs(self, operation_id, offset, size):
        instance = self.manager.get_instance(operation_id)
        if instance:
            try:
                return instance.get_jobs(offset, size)
            except Exception as e:
                traceback.print_stack()
                raise JobServiceException(message=e.message)
        else:
            raise JobServiceException(message='Invalid operation id.')

class JobServiceInstance:
    def __init__(self, jobs):
        self.jobs = jobs

    def get_jobs(self, offset, size):
        return self.jobs[offset:offset+size]

    def clean(self):
        self.jobs = []

class JobServiceInstanceManager:
    def __init__(self):
        self.instances = dict()
        self.job_num = 100000
        self.attr_num = 100
        self.jobs = self.init_jobs(self.job_num, self.attr_num)

    def get_instance(self, id):
        return self.instances.get(id)

    def create_instance(self, id):
        instance = JobServiceInstance(self.jobs)
        self.instances.update({id: instance})
        return instance

    def drop_instance(self, id):
        instance = self.instances.get(id)
        if instance:
            instance.clean()
        self.instances.pop(id)

    def init_jobs(self, job_num, attr_num):
        print 'prepare jobs begin'
        jobs = []
        attrs = dict()
        for i in range(0, attr_num):
            attrs.update({'attr_' + str(i): 'long long long string ... ' + str(i)})

        for i in range(0, job_num):
            job = Job(id=str(i), name='job_' + str(i), queue='normal',
                        user='kongxx', cmd='sleep 1',
                        attrs=json.dumps(attrs))
            jobs.append(job)
        print 'prepare jobs end'
        return jobs

handler = JobServiceHandler()
processor = JobService.Processor(handler)
transport = TSocket.TServerSocket(port=9090)
tfactory = TTransport.TBufferedTransportFactory()
pfactory = TBinaryProtocol.TBinaryProtocolFactory()

server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

print 'Starting the server...'
server.serve()
print 'done.'
