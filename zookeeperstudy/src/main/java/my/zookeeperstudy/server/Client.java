package my.zookeeperstudy.server;

import org.apache.zookeeper.*;

import java.util.List;

public class Client {
	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("fanbinx1:2181,fanbinx2:2181,fanbinx3:2181", 10000,
				new Watcher() {
					public void process(WatchedEvent event) {
						System.out.println("event: " + event.getType());
					}
				});

		System.out.println(zk.getState());
//
//		zk.create("/myApps", "myAppsData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		zk.create("/myApps/App1", "App1Data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		zk.create("/myApps/App2", "App2Data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		zk.create("/myApps/App3", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		zk.setData("/myApps/App3","App3Data".getBytes(), -1);
//
//		System.out.println(zk.exists("/myApps", true));
//		System.out.println(new String(zk.getData("/myApps", true, null)));

		List<String> children = zk.getChildren("/mycluster", true);
		for (String child : children) {
			System.out.println(new String(zk.getData("/mycluster/" + child, true, null)));
			zk.delete("/mycluster/" + child,-1);
		}

//		zk.delete("/myApps",-1);

		zk.close();
	}
}
