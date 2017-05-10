package au.edu.unsw.soacourse.jobs.dao;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	static Connection getConnection() {
		try {

			String dbpath = null;
			try { // toURI, to convert %20 to space
				dbpath = DBUtil.class.getClassLoader().getResource("db.sqlite").toURI().getPath();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:" + dbpath;
			return DriverManager.getConnection(url);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
