package my.redisstudy.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtils {
	
	private static final String REDIS_HOST = "localhost";
	private static final int REDIS_PORT = 6379;
	
	public static JedisUtils instance = new JedisUtils();
	
	private JedisPool jedisPool;
	
	private JedisUtils() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setTestOnBorrow(true);
		
		jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT);
	}
	
	public static JedisUtils getInstance() {
		return instance;
	}
	
	public void execute(RedisVoidExecutor executor) {
		Jedis jedis = getConnection();
		executor.execute(jedis);
		closeConnection(jedis);
	}
	
	public <T> T execute(RedisExecutor<T> executor) {
		Jedis jedis = getConnection();
		T t = executor.execute(jedis);
		closeConnection(jedis);
		return t;
	}
	
	private Jedis getConnection() {
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}

	private void closeConnection(Jedis jedis) {
		jedis.close();
	}
}
