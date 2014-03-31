package my.embeddb.derby;

import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyTest {

	public static void main(String[] args) throws Exception {
		//createTable();
		//insertTable();
		selectTable();
	}

	public static void createTable() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			String sql = "create table mytable(id int, name varchar(30))";
			ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (Exception ex) {
			Logger.getLogger(DerbyTest.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(conn);
		}
	}

	public static void insertTable() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			String sql = "insert into mytable(id, name) values (?, ?)";
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < 10; i++) {
				ps.setObject(1, i);
				ps.setObject(2, "name_" + i);
				ps.executeUpdate();
			}
		} catch (Exception ex) {
			Logger.getLogger(DerbyTest.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(conn);
		}
	}

	public static void selectTable() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String sql = "select * from mytable";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getObject(1) + "\t" + rs.getObject(2));
			}
		} catch (Exception ex) {
			Logger.getLogger(DerbyTest.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			DbUtils.closeQuietly(conn);
		}
	}

	public static Connection getConnection() throws Exception {
		return DBUtils.getInstance().getConnection();
	}
}
