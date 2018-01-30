# HBase客户端API-表管理

上一篇博客说了怎样搭建HBase环境，今天说说怎样使用 HBase 的客户端 API 来操作 HBase 中的数据。

## 创建工程

首先创建一个 Maven 工程，然后添加hbase客户端api的依赖项，如下：

``` xml
<dependency>
	<groupId>org.apache.hbase</groupId>
	<artifactId>hbase-client</artifactId>
	<version>2.0.0-alpha4</version>
</dependency>
```

## 添加配置文件

在 Maven 工程的resources目录下添加 hbase-site.xml 文件，hbase客户端 API 默认会在系统的 classpath 下找此文件来加载连接信息。

``` xml
<configuration>
    <property>
        <name>hbase.rootdir</name>
        <value>file:///apps/hbase-2.0.0-beta-1/data/hbase</value>
    </property>
    <property>
        <name>hbase.zookeeper.property.dataDir</name>
        <value>/apps/hbase-2.0.0-beta-1/data/zookeeper</value>
    </property>
    <property>
        <name>hbase.zookeeper.quorum</name>
        <value>bd1,bd2,bd3</value>
    </property>
    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>
</configuration>
```

## 创建 Connection 对象

要操作 HBase 的数据表，也和 JDBC 编程类似，需要创建一个 HBase Connection 对象，然后通过这个 Connection 对象来操作，操作完成后需要关闭此连接。

``` java
// 根据 hbase-site.xml 文件初始化 Configuration 对象
Configuration config = HBaseConfiguration.create();

// 根据 Configuration 对象初始化 Connection 对象
Connection connection = ConnectionFactory.createConnection(config);

// 操作数据
// ...

connection.close();
```

## 表管理

在 HBase中，要管理表，需要通过 org.apache.hadoop.hbase.client.Admin 类实现，可以通过 Connection.getAdmin() 方法来获取一个 Admin 对象实例。

### 查看表

在 HBase 中，表的描述信息保存在 org.apache.hadoop.hbase.client.TableDescriptor 类中，因此我们可以通过 Admin.listTableDescriptors() 来获取 HBase 中所有表的描述信息。然后可以通过 org.apache.hadoop.hbase.client.TableDescriptor 类的一些方法查看或修改表的定义。最后在要记得调用 Admin.close() 来关闭操作。

``` java
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
```

### 创建表

创建表的操作主要是通过 TableDescriptor 和 ColumnFamilyDescriptor 类来实现的，而要创建 TableDescriptor 和 ColumnFamilyDescriptor 对象需要使用 TableDescriptorBuilder.newBuilder(...) 和 ColumnFamilyDescriptorBuilder.newBuilder(...) 方法来创建。下面代码演示了怎样创建一个 user 表，其中包含了 base 和 address 两个列族。

``` java
	private void createTable() throws IOException {
		Admin admin = connection.getAdmin();

		try {
			TableDescriptor tableDesc = TableDescriptorBuilder.newBuilder(TableName.valueOf("user"))
					.addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("base")).build())
					.addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("address")).build())
					.build();
			admin.createTable(tableDesc);
		} finally {
			admin.close();
		}
	}
```

### 删除表

在 HBase 中对表的删除需要通过 Admin.disableTable() 和 Admin.deleteTable() 方法来实现，如下：

``` java
	private void deleteTable() throws IOException {
		Admin admin = connection.getAdmin();

		try {
			admin.disableTable(TableName.valueOf("user"));
			admin.deleteTable(TableName.valueOf("user"));
		} finally {
			admin.close();
		}
	}
```

### 完整代码

最后看一下 HBase 中表管理操作的完整例子代码。

``` shell
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
}

```
