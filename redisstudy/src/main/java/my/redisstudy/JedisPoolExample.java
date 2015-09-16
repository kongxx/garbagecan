package my.redisstudy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolExample {
	private static final String REDIS_HOST = "192.168.0.88";

	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(1);
		config.setMaxTotal(10);
		config.setMaxWaitMillis(10*1000);

		JedisPool pool = new JedisPool(config, REDIS_HOST);

		Jedis jedis = pool.getResource();
		try {
			jedis.set("foo", "bar");
			System.out.println(jedis.get("foo"));
			jedis.del("foo");
			System.out.println(jedis.get("foo"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}

		pool.destroy();
	}
}
