package com.Mosbach.Raumverwaltung.domain;

import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.DAO.RoomDao;
import com.Mosbach.Raumverwaltung.DAO.StatusDao;
import com.Mosbach.Raumverwaltung.DAO.UserDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
	
	public static Booking buildBookingFromNResulutSet(ResultSet resultSet){
		Booking booking = null;
		try {
			booking = new Booking(
					resultSet.getInt(1),
					UserDao.getUserById(resultSet.getInt(2)),
					RoomDao.getRoomById(resultSet.getInt(3)),
					resultSet.getInt(4),
					IntBoolHelper.intToBool(resultSet.getInt(5)),
					IntBoolHelper.intToBool(resultSet.getInt(6)),
					StatusDao.getStatusById(resultSet.getInt(7)),
					LocalDate.parse(resultSet.getString(8)),
					LocalDate.parse(resultSet.getString(9)));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return booking;
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
	
	public boolean getWifi() {
		return wifi;
	}
	
	public boolean getFood() {
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
