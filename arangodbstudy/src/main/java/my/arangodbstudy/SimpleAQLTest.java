package my.arangodbstudy;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.util.MapBuilder;

import java.util.Collection;
import java.util.Map;

public class SimpleAQLTest {

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

		// 创建集合
		CollectionEntity collectionEntity = db.createCollection(COLLECTION_NAME);
		System.out.println("collection name: " + collectionEntity.getName());

		// 获取创建的集合
		ArangoCollection collection = db.collection(COLLECTION_NAME);

		// 创建文档对象并写入
		for (int i = 0; i < 10; i++) {
			BaseDocument document = new BaseDocument();
			document.addAttribute("name", "user_" + i);
			document.addAttribute("age", 10 + i);
			document.addAttribute("sex", 1);
			collection.insertDocument(document);
		}
		System.out.println("collection count: " + collection.count().getCount());

		// 使用AQL查询文档
		String query = "FOR user IN " + COLLECTION_NAME +
				" FILTER user.name == @name || user.age >= @age " +
				" RETURN user";
		Map<String, Object> params = new MapBuilder()
				.put("name", "user_0")
				.put("age", 15)
				.get();
		ArangoCursor<BaseDocument> cursor = db.query(query, params, null, BaseDocument.class);
		cursor.forEachRemaining(document -> {
			System.out.println("document: " + document);
		});

		// 删除集合
		collection.drop();

		// 删除数据库
		db.drop();

		// shutdown ArangoDB
		arangoDB.shutdown();
	}
}
