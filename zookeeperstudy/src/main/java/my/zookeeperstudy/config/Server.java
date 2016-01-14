package my.zookeeperstudy.config;

import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.File;
import java.net.InetSocketAddress;

public class Server {

	public static void main(String[] args) throws Exception {
		int tickTime = 2000;
		int maxClientCnxns = 60;
		File dir = new File(System.getProperty("java.io.tmpdir"), "zookeeper").getAbsoluteFile();
		ZooKeeperServer zkServer = new ZooKeeperServer(dir, dir, tickTime);
		ServerCnxnFactory standaloneServerFactory = ServerCnxnFactory.createFactory(new InetSocketAddress(2181), maxClientCnxns);
		standaloneServerFactory.startup(zkServer);
	}

}
