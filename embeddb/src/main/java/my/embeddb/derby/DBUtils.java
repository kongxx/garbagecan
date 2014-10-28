package my.embeddb.derby;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {

	private static DBUtils dbUtils = new DBUtils();

	/** Embeded */
	private static final String DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String DB_URL = "jdbc:derby:f:myderbydb;create=true";

	/** Network */
//	private static final String DRIVER_CLASS = "org.apache.derby.jdbc.ClientDriver";
//	private static final String DB_URL = "jdbc:derby://localhost:1527/f:/myderbydb";

	private static final String DB_USERNAME = "dbadmin";
	private static final String DB_PASSWORD = "letmein";

	private DataSource datasource;

	private DBUtils() {
		Properties props = new Properties();
		props.setProperty("driverClassName", DRIVER_CLASS);
		props.setProperty("url", DB_URL);
		props.setProperty("username", DB_USERNAME);
		props.setProperty("password", DB_PASSWORD);
		props.setProperty("defaultAutoCommit", "false");
		try {
			datasource = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create datasource instance.");
		}
	}

	public static DBUtils getInstance() {
		return dbUtils;
	}

	public Connection getConnection() throws SQLException {
		Connection conn = datasource.getConnection();
		return conn;
	}
}
