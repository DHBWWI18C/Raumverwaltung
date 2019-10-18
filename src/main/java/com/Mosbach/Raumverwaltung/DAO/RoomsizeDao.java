package com.Mosbach.Raumverwaltung.DAO;

import com.Mosbach.Raumverwaltung.Helper.Connect;
import com.Mosbach.Raumverwaltung.domain.Roomsize;

public class RoomsizeDao {
	
	public static Roomsize getRoomsizeById(int id){
		String sql = "SELECT * from roomsizes WHERE id = " + id + ";";
		return getRoomsize(sql);
	}
	
	private static Roomsize getRoomsize(String sql){
		return Roomsize.buildRoomsizeFromResultSet(Connect.getResultSet(sql));
	}
	
}
