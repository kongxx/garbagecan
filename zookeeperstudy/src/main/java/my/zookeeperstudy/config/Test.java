package my.zookeeperstudy.config;

public class Test {

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.connect("9.111.254.55:2181");

		System.out.println("mykey: " + config.getConfig("mykey"));

		config.setConfig("mykey1", "myvalue1");
		config.setConfig("mykey2", "myvalue2");
		System.out.println("mykey1: " + config.getConfig("mykey1"));
		System.out.println("mykey2: " + config.getConfig("mykey2"));

		config.setConfig("mykey1", "myvalue11");
		config.setConfig("mykey2", "myvalue22");
		System.out.println("mykey1: " + config.getConfig("mykey1"));
		System.out.println("mykey2: " + config.getConfig("mykey2"));

		config.clear();
	}

}
