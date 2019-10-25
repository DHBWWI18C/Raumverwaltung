package com.Mosbach.Raumverwaltung.domain;

import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.DAO.RoomsizeDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Room {
	private int id;
	private Roomsize roomsize;
	private boolean beamerAvailable;
	private int price;
	private String info;
	private String name;
	private String picturePath;
	
	private Room(int id, Roomsize roomsize, boolean beamerAvailable, int price, String info, String name, String picturePath) {
		this.id = id;
		this.roomsize = roomsize;
		this.beamerAvailable = beamerAvailable;
		this.price = price;
		this.info = info;
		this.name = name;
		this.picturePath = picturePath;
	}
	
	public static Room buildRoomFromResultSet(ResultSet resultSet){
		Room room = null;
		try {
			room = new Room(
					resultSet.getInt(1),
					RoomsizeDao.getRoomsizeById(resultSet.getInt(2)),
					IntBoolHelper.intToBool(resultSet.getInt(3)),
					resultSet.getInt(4),
					resultSet.getString(5),
					resultSet.getString(6),
					resultSet.getString(7));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return room;
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
	
	public boolean getBeamerAvailable() {
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
	
	public String getPicturePath() {
		return picturePath;
	}
}
