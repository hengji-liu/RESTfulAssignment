package au.edu.unsw.soacourse.jobs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.jobs.model.Review;

public class ReviewsDao {

	public Review findById(String id) {
		String sql = "SELECT reviewId, appId, reviewerDetails, comments, decision" //
				+ " FROM reviews" //
				+ " WHERE reviewId = ?;";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Review r = new Review();
				r.setReviewId(rs.getString("reviewId"));
				r.setAppId(rs.getString("appId"));
				r.setReviewerDetails(rs.getString("reviewerDetails"));
				r.setComments(rs.getString("comments"));
				r.setDecision(rs.getString("decision"));
				return r;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}

		return null;
	}

	public List<Review> findAll() {
		String sql = "SELECT reviewId, appId, reviewerDetails, comments, decision  FROM reviews"; //
		System.out.println(sql.toString());
		Connection conn = DBUtil.getConnection();
		try {
			List<Review> list = new ArrayList<>();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Review r = new Review();
				r.setReviewId(rs.getString("reviewId"));
				r.setAppId(rs.getString("appId"));
				r.setReviewerDetails(rs.getString("reviewerDetails"));
				r.setComments(rs.getString("comments"));
				r.setDecision(rs.getString("decision"));
				list.add(r);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return null;
	}

	public List<Review> findByAppId(String id) {
		String sql = "SELECT reviewId, appId, reviewerDetails, comments, decision" //
				+ "  FROM reviews" //
				+ " WHERE appId = " + id + ";";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			List<Review> list = new ArrayList<>();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Review r = new Review();
				r.setReviewId(rs.getString("reviewId"));
				r.setAppId(rs.getString("appId"));
				r.setReviewerDetails(rs.getString("reviewerDetails"));
				r.setComments(rs.getString("comments"));
				r.setDecision(rs.getString("decision"));
				list.add(r);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return null;
	}

	public int insert(Review obj) {
		String sql = "INSERT INTO reviews" //
				+ " (appId, reviewerDetails, comments, decision)" //
				+ " VALUES (?,?,?,?);";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, obj.getAppId());
			pstmt.setString(2, obj.getReviewerDetails());
			pstmt.setString(3, obj.getComments());
			pstmt.setString(4, obj.getDecision());
			int affectedRows = pstmt.executeUpdate();
			if (0 == affectedRows)
				return 0;
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next())
				return generatedKeys.getInt(1);
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return 0;
	}

	public int update(Review obj) {
		StringBuffer sql = new StringBuffer("UPDATE reviews SET");
		String appId = obj.getAppId();
		String reviewerDetails = obj.getReviewerDetails();
		String comments = obj.getComments();
		String decision = obj.getDecision();
		if (null != appId)
			sql.append(" appId=" + appId + ",");
		if (null != reviewerDetails)
			sql.append(" reviewerDetails='" + reviewerDetails + "',");
		if (null != comments)
			sql.append(" comments='" + comments + "',");
		if (null != decision)
			sql.append(" decision=" + decision + ",");

		// remove the last comma
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE reviewId=" + obj.getReviewId() + ";");
		System.out.println(sql.toString());

		// update
		Connection conn = DBUtil.getConnection();
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate(sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return 0;
	}
}
