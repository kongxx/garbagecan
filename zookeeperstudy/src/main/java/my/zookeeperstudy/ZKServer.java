package my.zookeeperstudy;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

import java.io.File;
import java.net.InetAddress;
import java.util.Properties;

public class ZKServer {
	protected String id = null;
	protected String dataDir = null;
	protected String clientPort = null;

	public ZKServer(String id, String dataDir, String clientPort) {
		this.id = id;
		this.dataDir = dataDir;
		this.clientPort = clientPort;
	}

	public void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Properties props = new Properties();
					props.setProperty("tickTime", "2000");
					props.setProperty("dataDir", dataDir);
					FileUtils.write(new File(props.getProperty("dataDir"), "myid"), id);
					props.setProperty("clientPort", clientPort);
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
