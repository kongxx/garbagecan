#!/usr/bin/env python
# -*- coding: utf-8 -*-
from datetime import datetime
import time
import threading
from redis_utils import RedisUtils


class MyPubThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        redis = RedisUtils().get_connection()
        for i in range(1, 100):
            print("publish mychannel1 %s " % "mychannel1_" + str(i))
            redis.publish("mychannel1", "mychannel1_" + str(i))

            print("publish mychannel2 %s " % "mychannel2_" + str(i))
            redis.publish("mychannel2", "mychannel2_" + str(i))

            time.sleep(2)
        RedisUtils().release_connection(redis)


class MySubThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        # self.sub1()
        self.sub2()

    def sub1(self):
        redis = RedisUtils().get_connection()
        p = redis.pubsub()
        p.subscribe("mychannel1", "mychannel2")
        for message in p.listen():
            print("Get message %s " % message['data'])
        RedisUtils().release_connection(redis)

    def sub2(self):
        redis = RedisUtils().get_connection()
        p = redis.pubsub()

        def my_handler(message):
            print("Get message %s " % message['data'])

        p.subscribe(**{'mychannel1': my_handler})
        p.subscribe(**{'mychannel2': my_handler})

        while True:
            message = p.get_message()
            if message:
                time.sleep(0.001)

        RedisUtils().release_connection(redis)


if __name__ == "__main__":
    MySubThread().start()
    MyPubThread().start()

    time.sleep(10000)
