package com.yx.hqms.sqlDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yx.hqms.DBConnect.DBConnect;

public class SQLAction {

	ResultSet rs = null;
	Connection conn = null;
	PreparedStatement stmt = null;
	
	public List<String> nameSql(String tableName, String arg, String other){
		List<String> resultList = new ArrayList<String>();

		try {
			conn = DBConnect.getConnection();
			String sql = "select measure from " + tableName + " where measure like '%" + arg + "%' ";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				String measure = rs.getString("measure");
				resultList.add(measure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnect.close(rs, conn, stmt);
		}
		return resultList;
	}
}
