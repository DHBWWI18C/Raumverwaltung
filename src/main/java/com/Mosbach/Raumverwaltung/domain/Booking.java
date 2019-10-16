package com.Mosbach.Raumverwaltung.domain;

import com.Mosbach.Raumverwaltung.controller.RoomController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Booking {
	private int id;
	private User user;
	private Room room;
	private int price;
	private boolean wifi;
	private boolean food;
	private Status status;
	private LocalDate startDate;
	private LocalDate endDate;
	
	private Booking(int id, User user, Room room, int price, boolean wifi, boolean food, Status status, LocalDate startDate, LocalDate endDate) {
		this.id = id;
		this.user = user;
		this.room = room;
		this.price = price;
		this.wifi = wifi;
		this.food = food;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public static Booking createBooking(User user, Room room, int price, boolean wifi, boolean food, Status status, LocalDate startDate, LocalDate endDate) {
		if (User.getUserById(user.getId()) == null) return null;
		if (Room.getRoomById(room.getId()) == null) return null;
		if (Status.getStatusById(status.getId()) == null) return null;
		if (RoomController.checkAvailability(room, startDate, endDate)) return null;
		String sql = "INSERT into bookings (user, room, price, wifi, food, status)" +
				"VALUES (" + user.getId() + ", " +
				"" + room.getId()  + ", " +
				"" + price + ", " +
				"" + IntBoolHelper.boolToInt(wifi) + ", " +
				"" + IntBoolHelper.boolToInt(food) + "," +
				"" + status.getId() + ", " +
				"" + startDate.format(DateTimeFormatter.BASIC_ISO_DATE) + ", " +
				"" + endDate.format(DateTimeFormatter.BASIC_ISO_DATE) + ");";
		
		Connect.getResultSet(sql);
		return getBooking("SELECT * FROM bookings " +
						"WHERE id = (SELECT MAX(id) " +
						"FROM bookings);");
	}
	
	
	public static Booking getBookingById(int id){
		String sql = "SELECT * from bookings WHERE id = " + id + ";";
		return getBooking(sql);
	}
	
	private static Booking getBooking(String sql){
		Booking booking = null;
		try {
			ResultSet resultSet = Connect.getResultSet(sql);
			booking = new Booking(
					resultSet.getInt(1),
					User.getUserById(resultSet.getInt(2)),
					Room.getRoomById(resultSet.getInt(3)),
					resultSet.getInt(4),
					IntBoolHelper.intToBool(resultSet.getInt(5)),
					IntBoolHelper.intToBool(resultSet.getInt(6)),
					Status.getStatusById(resultSet.getInt(7)),
					LocalDate.parse(resultSet.getString(8)),
					LocalDate.parse(resultSet.getString(9)));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return booking;
	}
	
	public static List<Booking> getBookings(String sql){
		List<Booking> bookingList = new ArrayList<>();
		Booking booking = null;
		try {
			ResultSet resultSet = Connect.getResultSet(sql);
			while (resultSet.next()) {
				booking = new Booking(
						resultSet.getInt(1),
						User.getUserById(resultSet.getInt(2)),
						Room.getRoomById(resultSet.getInt(3)),
						resultSet.getInt(4),
						IntBoolHelper.intToBool(resultSet.getInt(5)),
						IntBoolHelper.intToBool(resultSet.getInt(6)),
						Status.getStatusById(resultSet.getInt(7)),
						LocalDate.parse(resultSet.getString(8)),
						LocalDate.parse(resultSet.getString(9)));
				bookingList.add(booking);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return bookingList;
	}
	
	public int getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public int getPrice() {
		return price;
	}
	
	public boolean isWifi() {
		return wifi;
	}
	
	public boolean isFood() {
		return food;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	@Override
	public String toString() {
		return "Booking{" +
				"id=" + id +
				", user=" + user +
				", room=" + room +
				", price=" + price +
				", wifi=" + wifi +
				", food=" + food +
				", status=" + status +
				", startDate=" + startDate +
				", endDate=" + endDate +
				'}';
	}
}
