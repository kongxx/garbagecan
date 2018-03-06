package my.struts2study.upload.action;

import java.io.File;
import com.opensymphony.xwork2.ActionSupport;
import my.struts2study.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Namespace("/upload")
@ResultPath(value="/")
@ParentPackage(value="mydefault")
public class UploadAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(UploadAction.class);

	private User user;
	private File myFile;
	private String myFileContentType;
	private String myFileFileName;

	@Action(value="toUpload",
			results={
					@Result(name="success", location="upload.jsp")
			}
	)
	public String toUpload() {
		log.info("toUpload");
		return SUCCESS;
	}

	@Action(value="doUpload",
			results={
					@Result(name="success", location="upload.jsp"),
					@Result(name="input", location="upload.jsp")
			}
	)
	public String doUpload() {
		log.info("doUpload");
		log.info("user: " + user);
		log.info("myFileFileName: " + myFileFileName);
		log.info("myFileContentType: " + myFileContentType);
		log.info("myFile: " + myFile);

		return SUCCESS;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public File getMyFile() {
		return myFile;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	public String getMyFileContentType() {
		return myFileContentType;
	}

	public void setMyFileContentType(String myFileContentType) {
		this.myFileContentType = myFileContentType;
	}

	public String getMyFileFileName() {
		return myFileFileName;
	}

	public void setMyFileFileName(String myFileFileName) {
		this.myFileFileName = myFileFileName;
	}
}
