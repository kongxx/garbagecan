package my.ignitestudy;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Arrays;

public class TestDataGrid {
	public static void main( String[] args ) {
		test();
	}

	public static void test() {
		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		ipFinder.setAddresses(Arrays.asList("192.168.0.192:47500..47509", "192.168.0.193:47500..47509"));
		spi.setIpFinder(ipFinder);
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setDiscoverySpi(spi);
		cfg.setClientMode(true);

		Ignite ignite = Ignition.start(cfg);
		IgniteCache<String, String> cache = ignite.getOrCreateCache("myCache");

		for (int i = 0; i < 10; i++) {
			cache.put("mykey_" + i, "myvalue_" + i);
		}

		for (int i = 0; i < 10; i++) {
			String key = "mykey_" + i;
			System.out.println("Got [key=" + key + ", val=" + cache.get(key) + ']');
		}
	}
}