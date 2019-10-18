package com.Mosbach.Raumverwaltung.domain;

import com.Mosbach.Raumverwaltung.Helper.Connect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Roomsize {
	private int id;
	private String size;
	
	private Roomsize(int id, String size) {
		this.id = id;
		this.size = size;
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
				", size='" + size + '\'' +
				'}';
	}
	
	public int getId() {
		return id;
	}
	
	public String getSize() {
		return size;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Roomsize)) return false;
		Roomsize roomsize = (Roomsize) o;
		return id == roomsize.id &&
				size.equals(roomsize.size);
	}
	
}
