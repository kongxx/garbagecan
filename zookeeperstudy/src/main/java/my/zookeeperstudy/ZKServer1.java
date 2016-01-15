package my.zookeeperstudy;

public class ZKServer1 {

	public static void main(String[] args) throws Exception {
		ZKServer zkServer = new ZKServer("1", "/tmp/zookeeper1/data", "2181");
		zkServer.startServer();
	}

}
