package my.zookeeperstudy.config;

public class ClientB {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("9.111.254.59:2181");

		for (int i = 0; i < 5; i++) {
			config.setConfig("mykey1", "myvalue_" + i);
			config.setConfig("mykey2", "myvalue_" + i);

			Thread.sleep(5 * 1000);
		}

		config.clear();
	}
}
