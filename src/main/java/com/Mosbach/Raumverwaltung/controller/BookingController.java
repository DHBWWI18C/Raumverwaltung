package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.DAO.*;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.Helper.SimplifiedBooking;
import com.Mosbach.Raumverwaltung.domain.*;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {


//	CREATE
	@RequestMapping(method = RequestMethod.POST, path = "/booking")
	public Booking createBooking(@RequestParam(value = "token", 	required = true) String tokenString,
								 @RequestParam(value = "price", 	required = true) Integer price,
								 @RequestParam(value = "roomId",    required = true) Integer roomId,
								 @RequestParam(value = "wifi",      required = true) Integer wifi,
								 @RequestParam(value = "food",      required = true) Integer food,
								 @RequestParam(value = "statusId",  required = false, defaultValue = "1") Integer statusId,
								 @RequestParam(value = "startDate", required = true) String startDate,
								 @RequestParam(value = "endDate",   required = true) String endDate){
		User user;
		Token token = TokenDao.getTokenFromTokenString(tokenString);
		if (!token.isValid()) return null;
		user = UserDao.getUserById(token.getUserId());
		Room room = RoomDao.getRoomById(roomId);
		LocalDate startLocalDate = null;
		LocalDate endLocalDate = null;
	
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		if (startDate != null) startLocalDate = LocalDate.parse(startDate, formatter);
		if (endDate != null) endLocalDate = LocalDate.parse(endDate, formatter);
		
		if (!checkAvailability(room, startLocalDate, endLocalDate)) return null;
		
		return BookingDao.createBooking(
				user,
				room,
				price,
				IntBoolHelper.intToBool(wifi),
				IntBoolHelper.intToBool(food),
				StatusDao.getStatusById(statusId),
				startLocalDate,
				endLocalDate);
	}

//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/booking")
	public Booking getBooking(@RequestParam(value = "bookingId", required = true) Integer bookingId){
		return BookingDao.getBookingById(bookingId);
	}
	
//	@RequestMapping(method = RequestMethod.GET, path = "/bookings")
//	public List<Booking> getBookings(@RequestParam(value = "token", required = true) String tokenString){
//		return BookingDao.getBookingsByUserId(TokenDao.getTokenFromTokenString(tokenString).getUserId());
//	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/bookings")
	public List<SimplifiedBooking> getBookings(@RequestParam(value = "token", required = true) String tokenString) {
		int userId = TokenDao.getTokenFromTokenString(tokenString).getUserId();
		List<SimplifiedBooking> simplifiedBookings = new ArrayList<>();
		List<Booking> bookings = BookingDao.getBookingsByUserId(userId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		
		for (Booking booking : bookings){
			String startDate = booking.getStartDate().format(formatter);
			String endDate = booking.getEndDate().format(formatter);
			simplifiedBookings.add(new SimplifiedBooking(booking.getId(), userId, booking.getRoom().getId(), startDate, endDate, booking.getWifi(), booking.getFood()));
		}
		return simplifiedBookings;
	}
		
		
		@RequestMapping(method = RequestMethod.PUT, path = "/booking")
	public Booking updateBooking(@RequestParam(value = "bookingId", required = true) Integer bookingId,
								 @RequestParam(value = "token", required = true) String tokenString,
								 @RequestParam(value = "roomId", required = false) Integer roomId,
								 @RequestParam(value = "price", required = false) Integer price,
								 @RequestParam(value = "wifi", required = false) Integer wifi,
								 @RequestParam(value = "food", required = false) Integer food,
								 @RequestParam(value = "statusId", required = false, defaultValue = "1") Integer statusId,
								 @RequestParam(value = "startDate", required = false) String startDate,
								 @RequestParam(value = "endDate", required = false) String endDate){
		Token token = TokenDao.getTokenFromTokenString(tokenString);
		if (!token.isValid()) return null;
		Booking oldBooking = BookingDao.getBookingById(bookingId);
		Room room;
		User user = UserDao.getUserById(token.getUserId());
		Status status;
		LocalDate startLocalDate = null;
		LocalDate endLocalDate = null;
		
		if (roomId != null) room = RoomDao.getRoomById(roomId);
		else room = oldBooking.getRoom();
		
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
	
	@RequestMapping(method = RequestMethod.GET, path = "/price")
	public String fullPrice(
			@RequestParam(value = "startDate",  required = true) String startDate,
			@RequestParam(value = "endDate",    required = true) String endDate,
			@RequestParam(value = "roomId",     required = true) Integer roomId,
			@RequestParam(value = "food",       required = true) Integer food,
			@RequestParam(value = "wifi",       required = true) Integer wifi) {
		
		if (startDateBeforeEndDate(startDate, endDate)) {
			int duration = bookingDuration(startDate, endDate);
			Room room = RoomDao.getRoomById(roomId);
			int roomPerDay = room.getPrice();
			int roomPrice = roomPerDay * duration;
			int wifiPerDay = 15;
			int foodPerDay = 50;
			int totalWifiPrice = 0;
			int totalFoodPrice = 0;
			
			if (wifi == 1) {
				totalWifiPrice = wifiPerDay * duration;
			}
			if (food == 1) {
				totalFoodPrice = foodPerDay * duration;
			}
			
			int fullPrice = totalFoodPrice + totalWifiPrice + roomPrice;
			String json = "{" +
					"\"priceSum\"" + ":"  + fullPrice + "," +
					"\"wifiPrice\""   + ":"  + totalWifiPrice + "," +
					"\"foodPrice\""   + ":"  + totalFoodPrice + "," +
					"\"roomPrice\""   + ":"  + roomPrice +
					"}";
			
			return json;
		}
		else {
			return null;
		}
	}

// prüft richtige Datumsreihenfolge
	private static boolean startDateBeforeEndDate(String startDate, String endDate) {
    LocalDate startLocalDate;
    LocalDate endLocalDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    if (startDate != null && endDate != null) {
      startLocalDate = LocalDate.parse(startDate, formatter);
      endLocalDate = LocalDate.parse(endDate, formatter);
    } else return false;
    return startDateBeforeEndDate(startLocalDate, endLocalDate);
  }
  
  private static boolean startDateBeforeEndDate(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      return false;
    }
    // falls  = 0, liegt endDate ein Tag vor startDate
    // muss also >0 sein, damit enddate auf dem gleichen Tag oder später liegt
    if (startDate.compareTo(endDate) < 1) return true;
    else return false;
  }

  private int bookingDuration (String startDate, String endDate) {
	  if (startDateBeforeEndDate(startDate, endDate)) {
      int duration = 0;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      LocalDate startLocalDate;
      LocalDate endLocalDate;
      startLocalDate = LocalDate.parse(startDate, formatter);
      endLocalDate = LocalDate.parse(endDate, formatter);
      LocalDate tempDate = startLocalDate;

      while (tempDate.compareTo(endLocalDate) < 1) {
        tempDate = tempDate.plusDays(1);
        duration ++;
      }
	    return duration;
    }
	  else {
      return 0;
    }
  }

  @RequestMapping(method = RequestMethod.GET, path = "isRoomAvailable")
  public static boolean isRoomAvailable (@RequestParam(value = "roomId", required = true) Integer roomId,
										 @RequestParam(value = "startDate", required = true) String startDate,
										 @RequestParam(value = "endDate", required = true) String endDate) {
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	  LocalDate startLocalDate;
	  LocalDate endLocalDate;
	  startLocalDate = LocalDate.parse(startDate, formatter);
	  endLocalDate = LocalDate.parse(endDate, formatter);
	  return checkAvailability(RoomDao.getRoomById(roomId), startLocalDate, endLocalDate);
	}
	
	public static boolean checkAvailability (Room room, LocalDate startDate, LocalDate endDate){
		if (startDate == null && endDate == null) return true; //Wenn keine Angaben zum Zeitpunkt existieren wird ist der Raum verfügbar
		if (!startDateBeforeEndDate(startDate, endDate)) return false;
		List<LocalDate> checkDays = new ArrayList<>();//Tage zwischen startDate und endDate
		while (startDate.compareTo(endDate) < 1){
			checkDays.add(startDate);
			startDate = startDate.plusDays(1);
		}
		
		List<Booking> bookingList = BookingDao.getBookingsByRoomId(room.getId());
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
