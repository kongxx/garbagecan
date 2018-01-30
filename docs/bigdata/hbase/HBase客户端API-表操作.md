# HBase客户端API-表操作

上一篇博客说了使用 HBase 的客户端 API 来操作管理 HBase 中的表，今天我们看看怎样通过 API 来操作表中的数据。

## 介绍

在 HBase 中对数据表中的数据的操做我们一般是通过 Table， Put， Get， Delete，Scan，Result等几个类来实现。

- Table 是表对象，对应数据库中的一张表，我们可以在表上执行添加，修改，删除和查询操作。
- Put 主要是用了对数据表中的记录执行写入/更新操作。
- Get 主要是用了对数据表中的记录执行查询操作。
- Delete 主要是用了对数据表中的记录执行查询操作。
- Scan 用来在数据表中执行查询操作。
- Result 用来保存查询的结果记录。

## 写入数据操作

- 在写入数据时，我们需要首先获取到需要操作的Table对象。
- 然后创建一个Put对象来执行更新操作，创建对象时需要给定一个行名。
- 然后在Put对象中添加需要执行的操作，这里是添加数据。
- 数据填充完后，在表上执行put操作。
- 最后，不要忘了关闭表。

``` java
	private void putRow(String row, String username, String password, String home, String office) throws IOException {
		Table table = connection.getTable(TableName.valueOf("user"));
		Put put = new Put(Bytes.toBytes(row));
		put.addColumn(Bytes.toBytes("base"), Bytes.toBytes("username"), Bytes.toBytes(username));
		put.addColumn(Bytes.toBytes("base"), Bytes.toBytes("password"), Bytes.toBytes(password));
		put.addColumn(Bytes.toBytes("address"), Bytes.toBytes("home"), Bytes.toBytes(home));
		put.addColumn(Bytes.toBytes("address"), Bytes.toBytes("office"), Bytes.toBytes(office));
		table.put(put);
		table.close();
	}
```

## 获取数据

- 需要获取到需要操作的Table对象。
- 创建Get对象来执行获取操作，创建Get对象的时候需要告诉它是要获取哪一行数据。
- 然后在表上执行get操作来获取数据。
- 取到数据后，数据是保持在Result对象中，我们可以通过Result对象的一些方法来取得需要的值。
- 最后，不要忘了关闭表。

``` java
	private void getRow(String row) throws IOException {
		Table table = connection.getTable(TableName.valueOf("user"));
		Get get = new Get(Bytes.toBytes(row));
		Result result = table.get(get);
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
		table.close();
	}
```

## 删除数据

- 需要获取到需要操作的Table对象。
- 创建Delete对象来执行删除操作，创建Delete对象的时候需要告诉它是要删除哪一行数据。
- 然后在表上执行delete操作来删除数据。
- 最后，不要忘了关闭表。

``` java
	private void deleteRow(String row) throws IOException {
		Table table = connection.getTable(TableName.valueOf("user"));
		Delete delete = new Delete(Bytes.toBytes(row));
		table.delete(delete);
		table.close();
	}
```

## 查询数据

- 需要获取到需要操作的Table对象。
- 创建Scan对象来执行查询操作。
- 然后在表上执行scan操作并得到ResultScanner对象。
- 然后我们在ResultScanner上执行迭代操作来获取其中的值。
- 最后，不要忘了关闭表。

``` java
	private void getRows() throws IOException {
		Table table = connection.getTable(TableName.valueOf("user"));
		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			getRow(result);
		}
		table.close();
	}
```

## 完整代码

最后是完整的执行数据库操作的例子代码。

``` java
package my.hbasestudy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TestAPI {

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

		TestAPI t = new TestAPI(connection);

		t.listTable();

		t.createTable();
		t.listTable();

		t.putRows();
		t.getRows();

		t.deleteRows();
		t.getRows();

		t.deleteTable();

		long t2 = System.currentTimeMillis();
		System.out.println("Time: " + (t2 - t1));

		connection.close();
	}

	public TestAPI(Connection connection) {
		this.connection = connection;
	}

	private void listTable() throws IOException {
		Admin admin = connection.getAdmin();

		try {
			List<TableDescriptor> tableDescriptors = admin.listTableDescriptors();
			for (TableDescriptor tableDescriptor : tableDescriptors) {
				TableName tableName = tableDescriptor.getTableName();
				System.out.println("Table: " + tableName);
				System.out.println("\texists: " + admin.tableExists(tableName));
				System.out.println("\tenabled: " + admin.isTableEnabled(tableName));
			}
		} finally {
			admin.close();
		}
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

	private void putRows() throws IOException {
		for (int i = 0; i < 10; i++) {
			putRow("row_" + i, "user_" + i, "password_" + i, "home_" + i, "office_" + i);
		}
	}

	private void putRow(String row, String username, String password, String home, String office) throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Put put = new Put(Bytes.toBytes(row));
		put.addColumn(Bytes.toBytes(COLUMN_FAMILY_BASE), Bytes.toBytes(COLUMN_USERNAME), Bytes.toBytes(username));
		put.addColumn(Bytes.toBytes(COLUMN_FAMILY_BASE), Bytes.toBytes(COLUMN_PASSWORD), Bytes.toBytes(password));
		put.addColumn(Bytes.toBytes(COLUMN_FAMILY_ADDRESS), Bytes.toBytes(COLUMN_HOME), Bytes.toBytes(home));
		put.addColumn(Bytes.toBytes(COLUMN_FAMILY_ADDRESS), Bytes.toBytes(COLUMN_OFFICE), Bytes.toBytes(office));
		table.put(put);
		table.close();
	}

	private void getRows() throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			getRow(result);
		}
		table.close();
	}

	private void getRow(String row) throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Get get = new Get(Bytes.toBytes(row));
		Result result = table.get(get);
		getRow(result);
		table.close();
	}

	private void getRow(Result result) {
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

	private void deleteRows() throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			Delete delete = new Delete(result.getRow());
			table.delete(delete);
		}
		table.close();
	}

	private void deleteRow(String row) throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Delete delete = new Delete(Bytes.toBytes(row));
		table.delete(delete);
		table.close();
	}
}
```
