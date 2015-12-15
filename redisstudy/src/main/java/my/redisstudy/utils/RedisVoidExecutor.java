package my.redisstudy.utils;

import redis.clients.jedis.Jedis;

public interface RedisVoidExecutor {
	
	public void execute(Jedis jedis);
	
}
