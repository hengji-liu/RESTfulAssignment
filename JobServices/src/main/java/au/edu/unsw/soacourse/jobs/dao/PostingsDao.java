package au.edu.unsw.soacourse.jobs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import au.edu.unsw.soacourse.jobs.model.Posting;

public class PostingsDAO {

	public Posting findById(String id) {
		String sql = "SELECT jobId, companyName, salaryRate, positionType, location, descriptions, status" //
				+ " FROM postings" //
				+ " WHERE jobId = ?;";
		
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
		}finally {
			DBUtil.closeConnection(conn);
		}
		
		return null;
	}

	public int insert(Posting obj) {
		String sql = "INSERT INTO postings" //
				+ " (companyName, salaryRate, positionType, location, descriptions, status)" //
				+ " VALUES (?,?,?,?,?,?)";
		
		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, obj.getCompanyName());
			pstmt.setString(2, obj.getSalaryRate());
			pstmt.setString(3, obj.getPositionType());
			pstmt.setString(4, obj.getLocation());
			pstmt.setString(5, obj.getDescriptions());
			pstmt.setString(6,  obj.getStatus());
			int affectedRows = pstmt.executeUpdate();
			if (0 == affectedRows)
				return 0 ;
	        ResultSet generatedKeys = pstmt.getGeneratedKeys();
	        if (generatedKeys.next())
            	return generatedKeys.getInt(1);
	        else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeConnection(conn);
		}
		return 0;
	}

	public Posting delete(Posting t) {
		// TODO Auto-generated method stub
		return null;
	}

	public Posting update(Posting t) {
		// TODO Auto-generated method stub
		return null;
	}

}
