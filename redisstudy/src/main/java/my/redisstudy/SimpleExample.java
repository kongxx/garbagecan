package my.redisstudy;

import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleExample {
	private static final String REDIS_HOST = "192.168.0.88";

	public static void main(String[] args) throws Exception {
//		testKeyValue();
//		testList();
//		testMap();
	}

	private static void testKeyValue() throws Exception {
		System.out.println("========================= test key value =========================");
		Jedis jedis = new Jedis(REDIS_HOST);

		jedis.set("foo", "bar");
		System.out.println(jedis.get("foo"));
		jedis.del("foo");
		System.out.println(jedis.get("foo"));

		jedis.setex("foo", 5, "bar");
		System.out.println(jedis.get("foo"));
		System.out.println(jedis.ttl("foo"));
		Thread.sleep(5 * 1000);
		System.out.println(jedis.get("foo"));
		jedis.disconnect();
	}

	private static void testList() {
		System.out.println("========================= test list =========================");
		Jedis jedis = new Jedis(REDIS_HOST);

		jedis.del("mykey1");
		jedis.lpush("mykey1", "value11");
		jedis.lpush("mykey1", "value12");
		jedis.lpush("mykey1", "value13", "value14", "value15");
		System.out.println("list length: " + jedis.llen("mykey1"));
		System.out.println(jedis.lrange("mykey1", 0, -1));

		jedis.lpush("mykey2", "value20");
		System.out.println(jedis.lpop("mykey2"));

		jedis.disconnect();
	}

	private static void testMap() {
		System.out.println("========================= test map =========================");
		Jedis jedis = new Jedis(REDIS_HOST);

		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		jedis.hmset("map", map);
		System.out.println(jedis.hkeys("map"));
		System.out.println(jedis.hgetAll("map"));
		System.out.println(jedis.hget("map", "key1"));

		Iterator<String> it = jedis.hkeys("map").iterator();
		while (it.hasNext()){
			String key = it.next();
			System.out.println(key + ": " + jedis.hget("map", key));
		}

		jedis.disconnect();
	}

	private static void testMap2() {
		System.out.println("========================= test map2 =========================");
		Jedis jedis = new Jedis(REDIS_HOST);

		Map<String, Map<String, String>> map = new LinkedHashMap<String,Map<String, String>>();
		Map<String,String> map1 = new LinkedHashMap<String,String>();
		map1.put("key11", "value11");
		map1.put("key12", "value12");
		Map<String,String> map2 = new LinkedHashMap<String,String>();
		map1.put("key21", "value21");
		map1.put("key22", "value22");
		map.put("map1", map1);
		map.put("map2", map2);

//		jedis.hmset("map", map);
//		System.out.println(jedis.hkeys("map"));
//		System.out.println(jedis.hgetAll("map"));
//		System.out.println(jedis.hget("map", "key1"));
//
//		Iterator<String> it = jedis.hkeys("map").iterator();
//		while (it.hasNext()){
//			String key = it.next();
//			System.out.println(key + ": " + jedis.hget("map", key));
//		}

		jedis.disconnect();
	}
}
