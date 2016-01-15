package my.zookeeperstudy.lock;

public class ZKClient1 {
	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient();
		zkClient.start("localhost:2181");
	}
}
