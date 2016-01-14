package my.zookeeperstudy.config;

public class ClientA {
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("localhost:2181");

		String lastValue = null;
		while (true) {
			Thread.sleep(1000);
			String value = config.getConfig("mykey");
			if (value == null || value.equals(lastValue)) {
				continue;
			} else {
				lastValue = value;
			}
		}
	}
}
