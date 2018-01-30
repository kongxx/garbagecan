# HBase客户端API-Batch操作

上一篇博客说了使用 HBase 的客户端 API 来操作操作 HBase 表中记录，今天我们看看怎样通过 API 来批量操作表中的数据。

安装上一篇博客中的方法在 HBase 中如果更新（添加/修改/删除）记录，是按行一条一条更新的，这种方法在处理大量更新操作时，性能比较差，还好在 HBase 中提供了以 Batch 方式来批量更新数据表的方法。下面就看看怎样通过 Table.batch() 方法来批量更新

要使用 Table 的 batch 模式批量更新，我们需要创建一个Put操作的集合，同时提供和一个和Put操作集合长度相等的Object对象数组，用来存放操作结果。然后再调用 “table.batch(actions, results);” 即可，看下面代码片段。

``` java
	private void batch() throws IOException {
		// 创建表
		...

		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		List<Row> actions = new ArrayList<Row>();
		for (int i = 0; i < 10000; i++) {
			Put put = new Put(Bytes.toBytes("row_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_BASE), Bytes.toBytes(COLUMN_USERNAME), Bytes.toBytes("user_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_BASE), Bytes.toBytes(COLUMN_PASSWORD), Bytes.toBytes("password_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_ADDRESS), Bytes.toBytes(COLUMN_HOME), Bytes.toBytes("home_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_ADDRESS), Bytes.toBytes(COLUMN_OFFICE), Bytes.toBytes("office_" + i));
			actions.add(put);
		}
		Object[] results = new Object[actions.size()];

		try {
			table.batch(actions, results);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			printRow(result);
		}

		table.close();

		// 删除表
		...
	}
```
	
完整例子代码如下

``` java
package my.hbasestudy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestBatch {

	private static final String TABLE_NAME = "user";

	private static final String COLUMN_FAMILY_BASE = "base";
	private static final String COLUMN_FAMILY_ADDRESS = "address";

	private static final String COLUMN_USERNAME = "username";
	private static final String COLUMN_PASSWORD = "password";
	private static final String COLUMN_HOME = "home";
	private static final String COLUMN_OFFICE = "office";

	private Connection connection;

	public static void main(String[] args) throws Exception {
		Configuration config = HBaseConfiguration.create();
		Connection connection = ConnectionFactory.createConnection(config);

		long t1 = System.currentTimeMillis();

		TestBatch t = new TestBatch(connection);
		t.batch();

		long t2 = System.currentTimeMillis();
		System.out.println("Time: " + (t2 - t1));

		connection.close();
	}

	public TestBatch(Connection connection) {
		this.connection = connection;
	}

	private void batch() throws IOException {
		createTable();

		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		List<Row> actions = new ArrayList<Row>();
		for (int i = 0; i < 10000; i++) {
			Put put = new Put(Bytes.toBytes("row_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_BASE), Bytes.toBytes(COLUMN_USERNAME), Bytes.toBytes("user_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_BASE), Bytes.toBytes(COLUMN_PASSWORD), Bytes.toBytes("password_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_ADDRESS), Bytes.toBytes(COLUMN_HOME), Bytes.toBytes("home_" + i));
			put.addColumn(Bytes.toBytes(COLUMN_FAMILY_ADDRESS), Bytes.toBytes(COLUMN_OFFICE), Bytes.toBytes("office_" + i));
			actions.add(put);
		}
		Object[] results = new Object[actions.size()];

		try {
			table.batch(actions, results);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			printRow(result);
		}

		table.close();

		deleteTable();
	}

	private void createTable() throws IOException {
		Admin admin = connection.getAdmin();

		try {
			TableDescriptor tableDesc = TableDescriptorBuilder.newBuilder(TableName.valueOf(TABLE_NAME))
					.addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(COLUMN_FAMILY_BASE)).build())
					.addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(COLUMN_FAMILY_ADDRESS)).build())
					.build();
			admin.createTable(tableDesc);
		} finally {
			admin.close();
		}
	}

	private void deleteTable() throws IOException {
		Admin admin = connection.getAdmin();

		try {
			admin.disableTable(TableName.valueOf(TABLE_NAME));
			admin.deleteTable(TableName.valueOf(TABLE_NAME));
		} finally {
			admin.close();
		}
	}

	private void printRow(Result result) {
		if (Bytes.toString(result.getRow()) != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(Bytes.toString(result.getRow()));
			sb.append("[");
			sb.append("base:username=" + Bytes.toString(result.getValue(Bytes.toBytes("base"), Bytes.toBytes("username"))));
			sb.append(", base:password=" + Bytes.toString(result.getValue(Bytes.toBytes("base"), Bytes.toBytes("password"))));
			sb.append(", address:home=" + Bytes.toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("home"))));
			sb.append(", address:office=" + Bytes.toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("office"))));
			sb.append("]");
			System.out.println(sb.toString());
		}
	}
}
```