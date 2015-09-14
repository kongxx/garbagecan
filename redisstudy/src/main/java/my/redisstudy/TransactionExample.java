package my.redisstudy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

public class TransactionExample {
	private static final String REDIS_HOST = "192.168.0.88";

	public static void main(String[] args) throws Exception {
		Jedis jedis = new Jedis(REDIS_HOST);
		Transaction tx = jedis.multi();
		for (int i = 0; i < 10000; i++) {
			tx.set("key_" + i, "value_" + i);
		}
		List<Object> results = tx.exec();
		jedis.disconnect();
	}

}
