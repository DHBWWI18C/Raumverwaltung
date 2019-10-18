package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.DAO.BookingDao;
import com.Mosbach.Raumverwaltung.DAO.RoomDao;
import com.Mosbach.Raumverwaltung.DAO.StatusDao;
import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.domain.Booking;
import com.Mosbach.Raumverwaltung.domain.Room;
import com.Mosbach.Raumverwaltung.domain.Status;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BookingController {

//	CREATE
	@RequestMapping(method = RequestMethod.POST, path = "/createBooking")
	public Booking createBooking(@RequestParam(value = "userId", required = true) Integer userId,
								 @RequestParam(value = "roomId", required = true) Integer roomId,
								 @RequestParam(value = "price", required = true) Integer price,
								 @RequestParam(value = "wifi", required = true) Integer wifi,
								 @RequestParam(value = "food", required = true) Integer food,
								 @RequestParam(value = "statusId", required = false, defaultValue = "1") Integer statusId,
								 @RequestParam(value = "startDate", required = true) String startDate,
								 @RequestParam(value = "endDate", required = true) String endDate){
		Room room = RoomDao.getRoomById(roomId);
		LocalDate startLocalDate = null;
		LocalDate endLocalDate = null;
	
	//		Standard Localdate Aufbau: 2001-01-01
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		if (startDate != null) startLocalDate = LocalDate.parse(startDate, formatter);
		if (endDate != null) endLocalDate = LocalDate.parse(endDate, formatter);
		
		if (!checkAvailability(room, startLocalDate, endLocalDate)) return null;
		
		return BookingDao.createBooking(
				UserDao.getUserById(userId),
				room,
				price,
				IntBoolHelper.intToBool(wifi),
				IntBoolHelper.intToBool(food),
				StatusDao.getStatusById(statusId),
				startLocalDate,
				endLocalDate);
	}

//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/getBooking")
	public Booking getBooking(@RequestParam(value = "bookingId", required = true) Integer bookingId){
		return BookingDao.getBookingById(bookingId);
	}

	//	UPDATE
	@RequestMapping(method = RequestMethod.PUT, path = "/updateBooking")
	public Booking updateBooking(@RequestParam(value = "bookingId", required = true) Integer bookingId,
								 @RequestParam(value = "userId", required = false) Integer userId,
								 @RequestParam(value = "roomId", required = false) Integer roomId,
								 @RequestParam(value = "price", required = false) Integer price,
								 @RequestParam(value = "wifi", required = false) Integer wifi,
								 @RequestParam(value = "food", required = false) Integer food,
								 @RequestParam(value = "statusId", required = false, defaultValue = "1") Integer statusId,
								 @RequestParam(value = "startDate", required = false) String startDate,
								 @RequestParam(value = "endDate", required = false) String endDate){
		Booking oldBooking = BookingDao.getBookingById(bookingId);
		Room room;
		User user;
		Status status;
		LocalDate startLocalDate = null;
		LocalDate endLocalDate = null;
		
		if (roomId != null) room = RoomDao.getRoomById(roomId);
		else room = oldBooking.getRoom();
		
		if (userId == null) user = UserDao.getUserById(userId);
		else user = oldBooking.getUser();
		
		if (price == null)  price = oldBooking.getPrice();
		if (wifi == null) wifi = IntBoolHelper.boolToInt(oldBooking.getWifi());
		if (food == null) food = IntBoolHelper.boolToInt(oldBooking.getFood());
		
		if (statusId != null) status = StatusDao.getStatusById(statusId);
		else status = oldBooking.getStatus();
		
//		Standard Localdate Aufbau: 2001-01-01
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		if (startDate != null) startLocalDate = LocalDate.parse(startDate, formatter);
		else startLocalDate = oldBooking.getStartDate();
		
		if (endDate != null) endLocalDate = LocalDate.parse(endDate, formatter);
		else endLocalDate = oldBooking.getEndDate();
		
		if (!checkAvailability(room, startLocalDate, endLocalDate)) return null;
		
		return BookingDao.createBooking(
				user,
				room,
				price,
				IntBoolHelper.intToBool(wifi),
				IntBoolHelper.intToBool(food),
				status,
				startLocalDate,
				endLocalDate);
	}
	
	public static boolean checkAvailability (Room room, LocalDate startDate, LocalDate endDate){
//		todo: funktioniert nicht wenn endDate vor startDate liegt
		if (startDate == null && endDate == null) return true;
		List<LocalDate> checkDays = new ArrayList<>();//Tage zwischen startDate und endDate
		while (startDate.compareTo(endDate) < 1){
			checkDays.add(startDate);
			startDate = startDate.plusDays(1);
		}
		
		String sql = "SELECT * from bookings WHERE room = " + room.getId() + ";";
		List<Booking> bookingList = BookingDao.getBookings(sql);
		for (Booking booking: bookingList) {
			LocalDate controllDate = booking.getStartDate();
			while (controllDate.compareTo(booking.getEndDate()) < 1){
				if (checkDays.contains(controllDate)) return false;
				controllDate = controllDate.plusDays(1);
			}
			if (checkDays.contains(booking.getEndDate())) return false;
		}
		
		return true;
	}

}
