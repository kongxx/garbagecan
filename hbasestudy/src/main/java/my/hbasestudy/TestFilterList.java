package my.hbasestudy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestFilterList {
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

		TestFilterList t = new TestFilterList(connection);
		t.test();

		connection.close();
	}

	public TestFilterList(Connection connection) {
		this.connection = connection;
	}

	private void test() throws IOException {
//		createTable();
//		prepare();

		filterMustPassAll();
		filterMustPassOne();

//		deleteTable();
	}

	private void filterMustPassAll() throws IOException {
		System.out.println("---------- must pass all ----------");
		Filter filter1 = new SingleColumnValueFilter(Bytes.toBytes("base"), Bytes.toBytes("username"),
				CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("user_0")));
		Filter filter2 = new SingleColumnValueFilter(Bytes.toBytes("base"), Bytes.toBytes("password"),
				CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("password_0")));

		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filter1, filter2);

		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Scan scan = new Scan();
		scan.setFilter(filterList);

		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			printRow(result);
		}
		resultScanner.close();
		table.close();
	}

	private void filterMustPassOne() throws IOException {
		System.out.println("---------- must pass one ----------");
		Filter filter1 = new SingleColumnValueFilter(Bytes.toBytes("base"), Bytes.toBytes("username"),
				CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("user_0")));
		Filter filter2 = new SingleColumnValueFilter(Bytes.toBytes("base"), Bytes.toBytes("username"),
				CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("user_10")));
		Filter filter3 = new SingleColumnValueFilter(Bytes.toBytes("base"), Bytes.toBytes("username"),
				CompareOperator.EQUAL, new BinaryComparator(Bytes.toBytes("user_99")));

		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE, filter1, filter2, filter3);

		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Scan scan = new Scan();
		scan.setFilter(filterList);

		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			printRow(result);
		}
		resultScanner.close();
		table.close();
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
