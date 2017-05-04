package au.edu.unsw.soacourse.jobs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import au.edu.unsw.soacourse.jobs.model.Posting;

public class PostingsDao implements DAO<Posting> {
    
	private Connection conn;

	public PostingsDao() {
	    try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:db.sqlite";
			this.conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Posting getOne(String id) {
		String sql = "SELECT jobId, companyName, salaryRate, positionType, location, descriptions, status" //
				+ " FROM postings" //
				+ " WHERE jobId = ?;";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			Posting p = new Posting();
			p.setJobId(rs.getString("jobId"));
			p.setCompanyName(rs.getString("companyName"));
			p.setSalaryRate(rs.getString("salaryRate"));
			p.setPositionType(rs.getString("positionType"));
			p.setLocation(rs.getString("location"));
			p.setDescriptions(rs.getString("descriptions"));
			p.setStatus(rs.getInt("status"));
			return p;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
