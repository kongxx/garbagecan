package my.redisstudy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.List;

public class PipelineExample {
	private static final String REDIS_HOST = "192.168.0.88";

	public static void main(String[] args) throws Exception {
		testPipeline();
	}

	public static void testPipeline() throws Exception {
		Jedis jedis = new Jedis(REDIS_HOST);
		Pipeline pipeline = jedis.pipelined();
		for (int i = 0; i < 10000; i++) {
			pipeline.set("key_" + i, "value_" + i);
		}
		List<Object> results = pipeline.syncAndReturnAll();
		System.out.println(results);
		jedis.disconnect();
	}

}
