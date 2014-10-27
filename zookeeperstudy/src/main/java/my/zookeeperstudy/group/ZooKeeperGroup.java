package my.zookeeperstudy.group;

import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ZooKeeperGroup {

	private final ZooKeeper zk;
	private Semaphore semaphore = new Semaphore(1);

	public ZooKeeperGroup(ZooKeeper zk) {
		this.zk = zk;
	}

	public void createGroup(String groupName) throws KeeperException, InterruptedException {
		String path = "/" + groupName;
		if (zk.exists(path, false) == null) {
			String createdPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("Created " + createdPath);
		}
	}

	public void deleteGroup(String groupName) throws KeeperException, InterruptedException {
		String path = "/" + groupName;
		if (zk.exists(path, false) != null) {
			List<String> children = zk.getChildren(path, false);
			for (String child : children) {
				System.out.println("Deleted " + path + "/" + child);
				zk.delete(path + "/" + child, -1);
			}
			zk.delete(path, -1);
			System.out.printf("Deleted group %s at path %s\n", groupName, path);
		}
	}

	public void joinGroup(String groupName, String memberName) throws KeeperException, InterruptedException {
		String path = "/" + groupName + "/" + memberName;
		String createdPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Created " + createdPath);
	}

	public void listGroup(String groupName) throws KeeperException, InterruptedException {
		String path = "/" + groupName;

		while (true) {
			semaphore.acquire();
			List<String> children = zk.getChildren(path, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getType() == Event.EventType.NodeChildrenChanged) {
						semaphore.release();
					}
				}
			});

			Collections.sort(children);
			for (String child : children) {
				System.out.println(child);
			}
		}
	}
}
