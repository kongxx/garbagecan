package my.redisstudy.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Sub {
	private static final String REDIS_HOST = "192.168.0.88";

	public static void main(String[] args) throws Exception {
		Jedis jedis = new Jedis(REDIS_HOST);

		jedis.subscribe(new JedisPubSub() {
			public void onMessage(String channel, String message) {
				System.out.println(channel + ": " + message);
			}

			public void onSubscribe(String channel, int subscribedChannels) {
			}

			public void onUnsubscribe(String channel, int subscribedChannels) {
			}

			public void onPSubscribe(String pattern, int subscribedChannels) {
			}

			public void onPUnsubscribe(String pattern, int subscribedChannels) {
			}

			public void onPMessage(String pattern, String channel, String message) {
			}
		}, "mychannel");
	}
}
