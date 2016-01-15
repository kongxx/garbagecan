package my.zookeeperstudy.lock;

import org.apache.zookeeper.*;

public class ZKClient {
	private final String lockBasePath = "/mylocks";
	private final String lockName = "mylock";

	public void start(String url) throws Exception {
		final ZooKeeper zk = new ZooKeeper(url, 10000, new Watcher() {
			public void process(WatchedEvent event) {
				System.out.println("event: " + event.getType());
			}
		});

		try {
			DistributedLocker locker = new DistributedLocker(zk, lockBasePath, lockName);

			System.out.println("before get lock");
			locker.getLock();
			System.out.println("after get lock");

			System.out.println("do something");
			Thread.sleep(60 * 1000);

			System.out.println("before release lock");
			locker.releaseLock();
			System.out.println("after release lock");
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}