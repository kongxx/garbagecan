package my.zookeeperstudy.server;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClusteredZKServer {

	public static void main(String[] args) throws Exception {
		InputStream is = ClusteredZKServer.class.getResourceAsStream("/my/zookeeperstudy/server/zoo.cfg");
		Properties props = new Properties();
		try {
			props.load(is);
		} finally {
			is.close();
		}

		for (String key : props.stringPropertyNames()) {
			Pattern pKey = Pattern.compile("^server\\.(\\d)");
			Pattern pValue = Pattern.compile("([\\w|.]*):\\d*:\\d*");
			Matcher mKey = pKey.matcher(key);
			Matcher mValue = pValue.matcher(props.getProperty(key));
			if (mKey.find() && mValue.find()) {
				String id = mKey.group(1);
				String host = mValue.group(1);
				String thisHostName = InetAddress.getLocalHost().getHostName();
				String thisHostAddress = InetAddress.getLocalHost().getHostAddress();
				if (host.equals(thisHostName) || host.equals(thisHostAddress)) {
					//System.out.println(new File(props.getProperty("dataDir"), "myid").getAbsolutePath());
					FileUtils.write(new File(props.getProperty("dataDir"), "myid"), id);
					QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
					quorumConfig.parseProperties(props);

					final ZooKeeperServerMain zkServer = new ZooKeeperServerMain();
					final ServerConfig config = new ServerConfig();
					config.readFrom(quorumConfig);
					zkServer.runFromConfig(config);
				}
			}
		}
	}
}
