package my.arangodbstudy;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentCreateEntity;

import java.util.Collection;

public class SimpleTest {

	private static final String DB_HOST = "192.168.31.201";
	private static final int DB_PORT = 8529;
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "Letmein";
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
		System.out.println("collection cound: " + collection.count().getCount());

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
