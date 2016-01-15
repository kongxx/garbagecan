package my.zookeeperstudy;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

import java.io.File;
import java.net.InetAddress;
import java.util.Properties;

public class ZKServerCluster {
	public static void main(String[] args) throws Exception {
		startServer(1, 2181, "/tmp/zookeeper/data" + 1);
		startServer(2, 2182, "/tmp/zookeeper/data" + 2);
		startServer(3, 2183, "/tmp/zookeeper/data" + 3);
	}

	private static void startServer(final int id, final int clientPort, final String dataDir) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Properties props = new Properties();
					props.setProperty("tickTime", "2000");
					props.setProperty("dataDir", dataDir);
					FileUtils.write(new File(props.getProperty("dataDir"), "myid"), String.valueOf(id));
					props.setProperty("clientPort", String.valueOf(clientPort));
					props.setProperty("initLimit", "10");
					props.setProperty("syncLimit", "5");
					String hostname = InetAddress.getLocalHost().getHostName();
					props.setProperty("server.1", hostname + ":2881:3881");
					props.setProperty("server.2", hostname + ":2882:3882");
					props.setProperty("server.3", hostname + ":2883:3883");

					QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
					quorumConfig.parseProperties(props);

					QuorumPeerMain quorumPeerMain = new QuorumPeerMain();
					quorumPeerMain.runFromConfig(quorumConfig);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
