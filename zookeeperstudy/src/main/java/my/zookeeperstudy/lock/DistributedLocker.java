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

	public void getLock() throws KeeperException, InterruptedException {
		if (zk.exists(lockBasePath, true) == null) {
			zk.create(lockBasePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		lockPath = zk.create(lockBasePath + "/" + lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("getLock path: " + lockPath);
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
				System.out.println(nodes);
				if (lockPath.endsWith(nodes.get(0))) {
					return;
				} else {
					lock.wait();
				}
			}
		}
	}

	public void releaseLock() throws KeeperException, InterruptedException {
		zk.delete(lockPath, -1);
		lockPath = null;
	}
}