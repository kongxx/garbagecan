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

		TestAPI t = new TestAPI(connection);

		t.listTable();

		t.createTable();
		t.listTable();

		t.putRows();
		t.getRows();

		t.deleteRows();
		t.getRows();

		t.deleteTable();

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
			System.out.println(Bytes.toString(result.getRow()));
			System.out.println("  base:username=" + Bytes.toString(result.getValue(Bytes.toBytes("base"), Bytes.toBytes("username"))));
			System.out.println("  base:password=" + Bytes.toString(result.getValue(Bytes.toBytes("base"), Bytes.toBytes("password"))));
			System.out.println("  address:home=" + Bytes.toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("home"))));
			System.out.println("  address:office=" + Bytes.toString(result.getValue(Bytes.toBytes("address"), Bytes.toBytes("office"))));
		}
	}

	private void deleteRows() throws IOException {
		Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> it = resultScanner.iterator();
		while (it.hasNext()) {
			Result result = it.next();
			getRow(result);
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
