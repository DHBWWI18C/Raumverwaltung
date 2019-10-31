package com.Mosbach.Raumverwaltung.Helper;

public class SimplifiedBooking {
	int id;
	int userId;
	int roomId;
	String startDate;
	String endDate;
	boolean wifi;
	boolean food;
	Object prices;
	
	
	public SimplifiedBooking(int id, int userId, int roomId, String startDate, String endDate, boolean wifi, boolean food) {
		this.id = id;
		this.userId = userId;
		this.roomId = roomId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.wifi = wifi;
		this.food = food;
		this.prices = null;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getRoomId() {
		return roomId;
	}
	
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public boolean isWifi() {
		return wifi;
	}
	
	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}
	
	public boolean isFood() {
		return food;
	}
	
	public void setFood(boolean food) {
		this.food = food;
	}
	
	public Object getPrices() {
		return prices;
	}
	
	public void setPrices(Object prices) {
		this.prices = prices;
	}
}


