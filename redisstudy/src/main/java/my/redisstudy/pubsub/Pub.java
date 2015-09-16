package my.redisstudy.pubsub;

import redis.clients.jedis.Jedis;

public class Pub {
	private static final String REDIS_HOST = "192.168.0.88";

	public static void main(String[] args) throws Exception {
		Jedis jedis = new Jedis(REDIS_HOST);
		int idx = 0;
		while(true) {
			jedis.publish("mychannel", "wahaha_" + idx++);
			Thread.sleep(1000);

		}
//		jedis.disconnect();
	}
}
