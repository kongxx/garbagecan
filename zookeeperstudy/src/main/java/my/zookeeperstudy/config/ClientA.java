package my.zookeeperstudy.config;

public class ClientA {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("localhost:2181");

		while (true) {
			Thread.sleep(1000);
			config.get("mykey");
		}
	}
}
