package my.zookeeperstudy.config;

public class ClientA {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("9.111.254.55:2181");

		while (true) {
			config.displayConfig();
			Thread.sleep(5 * 1000);
		}

	}
}
