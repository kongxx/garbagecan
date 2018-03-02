package my.hivestudy;

import java.sql.*;

public class JDBCExample {
	public static void main(String[] args) throws Exception {
		Class.forName("org.apache.hive.jdbc.HiveDriver");
		Connection conn = DriverManager.getConnection("jdbc:hive2://192.168.0.192:10000/default", "jhadmin", "jhadmin");

		createTable(conn);
		insertTable(conn);
		queryTable(conn);

		conn.close();
	}

	private static void createTable(Connection conn) throws Exception {
		PreparedStatement stmt = null;

		stmt = conn.prepareStatement("drop table if exists users");
		stmt.execute();

		stmt = conn.prepareStatement("CREATE TABLE users(id int, username string, password string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','");
		stmt.execute();

		ResultSet rs = stmt.executeQuery("show tables");
		if (rs.next()) {
			System.out.println("Table:" + rs.getString(1));
		}

		stmt.close();
	}

	private static void insertTable(Connection conn) throws Exception {
		// /tmp/users.dat
		// 1,user1,password1
		// 2,user2,password2
		// 3,user3,password3
		// 4,user4,password4
		// 5,user5,password5
		PreparedStatement stmt = conn.prepareStatement("load data local inpath '/tmp/users.dat' into table users");
		stmt.executeUpdate();
		stmt.close();
	}

	private static void queryTable(Connection conn) throws Exception {
		PreparedStatement stmt = conn.prepareStatement("select * from users");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			System.out.println("id: " + rs.getString(1));
			System.out.println("username: " + rs.getString(2));
			System.out.println("password: " + rs.getString(3));

		}
		stmt.close();
	}
}
