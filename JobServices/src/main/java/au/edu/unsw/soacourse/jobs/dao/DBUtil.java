package au.edu.unsw.soacourse.jobs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	
	static Connection getConnection(){
	    try {
	    	
	    	String dbpath = DBUtil.class.getClassLoader().getResource("db.sqlite").getPath();
	    	Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:"+dbpath;
			return DriverManager.getConnection(url);
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	static void closeConnection(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
