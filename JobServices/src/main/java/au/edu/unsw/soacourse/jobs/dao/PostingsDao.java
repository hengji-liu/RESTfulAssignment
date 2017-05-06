package au.edu.unsw.soacourse.jobs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.jobs.model.Posting;

public class PostingsDao {

	public Posting findById(String id) {
		String sql = "SELECT jobId, companyName, salaryRate, positionType, location, descriptions, status" //
				+ " FROM postings" //
				+ " WHERE jobId = ?;";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Posting p = new Posting();
				p.setJobId(rs.getString("jobId"));
				p.setCompanyName(rs.getString("companyName"));
				p.setSalaryRate(rs.getString("salaryRate"));
				p.setPositionType(rs.getString("positionType"));
				p.setLocation(rs.getString("location"));
				p.setDescriptions(rs.getString("descriptions"));
				p.setStatus(rs.getString("status"));
				return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}

		return null;
	}

	public int insert(Posting obj) {
		String sql = "INSERT INTO postings" //
				+ " (companyName, salaryRate, positionType, location, descriptions, status)" //
				+ " VALUES (?,?,?,?,?,?);";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, obj.getCompanyName());
			pstmt.setString(2, obj.getSalaryRate());
			pstmt.setString(3, obj.getPositionType());
			pstmt.setString(4, obj.getLocation());
			pstmt.setString(5, obj.getDescriptions());
			pstmt.setString(6, obj.getStatus());
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

	public int delete(String id) {
		String sql = "DELETE FROM postings"//
				+ " WHERE jobId = ?";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return 0;
	}

	public int update(Posting obj) {
		StringBuffer sql = new StringBuffer("UPDATE postings SET");
		String companyName = obj.getCompanyName();
		String descriptions = obj.getDescriptions();
		String location = obj.getLocation();
		String positionType = obj.getPositionType();
		String salaryRate = obj.getSalaryRate();
		String status = obj.getStatus();
		if (null != companyName)
			sql.append(" companyName='" + companyName + "',");
		if (null != descriptions)
			sql.append(" descriptions='" + descriptions + "',");
		if (null != location)
			sql.append(" location='" + location + "',");
		if (null != positionType)
			sql.append(" positionType='" + positionType + "',");
		if (null != salaryRate)
			sql.append(" salaryRate='" + salaryRate + "',");
		if (null != status)
			sql.append(" status=" + status + ",");

		// remove the last comma
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE jobId=" + obj.getJobId() + ";");
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

	public List<Posting> findAll() {
		String sql = "SELECT jobId, companyName, salaryRate, positionType, location, descriptions, status FROM postings;";
		System.out.println(sql.toString());
		Connection conn = DBUtil.getConnection();
		try {
			List<Posting> list = new ArrayList<>();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Posting p = new Posting();
				p.setJobId(rs.getString("jobId"));
				p.setCompanyName(rs.getString("companyName"));
				p.setSalaryRate(rs.getString("salaryRate"));
				p.setPositionType(rs.getString("positionType"));
				p.setLocation(rs.getString("location"));
				p.setDescriptions(rs.getString("descriptions"));
				p.setStatus(rs.getString("status"));
				list.add(p);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return null;
	}

	public List<Posting> search(String keyword, String status) {
		StringBuffer sql = new StringBuffer(
				"SELECT jobId, companyName, salaryRate, positionType, location, descriptions, status"//
						+ " FROM postings WHERE (1=1)");
		if (null != keyword && !"".equals(keyword)) {
			sql.append(" AND (" //
					+ " (companyName LIKE '%" + keyword + "%')"//
					+ " OR (positionType LIKE '%" + keyword + "%')"//
					+ " OR (location LIKE '%" + keyword + "%')"//
					+ " OR (descriptions LIKE '%" + keyword + "%')" + " )");
		}
		if (null != status && !"".equals(status)) {
			sql.append(" AND (status=" + status + ")");
		}
		sql.append(';');
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			List<Posting> list = new ArrayList<>();
			while (rs.next()) {
				Posting p = new Posting();
				p.setJobId(rs.getString("jobId"));
				p.setCompanyName(rs.getString("companyName"));
				p.setSalaryRate(rs.getString("salaryRate"));
				p.setPositionType(rs.getString("positionType"));
				p.setLocation(rs.getString("location"));
				p.setDescriptions(rs.getString("descriptions"));
				p.setStatus(rs.getString("status"));
				list.add(p);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return null;
	}
}
