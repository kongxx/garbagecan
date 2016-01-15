package my.zookeeperstudy.election;

import org.apache.zookeeper.*;

public class ZKClient {
	protected String id = null;
	protected String clientPort = null;
	protected String path = "/myapp_leader";

	public ZKClient(String id, String clientPort) {
		this.id = id;
		this.clientPort = clientPort;
	}

	protected void startClient() throws Exception {
		ZooKeeper zk = new ZooKeeper("localhost:" + clientPort, 10000,
				new Watcher() {
					public void process(WatchedEvent event) {
						System.out.println("event: " + event.getType());
					}
				});

		while (true) {
			byte[] leader = null;
			try {
				leader = zk.getData(path, true, null);
			} catch(Exception e) {
				System.out.println("The leader is null.");
			}
			if (leader == null) {
				try {
					zk.create(path, this.id.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				} catch(Exception e) {
					// ignore me
				}
			} else {
				System.out.println("The leader is: " + new String(leader));
			}
			Thread.sleep(1 * 1000);
		}
	}
}
