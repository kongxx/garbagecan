#!/usr/bin/env python
# -*- coding: utf-8 -*-
from datetime import datetime
from redis_utils import RedisUtils


def pub():
    redis = RedisUtils().get_connection()
    redis.publish("mychannel1", "mychannel1_" + str(datetime.now()))
    redis.publish("mychannel2", "mychannel2_" + str(datetime.now()))
    RedisUtils().release_connection(redis)

pub()