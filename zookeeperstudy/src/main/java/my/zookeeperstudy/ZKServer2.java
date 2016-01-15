package my.zookeeperstudy;

public class ZKServer2 {

	public static void main(String[] args) throws Exception {
		ZKServer zkServer = new ZKServer("2", "/tmp/zookeeper2/data", "2182");
		zkServer.startServer();
	}

}
