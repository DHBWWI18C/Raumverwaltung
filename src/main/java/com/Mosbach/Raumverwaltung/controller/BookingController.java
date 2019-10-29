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
import com.fasterxml.jackson.databind.util.JSONPObject;
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
//	todo: id automatisch ziehen aus der Session siehe loginController
//	für alle Sachen wo beim create ein User benötigt wird
//	den bisherigen Teil mit den Usern bitte nur auskommentieren, hier muss mit Luca noch mal abgestimmt werden
//	andere möglichkeit wäre nämlich, das so zu lassen wie es jetzt ist da der User auch noch in einer Frontend Session gespeichert ist und von da bei jedem Aufruf mitübertragen werden kann
	@RequestMapping(method = RequestMethod.POST, path = "/createBooking")
	public Booking createBooking(
    @RequestParam(value = "userId",    required = true) Integer userId,
    @RequestParam(value = "roomId",    required = true) Integer roomId,
    @RequestParam(value = "price",     required = true) Integer price,
    @RequestParam(value = "wifi",      required = true) Integer wifi,
    @RequestParam(value = "food",      required = true) Integer food,
    @RequestParam(value = "statusId",  required = false, defaultValue = "1") Integer statusId,
    @RequestParam(value = "startDate", required = true) String startDate,
    @RequestParam(value = "endDate",   required = true) String endDate,
    HttpSession session){
		Room room = RoomDao.getRoomById(roomId);
    User user = UserDao.getUserById(userId);
    session.setAttribute("user", user.getId());
		LocalDate startLocalDate = null;
		LocalDate endLocalDate = null;
	
	//		Standard Localdate Aufbau: 2001.01.01
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
	    else if (wifi == 0) {
	      totalWifiPrice = 0;
      }
      if (food == 1) {
        totalFoodPrice = foodPerDay * duration;
      }
      else if (food == 0) {
        totalFoodPrice = 0;
      }

      //Todo: JSON-Objekt erzeugen
	    int fullPrice = totalFoodPrice + totalWifiPrice + roomPrice;
	    String json = "{ price: " +
        "{" +
        "gesamtPreis" + " : "  + fullPrice + " , " +
        "wifiPrice"   + " : "  + totalWifiPrice + " , " +
        "foodPrice"   + " : "  + totalFoodPrice + " , " +
        "roomPrice"   + " : "  + roomPrice +
        "}" +
        "}";

	    return json;
    }
	  else {
      return null;
    }
  }


//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/getBooking")
	public Booking getBooking(@RequestParam(value = "bookingId", required = true) Integer bookingId){
		return BookingDao.getBookingById(bookingId);
	}

//	todo: testen
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

// prüft richtige Datumsreihenfolge
	public static boolean startDateBeforeEndDate (String startDate, String endDate) {
    LocalDate startLocalDate;
    LocalDate endLocalDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    if (startDate != null && endDate != null) {
      startLocalDate = LocalDate.parse(startDate, formatter);
      endLocalDate = LocalDate.parse(endDate, formatter);
    }
    else {
      return false;
    }
    // falls  = 0, liegt endDate ein Tag vor startDate
    // muss also >0 sein, damit enddate auf dem gleichen Tag oder später liegt
    if (startLocalDate.compareTo(endLocalDate) < 1) return true;
    else return false;
  }


  public static boolean startDateBeforeEndDate (LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      return false;
    }
    // falls  = 0, liegt endDate ein Tag vor startDate
    // muss also >0 sein, damit enddate auf dem gleichen Tag oder später liegt
    if (startDate.compareTo(endDate) < 1) return true;
    else return false;
  }

  public int bookingDuration (String startDate, String endDate) {
	  if (startDateBeforeEndDate(startDate, endDate)) {
      int duration = 0;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      LocalDate startLocalDate;
      LocalDate endLocalDate;
      startLocalDate = LocalDate.parse(startDate, formatter);
      endLocalDate = LocalDate.parse(endDate, formatter);
      LocalDate tempDate = startLocalDate;

      while (tempDate.compareTo(endLocalDate) < 1) {
        tempDate.plusDays(1);
        duration ++;
      }
	    return duration;
    }
	  else {
      return 0;
    }
  }

  //liefert true zurück, wenn der Raum verfügbar ist
  //deswegen normalerweise !checkAvailability und dann ggf. aussteigen
  //todo: methode unten löschen und hieraus Endpunkt machen
	public static boolean checkAvailability (
      @RequestParam(value = "roomId", required = true) Integer roomId,
      @RequestParam(value = "startDate", required = true) String startDate,
      @RequestParam(value = "endDate", required = true) String endDate) {){
    Room room = RoomDao.getRoomById(roomId);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate LocalStartDate = LocalDate.parse(startDate, formatter);
    LocalDate LocalEndDate = LocalDate.parse(endDate, formatter);
  }
    if (startDateBeforeEndDate(LocalStartDate, LocalEndDate)) {
      if (LocalStartDate == null && endDate == null) return true;
      List<LocalDate> checkDays = new ArrayList<>();//Tage zwischen startDate und endDate
      while (startDate.compareTo(endDate) < 1) {
        checkDays.add(LocalStartDate);
        startDate = startDate.plusDays(1);
      }

      String sql = "SELECT * from bookings WHERE room = " + room.getId() + ";";
      List<Booking> bookingList = BookingDao.getBookings(sql);
      for (Booking booking : bookingList) {
        LocalDate controllDate = booking.getStartDate();
        while (controllDate.compareTo(booking.getEndDate()) < 1) {
          if (checkDays.contains(controllDate)) return false;
          controllDate = controllDate.plusDays(1);
        }
        if (checkDays.contains(booking.getEndDate())) return false;
      }

      return true;
    }
    //weil endDate vor StartDate liegt
    return false;
	}
}
