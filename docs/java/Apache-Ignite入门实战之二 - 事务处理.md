Apache-Ignite入门实战之二 - 事务处理

前一篇文章介绍了怎样安装和使用 Ignite 的缓存。今天说说 Ignite 的缓存事务。

在我们平时的开发中经常会有这么一种场景，两个或多个线程同时在操作一个缓存的数据，此时我们希望要么这一批操作都成功，要么都失败。这种场景在数关系型据库中很常见，就是通过数据库的事务处理来实现的。下面我们就看看 Ignite 怎样实现这种事务处理。

下面先看一个测试程序。

``` java
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
```

- 测试程序中使用两个线程同时操作一块缓存。并且第二个线程稍晚运行，目的是为了等待第一个线程先把数据修改了，这是为了比较容易测试我们的程序。
- 要使用 Ignite 事务，需要将原子模式配置成 “CacheAtomicityMode.TRANSACTIONAL”，此配置也可以子啊配置文件里指定。
  ``` java
  cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
 ```
- 通过使用 Ignition.ignite().transactions().txStart(TransactionConcurrency, TransactionIsolation) 来创建事务。其中事务并发模式 TransactionConcurrency 可以是 OPTIMISTIC 或 PESSIMISTIC。 事务级别 TransactionIsolation 可以是 READ-COMMITTED，REPEATABLE_READ 和 SERIALIZABLE。
- 在我们开发使用事务的场景下，我们可以通过调整事务并发模式和事务级别参数来满足我们不同业务的需要。
- 事务最后需要使用 commit() 来提交修改，或通过 rollback() 来回滚修改。

运行测试程序，可以看到第一个线程修改了缓存，但是并没有提交修改，而是等到第二个线程提交修改后，此时第一个线程就会抛出异常，rollback修改。


