#!/usr/bin/env python
# -*- coding: utf-8 -*-
import types
import redis
from singleton import singleton


@singleton
class RedisConfig(object):
    host = '192.168.0.41'
    port = 6379
    db = 0
    max_connections = 10
    timeout = 10

    def __init__(self):
        pass


@singleton
class RedisUtils(object):

    JOBS_KEY_PREFIX = 'JOBS_'
    JOBS_SCHEDULED_KEY_PREFIX = 'JOBS_SCHEDULED_'
    JOBS_STATUS_KEY = 'JOBS_STATUS_MAP'
    JOBS_RUSAGE_KEY = 'JOBS_RUSAGE_MAP'

    def __init__(self):
        self._pool = None
        self.configure(RedisConfig())

    def configure(self, redis_config):
        if self._pool is not None:
            self.release()

        self._pool = redis.BlockingConnectionPool(
            host=redis_config.host,
            port=redis_config.port,
            db=redis_config.db,
            max_connections=redis_config.max_connections,
            timeout=redis_config.timeout)

    def release(self):
        try:
            self._pool.disconnect()
        except:
            pass

        try:
            self._pool.release()
        except:
            pass

    def get_connection(self):
        return redis.StrictRedis(connection_pool=self._pool)

    def release_connection(self, connection):
        try:
            self._pool.release(connection)
        except:
            pass

    def execute(self, func, *args, **kwargs):
        conn = None
        try:
            conn = self.get_connection()
            ret = func(conn, *args, **kwargs)
            if not isinstance(ret, types.NoneType):
                return ret
        finally:
            self.release_connection(conn)
