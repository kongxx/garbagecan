#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time
from Queue import Queue
from multiprocessing.managers import BaseManager
from job import Job


class Slave:

    def __init__(self):
        self.dispatched_job_queue = Queue()
        self.finished_job_queue = Queue()

    def start(self):
        BaseManager.register('get_dispatched_job_queue')
        BaseManager.register('get_finished_job_queue')

        server = '127.0.0.1'
        print('Connect to server %s...' % server)
        manager = BaseManager(address=(server, 8888), authkey='jobs')
        manager.connect()

        dispatched_jobs = manager.get_dispatched_job_queue()
        finished_jobs = manager.get_finished_job_queue()

        while True:
            job = dispatched_jobs.get(timeout=1)
            print('Run job: %s ' % job.job_id)
            time.sleep(1)
            finished_jobs.put(job)
        
if __name__ == "__main__":
    slave = Slave()
    slave.start()
