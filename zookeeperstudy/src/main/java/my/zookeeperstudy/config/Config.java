package my.zookeeperstudy.config;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Config implements Watcher {

	private ZooKeeper zk;

	private String basePath = "/config";

	public Config() {

	}

	public void connect(String hosts) throws IOException, KeeperException, InterruptedException {
		this.zk = new ZooKeeper(hosts, 1000, this);
		if (zk.exists(basePath, this) == null) {
			zk.create(basePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
	}

	public void set(String key, String value) throws InterruptedException, KeeperException {
		String path = basePath + "/" + key;
		if (zk.exists(path, this) == null) {
			zk.create(path, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} else {
			zk.setData(path, value.getBytes(), -1);
		}
	}

	public String get(String key) throws InterruptedException, KeeperException {
		String path = basePath + "/" + key;
		if (zk.exists(path, this) != null) {
			byte[] data = zk.getData(path, this, null);
			return new String(data);
		}
		return null;
	}

	public void printConfig() throws InterruptedException, KeeperException {
		System.out.println("----- begin -----");
		List<String> children = zk.getChildren(basePath, false);
		Collections.sort(children);
		for (String child : children) {
			System.out.println(child + ": " + get(child));
		}
		System.out.println("----- end -----");
	}

	public void clear() throws KeeperException, InterruptedException {
		if (zk.exists(basePath, false) != null) {
			List<String> children = zk.getChildren(basePath, false);
			for (String child : children) {
				zk.delete(basePath + "/" + child, -1);
				System.out.println("Deleted " + basePath + "/" + child);
			}
			zk.delete(basePath, -1);
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("event: " + event);
		if (event.getType() == Event.EventType.NodeDataChanged ||
				event.getType() == Event.EventType.NodeChildrenChanged) {
			try {
				printConfig();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (KeeperException e) {
				e.printStackTrace();
			}
		}
	}
}
