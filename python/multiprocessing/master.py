#!/usr/bin/env python
# -*- coding: utf-8 -*-

from Queue import Queue
from multiprocessing.managers import BaseManager
from job import Job


class Master:

    def __init__(self):
        self.dispatched_job_queue = Queue()
        self.finished_job_queue = Queue()

    def get_dispatched_job_queue(self):
        return self.dispatched_job_queue

    def get_finished_job_queue(self):
        return self.finished_job_queue

    def start(self):
        BaseManager.register('get_dispatched_job_queue', callable=self.get_dispatched_job_queue)
        BaseManager.register('get_finished_job_queue', callable=self.get_finished_job_queue)

        manager = BaseManager(address=('0.0.0.0', 8888), authkey='jobs')
        manager.start()

        dispatched_jobs = manager.get_dispatched_job_queue()
        finished_jobs = manager.get_finished_job_queue()

        job_id = 0
        while True:
            for i in range(0, 10):
                job_id = job_id + 1
                job = Job(job_id)
                print('Dispatch job: %s' % job.job_id)
                dispatched_jobs.put(job)

            while not dispatched_jobs.empty():
                job = finished_jobs.get(60)
                print('Finished Job: %s' % job.job_id)

        manager.shutdown()

if __name__ == "__main__":
    master = Master()
    master.start()

