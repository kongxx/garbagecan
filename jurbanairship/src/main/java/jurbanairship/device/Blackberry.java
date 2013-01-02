package jurbanairship.device;

public class Blackberry extends Device {
	@Override
	public String getRegisterAPIPath() {
		return "/api/device_pins/";
	}
}
