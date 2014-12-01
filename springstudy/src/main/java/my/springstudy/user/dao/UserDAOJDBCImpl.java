package my.springstudy.user.dao;

import my.springstudy.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository ("userDAOJDBCImpl")
public class UserDAOJDBCImpl implements UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

//	public User add(User user) {
//		this.getJdbcTemplate().update("insert into user (id, username, password) values (?, ?, ?)",
//				new Object[] {user.getId(), user.getUsername(), user.getPassword()});
//		return user;
//	}
//
//	public void update(User user) {
//		this.getJdbcTemplate().update("update user set username=?, password=? where id=?",
//			new Object[] {user.getUsername(), user.getPassword(), user.getId()});
//	}
//
//	public void delete(User user) {
//		this.getJdbcTemplate().update("delete from user where id=?", new Object[] {user.getId()});
//	}
	
	public User findById(String id) {
		String sql = "select id, username, password from user where id = ?";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				return user;
			}
		};
		return (User) this.jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);
	}

//	public List<User> list() {
//		String sql = "select id, username, password from user";
//		RowMapper mapper = new RowMapper() {
//			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
//				User user = new User();
//				user.setId(rs.getString("id"));
//				user.setUsername(rs.getString("username"));
//				user.setPassword(rs.getString("password"));
//				return user;
//			}
//		};
//		List<User> users = getJdbcTemplate().query(sql, mapper);
//		return users;
//	}

}
