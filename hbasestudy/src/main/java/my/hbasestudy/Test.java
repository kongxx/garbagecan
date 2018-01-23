package my.hbasestudy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Iterator;
import java.util.List;

public class Test {
	public static void main(String[] args) throws Exception {
		Configuration config = HBaseConfiguration.create();

		Connection connection = ConnectionFactory.createConnection(config);
		Admin admin = connection.getAdmin();
		List<TableDescriptor> tableDescriptors = admin.listTableDescriptors();
		for (TableDescriptor tableDescriptor: tableDescriptors) {
			System.out.println(tableDescriptor.getTableName());
		}

		Table table = connection.getTable(TableName.valueOf("user"));
		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			System.out.println(Bytes.toString(result.getRow()));
			System.out.println("  base:username=" + Bytes.toString(result.getValue(Bytes.toBytes("base"), Bytes.toBytes("username"))));
			System.out.println("  base:password=" + Bytes.toString(result.getValue(Bytes.toBytes("base"), Bytes.toBytes("password"))));
			System.out.println("  address:home=" + Bytes.toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("home"))));
			System.out.println("  address:office=" + Bytes.toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("office"))));
		}

		connection.close();
	}
}
