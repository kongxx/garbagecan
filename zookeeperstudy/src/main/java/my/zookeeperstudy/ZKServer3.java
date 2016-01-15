package my.zookeeperstudy;

public class ZKServer3 {

	public static void main(String[] args) throws Exception {
		ZKServer zkServer = new ZKServer("3", "/tmp/zookeeper3/data", "2183");
		zkServer.startServer();
	}

}
