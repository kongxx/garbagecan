package my.ignitestudy.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.transactions.Transaction;

import java.util.Arrays;

public class TestDataGrid {
	public static void main( String[] args ) throws Exception {
		Ignite ignite = getIgnite();
		testGetPut(ignite);
		testAtomOperation(ignite);
	}

	private static Ignite getIgnite() {
		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		ipFinder.setAddresses(Arrays.asList("192.168.0.192:47500..47509"));
		spi.setIpFinder(ipFinder);
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setDiscoverySpi(spi);
		cfg.setClientMode(true);

		Ignite ignite = Ignition.start(cfg);
		return ignite;
	}

	private static void testGetPut(Ignite ignite) {
		IgniteCache<String, String> cache = ignite.getOrCreateCache("myCache");

		for (int i = 0; i < 10; i++) {
			cache.put("mykey_" + i, "myvalue_" + i);
		}

		for (int i = 0; i < 10; i++) {
			String key = "mykey_" + i;
			System.out.println("Got [key=" + key + ", val=" + cache.get(key) + ']');
		}
	}

	private static void testAtomOperation(Ignite ignite) {
		IgniteCache<String, Integer> cache = ignite.getOrCreateCache("myCache");

		Integer oldValue = cache.getAndPutIfAbsent("MyKey", 11);
		System.out.println("MyKey: " + oldValue);

		boolean success = cache.putIfAbsent("MyKey", 22);
		System.out.println("MyKey: " + success);

		oldValue = cache.getAndReplace("MyKey", 11);
		System.out.println("MyKey replace: " + oldValue);

		success = cache.replace("MyKey", 22);
		System.out.println("MyKey replace: " + success);

		success = cache.replace("MyKey", 2, 22);
		System.out.println("MyKey replace: " + success);

		success = cache.remove("MyKey", 1);
		System.out.println("MyKey remove: " + success);
	}
}