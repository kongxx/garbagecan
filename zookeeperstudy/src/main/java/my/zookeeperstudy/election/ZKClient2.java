package my.zookeeperstudy.election;

public class ZKClient2 {

	public static void main(String[] args) throws Exception {
		ZKClient zkClient = new ZKClient("2", "2182");
		zkClient.startClient();
	}

}
