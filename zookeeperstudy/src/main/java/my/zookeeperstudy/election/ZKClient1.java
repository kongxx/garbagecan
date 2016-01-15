package my.zookeeperstudy.election;

public class ZKClient1 {

	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient("1", "2181");
		zkClient.startClient();
	}

}
