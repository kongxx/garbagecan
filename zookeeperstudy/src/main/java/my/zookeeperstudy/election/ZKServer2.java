package my.zookeeperstudy.election;

public class ZKServer2 extends ZKServer {

	public static void main(String[] args) throws Exception {
		ZKServer2 zkServer = new ZKServer2("2", "/tmp/zookeeper2/data", "2182");
		zkServer.startServer();
		Thread.sleep(5 * 1000);
		zkServer.startClient();
	}

	public ZKServer2(String id, String dataDir, String clientPort) {
		this.id = id;
		this.dataDir = dataDir;
		this.clientPort = clientPort;
	}
}
