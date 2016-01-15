package my.zookeeperstudy.lock;

public class ZKClient2 {
	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient();
		zkClient.start("localhost:2182");
	}
}
