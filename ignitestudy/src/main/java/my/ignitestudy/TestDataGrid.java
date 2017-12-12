package my.ignitestudy;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.transactions.Transaction;

import java.util.Arrays;

public class TestDataGrid {
	public static void main( String[] args ) {
//		testGetPut();
//		testAtomOperation();
		TestTransaction();
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

	private static void testGetPut() {
		IgniteCache<String, String> cache = getIgnite().getOrCreateCache("myCache");

		for (int i = 0; i < 10; i++) {
			cache.put("mykey_" + i, "myvalue_" + i);
		}

		for (int i = 0; i < 10; i++) {
			String key = "mykey_" + i;
			System.out.println("Got [key=" + key + ", val=" + cache.get(key) + ']');
		}
	}

	private static void testAtomOperation() {
		IgniteCache<String, Integer> cache = getIgnite().getOrCreateCache("myCache");

		Integer oldValue = cache.getAndPutIfAbsent("Hello", 11);
		System.out.println("Hello: " + oldValue);

		boolean success = cache.putIfAbsent("World", 22);
		System.out.println("World: " + success);

		oldValue = cache.getAndReplace("Hello", 11);
		System.out.println("Hello replace: " + oldValue);

		success = cache.replace("World", 22);
		System.out.println("World replace: " + success);

		success = cache.replace("World", 2, 22);
		System.out.println("World replace: " + success);

		success = cache.remove("Hello", 1);
		System.out.println("World remove: " + success);
	}

	private static void TestTransaction() {
		final Ignite ignite = getIgnite();
		final IgniteCache<String, Integer> cache = getIgnite().getOrCreateCache("myCache");

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Begin Transaction");
				try (Transaction tx = ignite.transactions().txStart()) {
					Integer hello = cache.get("MyKey");

					if (hello == 1) {
						cache.put("Hello", 11);
					}

					cache.put("World", 22);

					tx.commit();
				}
				System.out.println("End Transaction");
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {

			}
		}).start();


	}

}