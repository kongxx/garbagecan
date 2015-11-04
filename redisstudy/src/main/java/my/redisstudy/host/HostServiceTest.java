package my.redisstudy.host;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class HostServiceTest {
	private static final String REDIS_HOST = "localhost";
	private static final int REDIS_PORT = 6379;
	private static final int REDIS_CONNECTION_TIMEOUT = 600000;
	private static final int REDIS_TIMEOUT = 600000;
	
	private static final String HOSTS_KEY = "hosts";
	
	public static void main(String[] args) {
//		set();
		query();
	}
	
	public static void set() {
		Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, REDIS_CONNECTION_TIMEOUT, REDIS_TIMEOUT);

		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			String host = "host_" + i;
			Map<String, String> map = new LinkedHashMap<String, String>();
			for (int j = 0; j < 100; j++) {
				map.put(host + "|attr_" + j, "value_" + System.currentTimeMillis());
			}
			jedis.hmset(HOSTS_KEY, map);
		}
		
//		Iterator<String> it = jedis.hkeys(HOSTS_KEY).iterator();
//		while (it.hasNext()) {
//			String key = it.next();
//			System.out.println(key + ": " + jedis.hget(HOSTS_KEY, key));
//		}
		
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		jedis.disconnect();
		jedis.close();
	}

	public static void query() {
		Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT, REDIS_CONNECTION_TIMEOUT, REDIS_TIMEOUT);
		
		long start = System.currentTimeMillis();
		ScanParams scanParams = new ScanParams();
		scanParams.count(1000000000);
		scanParams.match("host_1*|*");
		ScanResult<Entry<String, String>> scanResult = jedis.hscan(HOSTS_KEY, String.valueOf(0), scanParams);
		List<Entry<String, String>> entries = scanResult.getResult();
//		for (Entry<String, String> entry : entries) {
//			//System.out.println(entry.getKey() + ": " + entry.getValue());
//		}
		System.out.println(entries.size());
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
		jedis.disconnect();
		jedis.close();
	}
}
