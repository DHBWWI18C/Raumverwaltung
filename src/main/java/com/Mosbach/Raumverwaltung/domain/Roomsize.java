package com.Mosbach.Raumverwaltung.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Roomsize {
	private int id;
	private String title;
	
	private Roomsize(int id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public static Roomsize buildRoomsizeFromResultSet(ResultSet resultSet){
		Roomsize roomsize = null;
		try {
			roomsize = new Roomsize(
					resultSet.getInt(1),
					resultSet.getString(2));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return roomsize;
	}
	
	@Override
	public String toString() {
		return "Roomsize{" +
				"id=" + id +
				", size='" + title + '\'' +
				'}';
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Roomsize)) return false;
		Roomsize roomsize = (Roomsize) o;
		return id == roomsize.id &&
				title.equals(roomsize.title);
	}
	
}
