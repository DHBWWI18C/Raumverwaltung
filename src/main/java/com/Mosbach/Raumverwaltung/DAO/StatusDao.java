package com.Mosbach.Raumverwaltung.DAO;

import com.Mosbach.Raumverwaltung.Helper.Connect;
import com.Mosbach.Raumverwaltung.domain.Status;

public class StatusDao {
	
	public static Status getStatusById(int id){
		String sql = "SELECT * from status WHERE id = " + id + ";";
		return getStatus(sql);
	}
	
	private static Status getStatus(String sql){
		return Status.buildStatusFromResultSet(Connect.getResultSet(sql));
	}
	
}
