package my.hadoopstudy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;
import java.net.URI;

public class Test {
	public static void main(String[] args) throws Exception {
		String uri = "hdfs://9.111.254.99:9000/";
		Configuration config = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), config);
		FileStatus[] statuses = fs.listStatus(new Path("/user/fkong/input"));
		for (FileStatus status : statuses) {
			System.out.println(status);
		}

		InputStream is = fs.open(new Path("/user/fkong/input/slaves"));
		IOUtils.copyBytes(is, System.out, 1024, true);
	}
}
