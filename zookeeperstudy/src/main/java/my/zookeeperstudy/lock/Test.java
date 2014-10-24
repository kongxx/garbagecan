package my.zookeeperstudy.lock;

import org.apache.zookeeper.*;

public class Test {
	public static void main(String[] args) throws Exception {
		final ZooKeeper zk = new ZooKeeper("9.111.254.55:2181", 10000, new Watcher() {
			public void process(WatchedEvent event) {
				//System.out.println("event: " + event.getType());
			}
		});

		if (zk.exists("/mylocks", false) == null) {
			zk.create("/mylocks", "mylocks".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DistributedLocker locker = new DistributedLocker(zk, "/mylocks", "mylock");

					System.out.println("Thread1 before lock");
					locker.lock();
					System.out.println("Thread1 after lock");

					System.out.println("Thread1 do something");
					Thread.sleep(10 * 1000);

					System.out.println("Thread1 before unlock");
					locker.unlock();
					System.out.println("Thread1 after unlock");
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DistributedLocker locker = new DistributedLocker(zk, "/mylocks", "mylock");

					System.out.println("Thread2 before lock");
					locker.lock();
					System.out.println("Thread2 after lock");

					System.out.println("Thread2 do something");
					Thread.sleep(5 * 1000);

					System.out.println("Thread2 before unlock");
					locker.unlock();
					System.out.println("Thread2 after unlock");
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

//		zk.close();
	}
}
