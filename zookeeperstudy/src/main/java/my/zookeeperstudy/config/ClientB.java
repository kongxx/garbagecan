package my.zookeeperstudy.config;

public class ClientB {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("localhost:2181");

		for (int i = 0; i < 10000; i++) {
			config.set("mykey", "myvalue_" + i);
			Thread.sleep(5 * 1000);
		}

		config.clear();
	}
}
