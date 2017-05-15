package au.edu.unsw.soacourse.foundITCo;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import au.edu.unsw.soacourse.foundITCo.beans.User;
import au.edu.unsw.soacourse.foundITCo.beans.UserApplication;
import au.edu.unsw.soacourse.foundITCo.beans.UserPosting;
import au.edu.unsw.soacourse.foundITCo.beans.UserProfile;
import au.edu.unsw.soacourse.foundITCo.beans.UserReview;

public class DBUtil {
	private static final String DATABASE_URL = "jdbc:sqlite:"
			+ DBUtil.class.getClassLoader().getResource("/foundITCo.db");

	private static ConnectionSource connectionSource;
	private static Dao<User, String> userDao;
	private static Dao<UserProfile, Integer> userProfileDao;
	private static Dao<UserPosting, String> userPostingDao;
	private static Dao<UserApplication, String> userApplicationDao;
    private static Dao<UserReview, Integer> userReviewDao;

	public static Dao<UserApplication, String> getUserApplicationDao() {
		try {
			connectionSource = new JdbcConnectionSource(DATABASE_URL);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userApplicationDao == null) {
			try {

				userApplicationDao = DaoManager.createDao(connectionSource, UserApplication.class);
				if (!userApplicationDao.isTableExists()) {
					TableUtils.createTable(connectionSource, UserApplication.class);
				}
				// else {
				// TableUtils.dropTable(connectionSource, User.class, true);
				// }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userApplicationDao;
	}

	public static Dao<UserPosting, String> getUserPostingDao() {
		try {
			connectionSource = new JdbcConnectionSource(DATABASE_URL);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userPostingDao == null) {
			try {

				userPostingDao = DaoManager.createDao(connectionSource, UserPosting.class);
				if (!userPostingDao.isTableExists()) {
					TableUtils.createTable(connectionSource, UserPosting.class);
				}
				// else {
				// TableUtils.dropTable(connectionSource, User.class, true);
				// }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userPostingDao;
	}

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
				// else {
				// TableUtils.dropTable(connectionSource, User.class, true);
				// }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userDao;
	}

	public static Dao<UserProfile, Integer> getUserProfileDao() {
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


	public static Dao<UserReview, Integer> getUserReviewDao() {
		try {
			connectionSource = new JdbcConnectionSource(DATABASE_URL);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userReviewDao == null) {
			try {

				userReviewDao = DaoManager.createDao(connectionSource, UserReview.class);
				if (!userReviewDao.isTableExists()) {
					TableUtils.createTable(connectionSource, UserReview.class);
				}
				// else {
				// TableUtils.dropTable(connectionSource, Poll.class, true);
				// }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userReviewDao;
	}

	public static void closeConnection() throws IOException {
		connectionSource.close();
	}

}
