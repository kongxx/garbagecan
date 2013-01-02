package jurbanairship.notification;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SimpleBlackBerryNotification implements Notification {
	@SerializedName("device_pins")
	private List<String> devicePins = new ArrayList<String>();
	private List<String> aliases = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();
	private BlackBerry blackberry;

	public List<String> getDevicePins() {
		return devicePins;
	}

	public void setDevicePins(List<String> devicePins) {
		this.devicePins = devicePins;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public BlackBerry getBlackberry() {
		return blackberry;
	}

	public void setBlackberry(BlackBerry blackberry) {
		this.blackberry = blackberry;
	}

	public static class BlackBerry {
		@SerializedName("content-type")
		private String contentType = "text/plain";
		private String body;

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}
	}
}
