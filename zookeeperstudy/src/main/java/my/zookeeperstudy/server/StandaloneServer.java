package my.zookeeperstudy.server;

import org.apache.zookeeper.*;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;

public class StandaloneServer {

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					runServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		Thread.sleep(5 * 1000);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					runClient();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private static void runServer() throws Exception {
		int tickTime = 2000;
		int maxClientCnxns = 60;

		File dir = new File(System.getProperty("java.io.tmpdir"), "zookeeper").getAbsoluteFile();

		ZooKeeperServer zkServer = new ZooKeeperServer(dir, dir, tickTime);
		ServerCnxnFactory standaloneServerFactory = ServerCnxnFactory.createFactory(new InetSocketAddress(2181), maxClientCnxns);
		standaloneServerFactory.startup(zkServer);
		//standaloneServerFactory.shutdown();
	}

	public static void runClient() throws Exception {
		ZooKeeper zk = new ZooKeeper("localhost:2181", 10000,
				new Watcher() {
					public void process(WatchedEvent event) {
						System.out.println("event: " + event.getType());
					}
				});

		System.out.println(zk.getState());

		zk.create("/myApps", "myAppsData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create("/myApps/App1", "App1Data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create("/myApps/App2", "App2Data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create("/myApps/App3", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.setData("/myApps/App3","App3Data".getBytes(), -1);

		System.out.println(zk.exists("/myApps", true));
		System.out.println(new String(zk.getData("/myApps", true, null)));

		List<String> children = zk.getChildren("/myApps", true);
		for (String child : children) {
			System.out.println(new String(zk.getData("/myApps/" + child, true, null)));
			zk.delete("/myApps/" + child,-1);
		}

		zk.delete("/myApps",-1);

		zk.close();
	}
}
