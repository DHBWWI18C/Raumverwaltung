package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.domain.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
	public List<Room> getRooms(@RequestParam(value = "roomSize", required = false, defaultValue = "0") int roomSize,
							   @RequestParam(value = "beamer", required = false, defaultValue = "3") int beamer,
							   @RequestParam(value = "priceMax", required = false) Integer priceMax,
							   @RequestParam(value = "startDate", required = false) String startDate,
							   @RequestParam(value = "endDate", required = false) String endDate){
		List<Room> rooms = Room.getAllRooms();
		List<Room> filterdRooms = new ArrayList<>();
		LocalDate startLocalDate = null;
		LocalDate endLocalDate = null;
		
//		Standard Localdate Aufbau: 01-01-2001
//		Der wird ein replace durchgeführt weil das Parsen so einfacher ist
		if (startDate != null) {
			startLocalDate = LocalDate.parse(startDate.replace(".", "-"));
		}
		if (endDate != null) {
			endLocalDate = LocalDate.parse(endDate.replace(".", "-"));
		}
		
		
		for (Room room: rooms){
			if (Roomsize.getRoomsizeById(roomSize) == room.getRoomsize()) filterdRooms.add(room); //todo: funktioniert nicht
			if (IntBoolHelper.intToBool(beamer) == room.isBeamerAvailable()) filterdRooms.add(room);
			if (priceMax != null && priceMax > room.getPrice()) filterdRooms.add(room);
			if (startDate !=null || endDate != null){
				if (startDate == null) startDate = endDate;
				if (endDate == null) endDate = startDate;
				
				if (checkAvailability(room, startLocalDate, endLocalDate)) filterdRooms.add(room);
			}
		}
		return filterdRooms;
	}
	
	public static boolean checkAvailability (Room room, LocalDate startDate, LocalDate endDate){
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


