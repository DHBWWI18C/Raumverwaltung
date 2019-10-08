package com.Mosbach.Raumverwaltung.domain;

import java.sql.*;

public class Connect {
	private static Connection c;
	/**
	 * Connect to a sample database
	 */
	private static Connection connect() {
		if (c == null){
			try {
				String url = "jdbc:sqlite:C:\\Users\\hendrik.strenger\\Desktop\\Raumverwaltung_Back\\src\\main\\java\\com\\Mosbach\\Raumverwaltung\\ProjectDB";
				c = DriverManager.getConnection(url);
//			System.out.println("Connection to SQLite has been established.");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return c;
		
	}
	
	public static void main(String[] args) {
		connect();
		System.out.println("Success");
	}
	
	public static ResultSet getResultSet(String sql){
		ResultSet rs = null;
		try {
			Connection conn = Connect.connect();
			Statement stmt  = conn.createStatement();
			rs    = stmt.executeQuery(sql);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
		}
		return rs;
	}
}
