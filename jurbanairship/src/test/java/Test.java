import jurbanairship.Register;
import jurbanairship.device.Android;
import jurbanairship.device.Device;

public class Test {
	public static void main(String[] args) throws Exception {
		Device device = new Android();
		device.setId("31ac6492-195a-49b5-8438-0da0f44a4fc9");
		Register register = new Register();
		register.register(device);
	}
}
