package jurbanairship.device;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Device {

	protected String id;
	protected String alias;
	protected List<String> tags = new ArrayList<String>();
	protected boolean active = true;
	protected Date registerTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public abstract String getRegisterAPIPath();
}
