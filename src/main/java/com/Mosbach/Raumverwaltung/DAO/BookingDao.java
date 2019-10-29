package com.Mosbach.Raumverwaltung.DAO;

import com.Mosbach.Raumverwaltung.Helper.Connect;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.controller.BookingController;
import com.Mosbach.Raumverwaltung.controller.RoomController;
import com.Mosbach.Raumverwaltung.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingDao {
	
	public static Booking getBookingById(int id){
		String sql = "SELECT * from bookings WHERE id = " + id + ";";
		return getBooking(sql);
	}
	
	private static Booking getBooking(String sql){
		ResultSet resultSet = Connect.getResultSet(sql);
		return Booking.buildBookingFromNResulutSet(resultSet);
	}
	
	public static List<Booking> getBookingsByUserId(int userId){
		String sql = "SELECT * from bookings WHERE user = " + userId + ";";
		return getBookings(sql);
	}
	
	public static List<Booking> getBookingsByRoomId(int rooId){
		String sql = "SELECT * from bookings WHERE room = " + rooId + ";";
		return getBookings(sql);
	}

	public static List<Booking> getBookings(String sql){
		List<Booking> bookingList = new ArrayList<>();
		ResultSet resultSet = Connect.getResultSet(sql);
		try {
			while (resultSet.next()){
				bookingList.add(Booking.buildBookingFromNResulutSet(resultSet));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return bookingList;
	}

	//ISO_LOCAL_DATE: yyyy-mm-dd, einheitlich
	public static Booking createBooking(User user, Room room, int price, boolean wifi, boolean food, Status status, LocalDate startDate, LocalDate endDate) {
		if (UserDao.getUserById(user.getId()) == null) return null;
		if (RoomDao.getRoomById(room.getId()) == null) return null;
		if (StatusDao.getStatusById(status.getId()) == null) return null;
		if (!BookingController.checkAvailability(room, startDate, endDate)) return null;
		String sql = "INSERT into bookings (user, room, price, wifi, food, status, startDate, endDate)" +
				"VALUES (" + user.getId() + ", " +
				"" + room.getId()  + ", " +
				"" + price + ", " +
				"" + IntBoolHelper.boolToInt(wifi) + ", " +
				"" + IntBoolHelper.boolToInt(food) + "," +
				"" + status.getId() + ", " +
				"'" + startDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "', " +
				"'" + endDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "');";
		
		Connect.getResultSet(sql);
		return getBooking("SELECT * FROM bookings " +
				"WHERE id = (SELECT MAX(id) " +
				"FROM bookings);");
	}
	
//	todo: updateBooking nachliefern
}
