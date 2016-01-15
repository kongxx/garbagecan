package my.zookeeperstudy.election;


public class ZKServer1 extends ZKServer {

	public static void main(String[] args) throws Exception {
		ZKServer1 zkServer = new ZKServer1("1", "/tmp/zookeeper1/data", "2181");
		zkServer.startServer();
		Thread.sleep(5 * 1000);
		zkServer.startClient();
	}

	public ZKServer1(String id, String dataDir, String clientPort) {
		this.id = id;
		this.dataDir = dataDir;
		this.clientPort = clientPort;
	}

}
