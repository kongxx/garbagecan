package jurbanairship.notification;

import java.util.ArrayList;
import java.util.List;

public class SimpleAndroidNotification implements Notification {
	private List<String> apids = new ArrayList<String>();
	private List<String> aliases = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();
	private Android android;

	public SimpleAndroidNotification() {

	}

	public List<String> getApids() {
		return apids;
	}

	public void setApids(List<String> apids) {
		this.apids = apids;
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

	public Android getAndroid() {
		return android;
	}

	public void setAndroid(Android android) {
		this.android = android;
	}

	public static class Android {
		private String alert;

		public String getAlert() {
			return alert;
		}

		public void setAlert(String alert) {
			this.alert = alert;
		}
	}
}
