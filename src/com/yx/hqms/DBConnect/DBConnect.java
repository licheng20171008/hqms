package com.yx.hqms.DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBConnect {

	private static String DRIVER = "com.sap.db.jdbc.Driver"; // jdbc 4.0
	private static String URL = "jdbc:sap://88.88.88.205:37015/RT0";
	private static String USER = "cct";
	private static String PASSWORD = "Qwer1234";
	
	private DBConnect(){
	}
	
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static void close(ResultSet rs, Connection con, Statement stmt) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
