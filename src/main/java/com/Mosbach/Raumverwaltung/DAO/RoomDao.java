package com.Mosbach.Raumverwaltung.DAO;

import com.Mosbach.Raumverwaltung.Helper.Connect;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.domain.Room;
import com.Mosbach.Raumverwaltung.domain.Roomsize;
import com.Mosbach.Raumverwaltung.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDao {
	
	public static Room createRoom(Roomsize roomsize, boolean beamerAvailable, int price, String info, String name, String picturePath) {
//		Sicherstellen, dass Raumname nicht schon vergeben ist
		if (getRoomByName(name) != null) return null;
		String sql = "INSERT into rooms (roomsize, beamerAvailable, price, infotext, name)" +
				"VALUES (" + roomsize.getId() + ", " +
				"" + IntBoolHelper.boolToInt(beamerAvailable)  + ", " +
				"" + price + ", " +
				"'" + info + "', " +
				"'" + name + "', " +
				"'" + picturePath + "');";
		
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
				rooms.add(Room.buildRoomFromResultSet(resultSet));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return rooms;
	}
	
	private static Room getRoom(String sql){
		Room room = null;
		return Room.buildRoomFromResultSet(Connect.getResultSet(sql));
	}
	
	public static Room updateRoom(int id, Roomsize roomsize, boolean beamerAvailable, int price, String info, String name, String picturePath){
		String sql = "UPDATE rooms " +
				"SET roomsize =  '" + roomsize.getId() +
				"', beamerAvailable = '" + beamerAvailable +
				"', price = '" + price +
				"', info = '" + info +
				"', name = " + name +
				"', picturePath = " + picturePath +
				"' WHERE id = " + id + ";";
		Connect.getResultSet(sql);
		return getRoomById(id);
	}
}
