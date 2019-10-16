package com.Mosbach.Raumverwaltung.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Roomsize {
	private int id;
	private String size;
	
	public Roomsize(int id, String size) {
		this.id = id;
		this.size = size;
	}
	
	public static Roomsize getRoomsizeById(int id){
		String sql = "SELECT * from roomsizes WHERE id = " + id + ";";
		return getRoomsize(sql);
	}
	
	private static Roomsize getRoomsize(String sql){
		Roomsize roomsize = null;
		
		try {
			ResultSet resultSet = Connect.getResultSet(sql);
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
