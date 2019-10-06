package com.Mosbach.Raumverwaltung.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Status {
	private int id;
	private String title;
	
	public Status(int id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public static Status getStatusById(int id){
		String sql = "SELECT * from status WHERE id = " + id + ";";
		return getStatus(sql);
	}
	
	private static Status getStatus(String sql){
		Status status = null;
		
		try {
			ResultSet resultSet = Connect.getResultSet(sql);
			status = new Status(
					resultSet.getInt(1),
					resultSet.getString(2));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return status;
	}
	
	@Override
	public String toString() {
		return "Status{" +
				"id=" + id +
				", title='" + title + '\'' +
				'}';
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
}
