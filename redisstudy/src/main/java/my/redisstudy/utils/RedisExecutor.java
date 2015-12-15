package my.redisstudy.utils;

import redis.clients.jedis.Jedis;

public interface RedisExecutor<T> {

	public T execute(Jedis jedis);
}
