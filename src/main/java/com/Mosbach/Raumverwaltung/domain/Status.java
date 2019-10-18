package com.Mosbach.Raumverwaltung.domain;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.Mosbach.Raumverwaltung.Helper.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Status {
	private int id;
	private String title;
	
	private Status(int id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public static Status buildStatusFromResultSet(ResultSet resultSet){
		Status status = null;
		try {
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
