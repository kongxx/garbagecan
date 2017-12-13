package my.ignitestudy.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.AtomicConfiguration;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;

import java.util.Arrays;

public class TransactionExample {
	public static void main( String[] args ) throws Exception {
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

	private static void TestTransaction() throws Exception {
		final Ignite ignite = getIgnite();
		CacheConfiguration cacheCfg = new CacheConfiguration();
		cacheCfg.setName("default");
		cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		cacheCfg.setBackups(1);
		final IgniteCache<String, String> cache = ignite.getOrCreateCache(cacheCfg);

		cache.remove("MyKey");

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Transaction 1: begin");
				try (Transaction tx = Ignition.ignite().transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.SERIALIZABLE)) {
					String value = cache.get("MyKey");

					cache.put("MyKey", "MyValue 1");
					try {
						Thread.currentThread().sleep(5 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println("Transaction 1: before commit, " + cache.get("MyKey"));
					tx.commit();
					System.out.println("Transaction 1: after commit, " + cache.get("MyKey"));
				}

				System.out.println("Transaction 1: end");
			}
		}).start();

		Thread.currentThread().sleep(2 * 1000);

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Transaction 2: begin");
				try (Transaction tx = Ignition.ignite().transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.SERIALIZABLE)) {
					String value = cache.get("MyKey");

					cache.put("MyKey", "MyValue 2");

					System.out.println("Transaction 2: before commit, " + cache.get("MyKey"));
					tx.commit();
					System.out.println("Transaction 2: after commit, " + cache.get("MyKey"));
				}
				System.out.println("Transaction 2: end");
			}
		}).start();
	}
}