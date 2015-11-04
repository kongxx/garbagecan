package my.redisstudy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import redis.clients.jedis.Jedis;

public class JedisUtils {
	private static final String REDIS_HOST = "localhost";
	private static final int REDIS_PORT = 6379;
	private static final int REDIS_CONNECTION_TIMEOUT = 600000;
	private static final int REDIS_TIMEOUT = 600000;
	
	public static Jedis getJedis() {
		String host = REDIS_HOST;
		try {
			if (InetAddress.getLocalHost().getHostName().equals("fanbin")) {
				host = "192.168.0.88";
			}
		} catch (UnknownHostException e) {
			// Ignore me
		}
		System.out.printf("Connect to Redis %s:%d\n", host, REDIS_PORT);
		Jedis jedis = new Jedis(host, REDIS_PORT, REDIS_CONNECTION_TIMEOUT, REDIS_TIMEOUT);
		return jedis;
	}
	
	public static void closeJedis(Jedis jedis) {
		jedis.close();
	}
}
