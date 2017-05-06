package au.edu.unsw.soacourse.jobs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.jobs.model.Application;
import au.edu.unsw.soacourse.jobs.model.Posting;

public class ApplicationDao {

	public Application findById(String id) {
		String sql = "SELECT appId, jobId, candidateDetails, coverLetter, status" //
				+ " FROM applications" //
				+ " WHERE appId = ?;";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Application p = new Application();
				p.setAppId(rs.getString("appID"));
				p.setJobId(rs.getString("jobId"));
				p.setCandidateDetails(rs.getString("candidateDetails"));
				p.setCoverLetter(rs.getString("coverLetter"));
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

	public List<Application> findAll() {
		String sql = "SELECT appId, jobId, candidateDetails, coverLetter, status  FROM applications"; //
		System.out.println(sql.toString());
		Connection conn = DBUtil.getConnection();
		try {
			List<Application> list = new ArrayList<>();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Application p = new Application();
				p.setAppId(rs.getString("appID"));
				p.setJobId(rs.getString("jobId"));
				p.setCandidateDetails(rs.getString("candidateDetails"));
				p.setCoverLetter(rs.getString("coverLetter"));
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

	public List<Application> findByJobId(String id) {
		String sql = "SELECT appId, jobId, candidateDetails, coverLetter, status" //
				+ " FROM applications" //
				+ " WHERE jobId = " + id + ";";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			List<Application> list = new ArrayList<>();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Application p = new Application();
				p.setAppId(rs.getString("appID"));
				p.setJobId(rs.getString("jobId"));
				p.setCandidateDetails(rs.getString("candidateDetails"));
				p.setCoverLetter(rs.getString("coverLetter"));
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

	public int insert(Application obj) {
		String sql = "INSERT INTO applications" //
				+ " (jobId, candidateDetails, coverLetter, status)" //
				+ " VALUES (?,?,?,?);";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, obj.getJobId());
			pstmt.setString(2, obj.getCandidateDetails());
			pstmt.setString(3, obj.getCoverLetter());
			pstmt.setString(4, obj.getStatus());
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

	// TODO
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

}
