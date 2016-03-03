#!/usr/bin/env python
# -*- coding: utf-8 -*-
from redis_utils import RedisUtils


def sub():
    redis = RedisUtils().get_connection()
    p = redis.pubsub()
    p.subscribe("mychannel1", "mychannel2")
    for message in p.listen():
        print message
    RedisUtils().release_connection(redis)

sub()