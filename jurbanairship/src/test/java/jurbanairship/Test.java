package jurbanairship;

import jurbanairship.device.Android;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) throws Exception {
		testRegister();
	}

	private static void testRegister() throws Exception {
		Register register = new Register();
		boolean bln = false;

		Android android = new Android();
		android.setId("31ac6492-195a-49b5-8438-0da0f44a4fc9");
		bln = register.register(android);
		logger.info("{}", bln);

//		Apple apple = new Apple();
//		apple.setId("FE66489F304DC75B8D6E8200DFF8A456E8DAEACEC428B427E9518741C92C6660");
//		apple.setAlias("abc");
//		List<String> tags = new ArrayList<String>();
//		tags.add("mytag");
//		apple.setTags(tags);
//		apple.setBadge(2);
//		apple.setQuiettime(new Apple.QuietTime("22:00", "08:00"));
//		apple.setTz("America/Los_Angeles");
//		bln = register.register(apple);
//		logger.info("{}", bln);
	}
}
