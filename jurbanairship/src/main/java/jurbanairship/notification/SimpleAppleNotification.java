package jurbanairship.notification;

import java.util.ArrayList;
import java.util.List;

public class SimpleAppleNotification implements Notification {
	private List<String> deviceTokens = new ArrayList<String>();
	private List<String> aliases = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();
	private Aps aps;

	public List<String> getDeviceTokens() {
		return deviceTokens;
	}

	public void setDeviceTokens(List<String> deviceTokens) {
		this.deviceTokens = deviceTokens;
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

	public Aps getAps() {
		return aps;
	}

	public void setAps(Aps aps) {
		this.aps = aps;
	}

	public static class Aps {
		private String alert;
		private String badge;
		private String sound;

		public String getAlert() {
			return alert;
		}

		public void setAlert(String alert) {
			this.alert = alert;
		}

		public String getBadge() {
			return badge;
		}

		public void setBadge(String badge) {
			this.badge = badge;
		}

		public String getSound() {
			return sound;
		}

		public void setSound(String sound) {
			this.sound = sound;
		}
	}
}
