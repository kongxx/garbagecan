package my.zookeeperstudy.election;

public class ZKClient3 {

	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient("3", "2183");
		zkClient.startClient();
	}

}
