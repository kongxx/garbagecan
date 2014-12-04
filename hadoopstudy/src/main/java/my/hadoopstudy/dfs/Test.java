package my.hadoopstudy.dfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;
import java.net.URI;

public class Test {
	public static void main(String[] args) throws Exception {
		String uri = "hdfs://9.111.254.189:9000/";
		Configuration config = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), config);
		FileStatus[] statuses = fs.listStatus(new Path("/user/fkong"));
		for (FileStatus status : statuses) {
			System.out.println(status);
		}

		FSDataOutputStream os = fs.create(new Path("/user/fkong/test.log"));
		os.write("Hello World!".getBytes());
		os.flush();
		os.close();

		InputStream is = fs.open(new Path("/user/fkong/test.log"));
		IOUtils.copyBytes(is, System.out, 1024, true);
	}
}
