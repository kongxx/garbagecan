Apache-Ignite入门实战之三 - 分布式锁

在 Ignite 的分布式缓存中还有一种常见应用场景是分布式锁，利用分布式锁我们可以实现简单的集群master选举功能。

下面是一个使用分布式锁的例子：

``` java
package my.ignitestudy.datagrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class DistributedLockExample {

	public static void main( String[] args ) throws Exception {
		test();
	}

	private static Ignite getIgnite() {
		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
		ipFinder.setAddresses(Arrays.asList("192.168.0.192:47500..47509"));
		spi.setIpFinder(ipFinder);

		IgniteConfiguration igniteCfg = new IgniteConfiguration();
		igniteCfg.setDiscoverySpi(spi);
		igniteCfg.setClientMode(true);

		Ignite ignite = Ignition.start(igniteCfg);
		return ignite;
	}

	private static void test() throws Exception {
		CacheConfiguration cacheCfg = new CacheConfiguration();
		cacheCfg.setName("default");
		cacheCfg.setBackups(1);
		IgniteCache<String, String> cache = getIgnite().getOrCreateCache(cacheCfg);

		String mylock = "mylock";

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Thread 1: before lock");
				Lock lock = cache.lock(mylock);
				lock.lock();
				System.out.println("Thread 1: after lock");

				try {
					cache.put(mylock, "Thread 1");
					Thread.currentThread().sleep(10 * 1000);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					System.out.println("Thread 1: before unlock");
					lock.unlock();
					System.out.println("Thread 1: after unlock");
				}
			}
		}).start();

		Thread.currentThread().sleep(2 * 1000);

		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Thread 2: before lock");
				Lock lock = cache.lock(mylock);
				lock.lock();
				System.out.println("Thread 2: after lock");

				try {
					cache.put(mylock, "Thread 2");
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					System.out.println("Thread 2: before unlock");
					lock.unlock();
					System.out.println("Thread 2: after unlock");
				}
			}
		}).start();
	}

}
```

- 例子中使用两个线程来模拟抢占锁的场景。
- 为了测试方便，第一个线程先启动，在获取锁后先sleep一会，等待第二个线程启动。
- 第二个线程启动后也试着去获取锁，此时由于第一个线程已经获取了锁，所以第二个线程会等待。
- 第一个线程sleep一会后，把锁释放，这时第二个线程就会立即获取锁，然后执行自己的逻辑。
