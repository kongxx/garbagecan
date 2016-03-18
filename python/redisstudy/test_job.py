#!/usr/bin/env python
# -*- coding: utf-8 -*-
from datetime import datetime
import time
import threading
import random
import redis


REDIS_HOST = '192.168.0.88'
REDIS_PORT = 6379
REDIS_DB = 0
CHANNEL_DISPATCH = 'CHANNEL_DISPATCH'
CHANNEL_RESULT = 'CHANNEL_RESULT'


class MyMaster():
    def __init__(self):
        pass

    def start(self):
        MyServerResultHandleThread().start()
        MyServerDispatchThread().start()


class MyServerDispatchThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        for i in range(1, 100):
            channel = CHANNEL_DISPATCH + '_' + str(random.randint(1, 3))
            print("Dispatch job %s to %s" % (str(i), channel))
            ret = r.publish(channel, str(i))
            if ret == 0:
                print("Dispatch job %s failed." % str(i))
            time.sleep(5)


class MyServerResultHandleThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        p = r.pubsub()
        p.subscribe(CHANNEL_RESULT)
        for message in p.listen():
            if message['type'] != 'message':
                continue
            print("Received finished job %s" % message['data'])


class MySlave():
    def __init__(self):
        pass

    def start(self):
        for i in range(1, 4):
            MyJobWorkerThread(CHANNEL_DISPATCH + '_' + str(i)).start()


class MyJobWorkerThread(threading.Thread):
    
    def __init__(self, channel):
        threading.Thread.__init__(self)
        self.channel = channel

    def run(self):
        r = redis.StrictRedis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)
        p = r.pubsub()
        p.subscribe(self.channel)
        for message in p.listen():
            if message['type'] != 'message':
                continue
            print("%s: Received dispatched job %s " % (self.channel, message['data']))
            print("%s: Run dispatched job %s " % (self.channel, message['data']))
            time.sleep(2)
            print("%s: Send finished job %s " % (self.channel, message['data']))
            ret = r.publish(CHANNEL_RESULT, message['data'])
            if ret == 0:
                print("%s: Send finished job %s failed." % (self.channel, message['data']))


if __name__ == "__main__":
    MySlave().start()
    MyMaster().start()

    time.sleep(10000)
