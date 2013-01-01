package jurbanairship;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;

public class JsonEntity extends StringEntity {
	public JsonEntity(String jsonString) throws UnsupportedEncodingException {
		super(jsonString, "UTF-8");
	}

	@Override
	public Header getContentType() {
		Header h = new BasicHeader("Content-Type", "application/json");
		return h;
	}
}