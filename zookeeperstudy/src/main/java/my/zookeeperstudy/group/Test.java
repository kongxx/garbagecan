package my.zookeeperstudy.group;

import org.apache.zookeeper.*;

import java.io.IOException;

public class Test {
	private static final String host1 = "localhost:2181";
	private static final String host2 = "localhost:2182";
	private static final String host3 = "localhost:2183";

	public static void main(String[] args) throws Exception {
		final ZooKeeper zk = new ZooKeeper(host1, 10000, null);

		ZooKeeperGroup group = new ZooKeeperGroup(zk);
		group.createGroup("myGroups");

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ZooKeeper zk = new ZooKeeper(host2, 10000, null);
					ZooKeeperGroup group = new ZooKeeperGroup(zk);
					group.listGroup("myGroups");
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ZooKeeper zk = new ZooKeeper(host3, 10000, null);
					ZooKeeperGroup group = new ZooKeeperGroup(zk);
					for (int i = 0; i < 3; i++) {
						Thread.sleep(i * 1000);
						group.joinGroup("myGroups", "member_" + i);
					}
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Thread.sleep(10 * 1000);
		group.deleteGroup("myGroups");
	}
}
