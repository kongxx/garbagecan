package jurbanairship;

import com.google.gson.*;
import jurbanairship.device.Android;
import jurbanairship.device.Blackberry;
import jurbanairship.notification.Notification;
import jurbanairship.notification.SimpleAndroidNotification;
import jurbanairship.notification.SimpleAppleNotification;
import jurbanairship.notification.SimpleBlackBerryNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {

	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) throws Exception {
//		testRegisterAPI();
		testPush();

//		JsonObject jsonObject = new JsonObject();
//		//jsonObject.addProperty("apids", new JsonArray());
//		JsonArray jsonArray = new JsonArray();
//		jsonArray.add(new JsonPrimitive("id"));
//		jsonObject.add("apids", jsonArray);
//		logger.info(jsonObject.toString());

//		//sb.append("{\"android\": {\"alert\": \""+alert+"\"}, \"apids\": [\""+apid+"\"]}");
//		SimpleAndroidNotification notification = new SimpleAndroidNotification();
//		notification.getApids().add("31ac6492-195a-49b5-8438-0da0f44a4fc9");
//		notification.getApids().add("31ac6492-195a-49b5-8438-0da0f44a4fc9-");
//		notification.getAliases().add("myAlias");
//		notification.getAliases().add("myAlias-");
//		notification.getTags().add("myTag");
//		notification.getTags().add("myTag-");
//		SimpleAndroidNotification.Android android = new SimpleAndroidNotification.Android();
//		android.setAlert("Hello World!");
//		notification.setAndroid(android);
	}

	private static void testRegisterAPI() throws Exception {
		Register register = new Register();
		boolean bln = false;

//		Android android = new Android();
//		android.setId("31ac6492-195a-49b5-8438-0da0f44a4fc9");
//		bln = register.register(android);
//		logger.info("{}", bln);

//		Android android = new Android();
//		android.setId("31ac6492-195a-49b5-8438-0da0f44a4fc9");
//		bln = register.unregister(android);
//		logger.info("{}", bln);

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

//		Blackberry blackberry = new Blackberry();
//		blackberry.setId("aaaaaaaa");
//		blackberry.setAlias("abc");
//		List<String> tags = new ArrayList<String>();
//		tags.add("mytag");
//		blackberry.setTags(tags);
//		bln = register.register(blackberry);
//		logger.info("{}", bln);
	}

	private static void testPush() throws Exception {
		Pusher pusher = new Pusher();

		Notification[] notifications = new Notification[3];
		for (int i = 0; i < 3; i++) {
			SimpleAndroidNotification notification = new SimpleAndroidNotification();
			notification.getApids().add("31ac6492-195a-49b5-8438-0da0f44a4fc9");
			notification.getAliases().add("myAlias");
			notification.getTags().add("myTag");
			SimpleAndroidNotification.Android android = new SimpleAndroidNotification.Android();
			android.setAlert("Hello World!");
			notification.setAndroid(android);

			notifications[i] = notification;
		}
		pusher.batchPush(notifications);

//		SimpleAppleNotification notification = new SimpleAppleNotification();
//		notification.getDeviceTokens().add("FE66489F304DC75B8D6E8200DFF8A456E8DAEACEC428B427E9518741C92C6660");
//		notification.getAliases().add("myAlias");
//		notification.getTags().add("myTag");
//		SimpleAppleNotification.Aps aps = new SimpleAppleNotification.Aps();
//		aps.setAlert("Hello World!");
//		aps.setBadge("auto");
//		notification.setAps(aps);
//		pusher.push(notification);

//		SimpleBlackBerryNotification notification = new SimpleBlackBerryNotification();
//		notification.getDevicePins().add("aaaaaaaa");
//		notification.getAliases().add("myAlias");
//		notification.getTags().add("myTag");
//		SimpleBlackBerryNotification.BlackBerry blackberry = new SimpleBlackBerryNotification.BlackBerry();
//		blackberry.setBody("Hello World!");
//		blackberry.setContentType("text/plain");
//		pusher.push(notification);
	}
}
