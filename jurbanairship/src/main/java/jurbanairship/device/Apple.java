package jurbanairship.device;

public class Apple extends Device {

	private int badge;
	private QuietTime quiettime;
	private String tz;

	public int getBadge() {
		return badge;
	}

	public void setBadge(int badge) {
		this.badge = badge;
	}

	public QuietTime getQuiettime() {
		return quiettime;
	}

	public void setQuiettime(QuietTime quiettime) {
		this.quiettime = quiettime;
	}

	public String getTz() {
		return tz;
	}

	public void setTz(String tz) {
		this.tz = tz;
	}

	@Override
	public String getRegisterAPIPath() {
		return "/api/device_tokens/";
	}

	public static class QuietTime {
		private String start;
		private String end;

		public QuietTime() {

		}

		public QuietTime(String start, String end) {
			this.start = start;
			this.end = end;
		}


		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}
	}
}
