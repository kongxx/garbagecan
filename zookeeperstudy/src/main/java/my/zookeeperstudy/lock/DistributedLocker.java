package my.zookeeperstudy.lock;

import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;

public class DistributedLocker {
	private final ZooKeeper zk;
	private final String lockBasePath;
	private final String lockName;
	private String lockPath;

	public DistributedLocker(ZooKeeper zk, String lockBasePath, String lockName) {
		this.zk = zk;
		this.lockBasePath = lockBasePath;
		this.lockName = lockName;
	}

	public void lock() throws KeeperException, InterruptedException {
		lockPath = zk.create(lockBasePath + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("lock path: " + lockPath);
		final Object lock = new Object();
		synchronized (lock) {
			while (true) {
				List<String> nodes = zk.getChildren(lockBasePath, new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						synchronized (lock) {
							lock.notifyAll();
						}
					}
				});
				Collections.sort(nodes);

				if (lockPath.endsWith(nodes.get(0))) {
					return;
				} else {
					lock.wait();
				}
			}
		}
	}

	public void unlock() throws KeeperException, InterruptedException {
		zk.delete(lockPath, -1);
		lockPath = null;
	}
}