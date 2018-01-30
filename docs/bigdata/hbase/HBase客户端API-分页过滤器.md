# HBase客户端API-分页过滤器

前一篇博客说了一下 HBase 的一些过滤器，今天看看 HBase 的分页过滤器。

在 HBase 中分页过滤是通过 PageFilter 来实现的，在创建这个参数的时候需要设置一个pageSize参数，通过这个参数来控制每页返回的行数，并且在每次查询时需要指定本次查询的起始行。

这里有一点需要注意，HBase中行键的排序是按字典顺序排列的，因此返回的结果也是按此顺序排列。

下面看一下分页过滤的代码片段

``` java
Filter filter = new PageFilter(10);
Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

byte[] lastRow = null;
while(true) {
	Scan scan = new Scan();
	scan.setFilter(filter);
	if (lastRow != null) {
		scan.withStartRow(lastRow, false);
	}
	ResultScanner resultScanner = table.getScanner(scan);
	Iterator<Result> it = resultScanner.iterator();
	int count = 0;
	while (it.hasNext()) {
		Result result = it.next();
		printRow(result);
		lastRow = result.getRow();
		count ++;
	}
	resultScanner.close();
	if (count == 0) {
		break;
	}
}
table.close();
```

- 首先需要创建一个PageFilter对象，并设置每页10行。
- 然后通过Scan.withStartRow()来设置起始行，对于第一次查询，可以不用设置。其中第二个参数是用来标识是否需要包括指定起始行。
- 执行查询，对于每次查询设置了一个计数，当计数为 0 时，表示本次查询没有返回结果，说明查询遍历完成，此时跳出循环。

下面是可运行完整代码

``` java
package my.hbasestudy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestPageFilter {
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

		TestPageFilter t = new TestPageFilter(connection);
		t.test();

		connection.close();
	}

	public TestPageFilter(Connection connection) {
		this.connection = connection;
	}

	private void test() throws IOException, DeserializationException {
		createTable();
		prepare();

		Filter filter = new PageFilter(10);
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		byte[] lastRow = null;
		while(true) {
			Scan scan = new Scan();
			scan.setFilter(filter);
			if (lastRow != null) {
				scan.withStartRow(lastRow, false);
			}
			ResultScanner resultScanner = table.getScanner(scan);
			Iterator<Result> it = resultScanner.iterator();
			int count = 0;
			while (it.hasNext()) {
				Result result = it.next();
				printRow(result);
				lastRow = result.getRow();
				count ++;
			}
			resultScanner.close();
			if (count == 0) {
				break;
			}
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

	private void prepare() throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

		List<Row> actions = new ArrayList<Row>();
		for (int i = 0; i < 100; i++) {
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

		table.close();
	}

	private void filter(Filter filter) throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Scan scan = new Scan();
		scan.setFilter(filter);
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			printRow(result);
		}
		resultScanner.close();
		table.close();
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
