package com.Mosbach.Raumverwaltung.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Room {
	private int id;
	private Roomsize roomsize;
	private boolean beamerAvailable;
	private int price;
	private String info;
	private String name;
	
	private Room(int id, Roomsize roomsize, boolean beamerAvailable, int price, String info, String name) {
		this.id = id;
		this.roomsize = roomsize;
		this.beamerAvailable = beamerAvailable;
		this.price = price;
		this.info = info;
		this.name = name;
	}
	
	public static Room createRoom(int roomsizeId, boolean beamerAvailable, int price, String info, String name) {
//		Sicherstellen, dass Raumname nicht schon vergeben ist
		if (getRoomByName(name) != null) return null;
//		Sicherstellen, dass die Raumgröße existiert
		if (Roomsize.getRoomsizeById(roomsizeId) == null) return null;
		String sql = "INSERT into rooms (roomsize, beamerAvailable, price, infotext, name)" +
				"VALUES (" + roomsizeId + ", " +
				"" + IntBoolHelper.boolToInt(beamerAvailable)  + ", " +
				"" + price + ", " +
				"'" + info + "', " +
				"'" + name + "');";
		
		Connect.getResultSet(sql);
		return getRoomByName(name);
	}
	
	public static Room getRoomByName(String name){
		String sql = "SELECT * from rooms WHERE name = '" + name + "';";
		return getRoom(sql);
	}
	
	public static Room getRoomById(int id){
		String sql = "SELECT * from rooms WHERE id = " + id + ";";
		return getRoom(sql);
	}
	
	public static List<Room> getAllRooms(){
		String sql = "SELECT * from rooms ;";
		return getRooms(sql);
	}
	
	private static List<Room> getRooms(String sql){
		List<Room> rooms = new ArrayList<>();
		ResultSet resultSet = Connect.getResultSet(sql);
		try {
			while (resultSet.next()){
				rooms.add(buildRoomFromResultSet(resultSet));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return rooms;
	}
	
	private static Room buildRoomFromResultSet(ResultSet resultSet){
		Room room = null;
		try {
			room = new Room(
					resultSet.getInt(1),
					Roomsize.getRoomsizeById(resultSet.getInt(2)),
					IntBoolHelper.intToBool(resultSet.getInt(3)),
					resultSet.getInt(4),
					resultSet.getString(5),
					resultSet.getString(6));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return room;
	}
	
	private static Room getRoom(String sql){
		Room room = null;
		return buildRoomFromResultSet(Connect.getResultSet(sql));
	}
	
	@Override
	public String toString() {
		return "Room{" +
				"id=" + id +
				", roomsize=" + roomsize.toString() +
				", beamerAvailable=" + beamerAvailable +
				", price=" + price +
				", info='" + info + '\'' +
				'}';
	}
	
	public int getId() {
		return id;
	}
	
	public Roomsize getRoomsize() {
		return roomsize;
	}
	
	public boolean isBeamerAvailable() {
		return beamerAvailable;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String getInfo() {
		return info;
	}
	
	public String getName() {
		return name;
	}
}
