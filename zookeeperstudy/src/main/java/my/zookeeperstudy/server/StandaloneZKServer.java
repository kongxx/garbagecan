package my.zookeeperstudy.server;

import org.apache.zookeeper.*;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class StandaloneZKServer {

	public static void main(String[] args) throws Exception {
		new Thread() {
			public void run() {
				try {
					runServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		Thread.sleep(5 * 1000);

		runClient();
	}

	private static void runServer() throws Exception {
		Properties props = new Properties();
		props.setProperty("tickTime", "2000");
		props.setProperty("dataDir", new File(System.getProperty("java.io.tmpdir"), "zookeeper").getAbsolutePath());
		props.setProperty("clientPort", "2181");
		props.setProperty("initLimit", "10");
		props.setProperty("syncLimit", "5");

		QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
		try {
			quorumConfig.parseProperties(props);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}

		final ZooKeeperServerMain zkServer = new ZooKeeperServerMain();
		final ServerConfig config = new ServerConfig();
		config.readFrom(quorumConfig);
		zkServer.runFromConfig(config);
	}

	private static void runClient() throws Exception {
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
