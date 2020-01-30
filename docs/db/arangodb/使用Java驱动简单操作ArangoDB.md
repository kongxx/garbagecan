# 使用Java驱动简单操作ArangoDB

前面说过怎样使用 ArangoDB 的 Web，Shell 和 Restful API 来操作数据库，今天看一下怎样使用Java语言来操作ArangoDB数据库。

首先创建一个Maven工程，添加 ArangoDB 的 Java 驱动库

``` xml
		<dependency>
			<groupId>com.arangodb</groupId>
			<artifactId>arangodb-java-driver</artifactId>
			<version>6.5.0</version>
		</dependency>
```

完整的 pom.xml 文件内容如下：

``` xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>my.arangodbstudy</groupId>
	<artifactId>arangodbstudy</artifactId>
	<packaging>jar</packaging>
	<version>1.0-SNAPSHOT</version>
	<name>arangodbstudy</name>
	<url>http://maven.apache.org</url>

	<dependencies>
		<dependency>
			<groupId>com.arangodb</groupId>
			<artifactId>arangodb-java-driver</artifactId>
			<version>6.5.0</version>
		</dependency>
		
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>8</source>
					<target>8</target>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```

下面写了一个简单的 Java 类，其中包含了一些常用的操作，如：创建/关闭连接，数据库操作，集合操作和文档操作等。

``` java
package my.arangodbstudy;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentCreateEntity;

import java.util.Collection;

public class SimpleTest {

	private static final String DB_HOST = "localhost";
	private static final int DB_PORT = 8529;
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "<password>";
	private static final String DB_NAME = "mydb";
	private static final String COLLECTION_NAME = "users";

	public static void main(String[] args) {
		// 构造ArangoDB实例
		ArangoDB arangoDB = new ArangoDB.Builder()
				.host(DB_HOST, DB_PORT)
				.user(DB_USERNAME)
				.password(DB_PASSWORD)
				.build();

		// 判断数据库存在，如果存在删除
		if (arangoDB.db(DB_NAME).exists()) {
			arangoDB.db(DB_NAME).drop();
		}

		// 创建数据库
		arangoDB.createDatabase(DB_NAME);
		System.out.println("arangodb databases: " + arangoDB.getDatabases());

		// 获取刚才创建的数据库
		ArangoDatabase db = arangoDB.db(DB_NAME);

		// 迭代打印数据库中的集合
		Collection<CollectionEntity> collectionEntities = db.getCollections();
		for (CollectionEntity collectionEntity: collectionEntities) {
			System.out.println(collectionEntity.getName());
		}

		// 创建集合
		CollectionEntity collectionEntity = db.createCollection(COLLECTION_NAME);
		System.out.println("collection name: " + collectionEntity.getName());

		// 获取创建的集合
		ArangoCollection collection = db.collection(COLLECTION_NAME);

		// 创建文档对象
		BaseDocument document = new BaseDocument();
		document.addAttribute("name", "user");
		document.addAttribute("age", 10);
		document.addAttribute("sex", 1);

		// 写入数据
		DocumentCreateEntity documentCreateEntity = collection.insertDocument(document);
		System.out.println("collection count: " + collection.count().getCount());

		// 查询
		document = collection.getDocument(documentCreateEntity.getKey(), BaseDocument.class);
		System.out.println("document: " + document);

		// 更新文档
		document.updateAttribute("sex", 0);
		collection.updateDocument(documentCreateEntity.getKey(), document);
		document = collection.getDocument(documentCreateEntity.getKey(), BaseDocument.class);
		System.out.println("document: " + document);

		// 删除文档
		collection.deleteDocument(documentCreateEntity.getKey());
		document = collection.getDocument(documentCreateEntity.getKey(), BaseDocument.class);
		System.out.println("document: " + document);

		// 删除集合
		collection.drop();

		// 删除数据库
		db.drop();

		// shutdown ArangoDB
		arangoDB.shutdown();
	}
}
```
