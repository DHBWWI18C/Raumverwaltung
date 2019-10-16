package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.domain.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomsController {

//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/getRoom")
	public Room getRoom(@RequestParam(value = "id", defaultValue = "1") int id) {
		return Room.getRoomById(id);
	}
	
//	READ ROOMS
	@RequestMapping(method = RequestMethod.GET, path = "getRooms")
	public List<Room> getRooms(@RequestParam(value = "roomSize", required = false) Integer roomSize,
							   @RequestParam(value = "beamer", required = false) Integer beamer,
							   @RequestParam(value = "priceMax", required = false) Integer priceMax,
							   @RequestParam(value = "startDate", required = false) String startDate,
							   @RequestParam(value = "endDate", required = false) String endDate){
		List<Room> rooms = Room.getAllRooms();
		List<Room> filterdRooms = new ArrayList<>();
		LocalDate startLocalDate = null;
		LocalDate endLocalDate = null;
		
//		Standard Localdate Aufbau: 2001-01-01
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		if (startDate != null) startLocalDate = LocalDate.parse(startDate, formatter);
		if (endDate != null) endLocalDate = LocalDate.parse(endDate, formatter);
		
//		Es wird ein start und endDate benötigt, wenn eines null ist wird der Wert des anderen hierfür übernommen
		if (startLocalDate != null || endLocalDate != null){
			if (startLocalDate == null) startLocalDate = endLocalDate;
			if (endLocalDate == null) endLocalDate = startLocalDate;
		}
		
		for (Room room : rooms) {
			if ((roomSize == null || Roomsize.getRoomsizeById(roomSize).equals(room.getRoomsize())) &&
					(beamer == null || IntBoolHelper.intToBool(beamer) == room.isBeamerAvailable()) &&
					(priceMax == null || priceMax >= room.getPrice()) &&
					(checkAvailability(room, startLocalDate, endLocalDate))) {
				filterdRooms.add(room);
			}
		}
		return filterdRooms;
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
		List<Booking> bookingList = Booking.getBookings(sql);
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


