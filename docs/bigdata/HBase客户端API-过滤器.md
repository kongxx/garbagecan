# HBase客户端API-过滤器

在使用HBase的API查询数据的时候，我们经常需要设置一些过滤条件来查询数据，这个时候我们就需要使用 HBase API 的各种 Filter 来实现这一功能。

在 HBase API 中使用过滤器需要创建一个 Filter 实例，然后使用Scan.setFilter()或者Get.setFilter()来使用 Filter，如下：

``` java
Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
Scan scan = new Scan();

Filter filter = new RowFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("row_2")));
scan.setFilter(filter);

ResultScanner resultScanner = table.getScanner(scan);
Iterator<Result> it = resultScanner.iterator();
while (it.hasNext()) {
	Result result = it.next();
	printRow(result);
}
resultScanner.close();
table.close();
```

在 HBase API 中提供了大量的 Filter 实现，比如一些常见的 Filter：

- RowFilter: 过滤指定的行记录
- FamilyFilter: 过滤指定的列族，其它列族返回null
- QualifierFilter: 过滤指定的列，其它列返回null
- ValueFilter: 过滤指定的值，，其它列返回null
- SingleColumnValueFilter: 单列值过滤器
- SingleColumnValueExcludeFilter: 单列值排除过滤器，被排除的列返回null
- PageFilter: 分页过滤器
- ColumnPaginationFilter: 列分页过滤器
- ...

在 HBase API 提供了一些常用比较运算符，这些写比较器可以用来比较过滤器中的值，如：

- CompareOperator.LESS
- CompareOperator.LESS_OR_EQUAL
- CompareOperator.EQUAL
- CompareOperator.NOT_EQUAL
- CompareOperator.GREATOR
- CompareOperator.GREATOR_OR_EQUAL
- CompareOperator.NO_OP

在 HBase API 还提供了一些常用比较器，这些写比较器可以用来比较过滤器中的值，如：

- BinaryComparator
- RegexStringComparator
- NullComparator
- SubstringComparator
- ...

下面是一段完整的列子是怎样，看看使用各种 Filter

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

public class TestFilter {
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

		TestFilter t = new TestFilter(connection);
		t.test();

		connection.close();
	}

	public TestFilter(Connection connection) {
		this.connection = connection;
	}

	private void test() throws IOException, DeserializationException {
		createTable();
		prepare();

		System.out.println("---------- Row Filter ----------");
		filter(new RowFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("row_2"))));
		filter(new RowFilter(CompareOperator.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("row_5"))));
		filter(new RowFilter(CompareOperator.EQUAL, new RegexStringComparator("row_*")));

		System.out.println("---------- Family Filter ----------");
		filter(new FamilyFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("base"))));
		filter(new FamilyFilter(CompareOperator.EQUAL, new RegexStringComparator("address*")));

		System.out.println("---------- Column Filter ----------");
		filter(new QualifierFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("username"))));
		filter(new QualifierFilter(CompareOperator.EQUAL, new RegexStringComparator("home*")));

		System.out.println("---------- Value Filter ----------");
		filter(new ValueFilter(CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("user_0"))));
		filter(new ValueFilter(CompareOperator.EQUAL, new RegexStringComparator("password_*")));

		System.out.println("---------- Single Column Value Filter ----------");
		filter(new SingleColumnValueFilter(Bytes.toBytes("base"), Bytes.toBytes("username"),
				CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("user_0"))));

		System.out.println("---------- Single Column Value Exclude Filter ----------");
		filter(new SingleColumnValueExcludeFilter(Bytes.toBytes("base"), Bytes.toBytes("username"),
				CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("user_0"))));
		
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
		for (int i = 0; i < 10; i++) {
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
