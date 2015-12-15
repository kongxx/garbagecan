//package my.redisstudy.host;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import my.redisstudy.Executor;
//import my.redisstudy.ExecutorUtils;
//import my.redisstudy.utils.JedisUtils;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Pipeline;
//import redis.clients.jedis.ScanParams;
//import redis.clients.jedis.ScanResult;
//import redis.clients.jedis.Transaction;
//
//public class HostService1 {
//	
//	private int hostNum;
//	private int propNum;
//	
//	public static void main(String[] args) {
//		HostService1 service = new HostService1(10000, 100);
//		service.testSet();
//		service.testGet();
//		service.testGetWithPipeline();
//	}
//	
//	public HostService1(int hostNum, int propNum) {
//		this.hostNum = hostNum;
//		this.propNum = propNum;
//	}
//	
//	public void testSet() {
//		final Jedis jedis = JedisUtils.getJedis();
//		ExecutorUtils.execute("testSet", new Executor() {
//			public void execute() {
//				for (int i = 0; i < hostNum; i++) {
//					Transaction tx = jedis.multi();
//					String host = "host_" + i;
//					Map<String, String> map = new LinkedHashMap<String, String>();
//					for (int j = 0; j < propNum; j++) {
//						map.put("attr_" + j, "value_" + System.currentTimeMillis());
//					}
//					tx.hmset(host, map);
//					tx.sadd("hosts", host);
//					tx.exec();
//				}
//			}
//		});
//		JedisUtils.closeJedis(jedis);
//	}
//
//	public void testGet() {
//		final Jedis jedis = JedisUtils.getJedis();
//		ExecutorUtils.execute("testGet", new Executor() {
//			public void execute() {
//				ScanParams scanParams = new ScanParams();
//				scanParams.count(1000000000);
//				ScanResult<String> scanResult = jedis.sscan("hosts", String.valueOf(0), scanParams);
//				List<String> hosts = scanResult.getResult();
//				System.out.println(hosts.size());
//				for (String host : hosts) {
//					jedis.hgetAll(host);
////					System.out.println(jedis.hgetAll(host));
//				}
//			}
//		});
//		
//		JedisUtils.closeJedis(jedis);
//	}
//	
//	public void testGetWithPipeline() {
//		final Jedis jedis = JedisUtils.getJedis();
//		ExecutorUtils.execute("testGetWithPipeline", new Executor() {
//			public void execute() {
//				ScanParams scanParams = new ScanParams();
//				scanParams.count(100000000);
//				ScanResult<String> scanResult = jedis.sscan("hosts", String.valueOf(0), scanParams);
//				List<String> hosts = scanResult.getResult();
//				System.out.println(hosts.size());
//				
//				Pipeline pipeline = jedis.pipelined();
//				
//				for (String host : hosts) {
//					pipeline.hgetAll(host);
//				}
//				
//				List<Object> results = pipeline.syncAndReturnAll();
//				for (Object obj : results) {
//					if (obj instanceof Map) {
//						Map<Object, Object> map = (Map<Object, Object>) obj;
////						System.out.println(map);
//					}
//				}
//			}
//		});
//
//		
//		JedisUtils.closeJedis(jedis);
//	}
//}
