package au.edu.unsw.soacourse.foundITCo;

import java.io.IOException;
import java.sql.SQLException;

import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserProfile;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBUtil {
	private static final String DATABASE_URL = "jdbc:sqlite:"+ DBUtil.class.getClassLoader().getResource("/foundITCo.db");
	
	private static ConnectionSource connectionSource;
	private static Dao<User, String> userDao;
	private static Dao<UserProfile, String> userProfileDao;
	
	public static Dao<User, String> getUserDao() {
		try {
			connectionSource = new JdbcConnectionSource(DATABASE_URL);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userDao == null) {
			try {

				userDao = DaoManager.createDao(connectionSource, User.class);
				if (!userDao.isTableExists()) {
					TableUtils.createTable(connectionSource, User.class);
				}
//				 else {
//				 TableUtils.dropTable(connectionSource, User.class, true);
//				 }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userDao;
	}
	
	public static Dao<UserProfile, String> getUserProfileDao() {
		try {
			connectionSource = new JdbcConnectionSource(DATABASE_URL);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userProfileDao == null) {
			try {

				userProfileDao = DaoManager.createDao(connectionSource, UserProfile.class);
				if (!userProfileDao.isTableExists()) {
					TableUtils.createTable(connectionSource, UserProfile.class);
				}
				// else {
				// TableUtils.dropTable(connectionSource, Poll.class, true);
				// }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userProfileDao;
	}
	
	public static void closeConnection() throws IOException {
		connectionSource.close();
	}
	
}
