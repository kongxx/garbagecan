package my.zookeeperstudy.election;

public class ZKServer3 extends ZKServer {
	public static void main(String[] args) throws Exception {
		ZKServer3 zkServer = new ZKServer3("3", "/tmp/zookeeper3/data", "2183");
		zkServer.startServer();
		Thread.sleep(5 * 1000);
		zkServer.startClient();
	}

	public ZKServer3(String id, String dataDir, String clientPort) {
		this.id = id;
		this.dataDir = dataDir;
		this.clientPort = clientPort;
	}
}
