package au.edu.unsw.soacourse.jobs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDao {

	public int verifyToken(String token) {
		String sql = "SELECT COUNT(*)" //
				+ " FROM partner_tokens" //
				+ " WHERE partnerToken = ?;";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return 0;
	}

	public String getPartnerUserRoleGroup(String shortKey) {
		String sql = "SELECT roleGroup" //
				+ " FROM partner_roles" //
				+ " WHERE shortKey = ?;";
		System.out.println(sql.toString());

		Connection conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, shortKey);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection(conn);
		}
		return null;
	}

}
