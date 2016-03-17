#!/usr/bin/env python
# -*- coding: utf-8 -*-
from datetime import datetime
import time
import threading
from redis_utils import RedisUtils


CHANNEL_DISPATCH = "CHANNEL_DISPATCH"
CHANNEL_RESULT = "CHANNEL_RESULT"


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
        redis = RedisUtils().get_connection()
        for i in range(1, 100):
            print("Dispatch job %s " % str(i))
            ret = redis.publish(CHANNEL_DISPATCH, str(i))
            if ret == 0:
                print("Dispatch job %s failed." % str(i))
            time.sleep(5)
        RedisUtils().release_connection(redis)


class MyServerResultHandleThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        redis = RedisUtils().get_connection()
        p = redis.pubsub()
        p.subscribe(CHANNEL_RESULT)
        for message in p.listen():
            if message['type'] != 'message':
                continue
            print("Received finished job %s " % message['data'])

        RedisUtils().release_connection(redis)


class MySlave():
    def __init__(self):
        pass

    def start(self):
        MyRunJobThread().start()


class MyRunJobThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        redis = RedisUtils().get_connection()
        p = redis.pubsub()
        p.subscribe(CHANNEL_DISPATCH)
        for message in p.listen():
            if message['type'] != 'message':
                continue
            print("Received dispatched job %s " % message['data'])
            time.sleep(2)
            print("Send finished job %s " % message['data'])
            ret = redis.publish(CHANNEL_RESULT, message['data'])
            if ret == 0:
                print("Send finished job %s failed." % message['data'])
        RedisUtils().release_connection(redis)


if __name__ == "__main__":
    MySlave().start()
    MyMaster().start()

    time.sleep(10000)
