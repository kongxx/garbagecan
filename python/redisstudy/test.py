#!/usr/bin/env python
# -*- coding: utf-8 -*-
import time
from redis.exceptions import *
from redis_utils import RedisUtils


def test_getset():
    redis = RedisUtils().get_connection()
    assert redis.set("mykey", "myvalue")
    assert redis.get("mykey") == "myvalue"
    assert redis.get("nokey") == None
    RedisUtils().release_connection(redis)


def test_pipeline():
    redis = RedisUtils().get_connection()
    pipe = redis.pipeline()
    assert pipe.set("mykey1", "myvalue1")
    assert pipe.set("mykey2", "myvalue2")
    pipe.execute()
    assert redis.get("mykey1") == "myvalue1"
    assert redis.get("mykey2") == "myvalue2"
    RedisUtils().release_connection(redis)


def test_transaction1():
    redis = RedisUtils().get_connection()
    pipe = redis.pipeline()
    try:
        pipe.watch("mykey1", "mykey2")
        pipe.multi()
        assert pipe.set("mykey1", "myvalue1")
        assert pipe.set("mykey2", "myvalue2")
        time.sleep(10)
        pipe.execute()
        print redis.get("mykey1")
        print redis.get("mykey2")
    except WatchError as e:
        print e
    finally:
        pipe.reset()
        RedisUtils().release_connection(redis)


def test_transaction2():
    redis = RedisUtils().get_connection()
    with redis.pipeline() as pipe:
        try:
            pipe.watch("mykey1", "mykey2")
            pipe.multi()
            assert pipe.set("mykey1", "myvalue1")
            assert pipe.set("mykey2", "myvalue2")
            time.sleep(10)
            pipe.execute()
            print redis.get("mykey1")
            print redis.get("mykey2")
        except WatchError as e:
            print e
        finally:
            RedisUtils().release_connection(redis)

# test_getset()
# test_pipeline()
# test_transaction1()
# test_transaction2()