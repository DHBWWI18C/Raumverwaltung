package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.DAO.BookingDao;
import com.Mosbach.Raumverwaltung.DAO.RoomDao;
import com.Mosbach.Raumverwaltung.domain.*;
import com.Mosbach.Raumverwaltung.DAO.RoomsizeDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.Mosbach.Raumverwaltung.controller.BookingController.checkAvailability;

@RestController
public class RoomController {

//	CREATE
	@RequestMapping(method = RequestMethod.POST, path = "/createRoom")
	public Room createRoom(@RequestParam(value = "sizeId", required = true) Integer sizeId,
						   @RequestParam(value = "beamerAvailable", required = true) Integer beamerAvailable,
						   @RequestParam(value = "price", required = true) Integer price,
						   @RequestParam(value = "info", required = false) String info,
						   @RequestParam(value = "name", required = true) String name){
		Roomsize roomsize = RoomsizeDao.getRoomsizeById(sizeId);
		if (roomsize == null) return null;
		return RoomDao.createRoom(roomsize, IntBoolHelper.intToBool(beamerAvailable), price, info, name);
	}


//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/getRoom")
	public Room getRoom(@RequestParam(value = "id", defaultValue = "1") int id) {
		return RoomDao.getRoomById(id);
	}
	
//	READ ROOMS
	@RequestMapping(method = RequestMethod.GET, path = "getRooms")
	public List<Room> getRooms(@RequestParam(value = "roomSize", required = false) Integer roomSize,
							   @RequestParam(value = "beamer", required = false) Integer beamer,
							   @RequestParam(value = "priceMax", required = false) Integer priceMax,
							   @RequestParam(value = "startDate", required = false) String startDate,
							   @RequestParam(value = "endDate", required = false) String endDate){
		List<Room> rooms = RoomDao.getAllRooms();
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
			if ((roomSize == null || RoomsizeDao.getRoomsizeById(roomSize).equals(room.getRoomsize())) &&
					(beamer == null || IntBoolHelper.intToBool(beamer) == room.getBeamerAvailable()) &&
					(priceMax == null || priceMax >= room.getPrice()) &&
					(checkAvailability(room, startLocalDate, endLocalDate))) {
				filterdRooms.add(room);
			}
		}
		return filterdRooms;
	}
	
//	UPDATE
//	todo: testen!
	@RequestMapping(method = RequestMethod.POST, path = "/updateRoom")
	public Room createRoom(@RequestParam(value = "id", required = true) Integer id,
						   @RequestParam(value = "sizeId", required = false) Integer sizeId,
						   @RequestParam(value = "beamerAvailable", required = false) Integer beamerAvailable,
						   @RequestParam(value = "price", required = false) Integer price,
						   @RequestParam(value = "info", required = false) String info,
						   @RequestParam(value = "name", required = false) String name){
		
		Room oldRoom = RoomDao.getRoomById(id);
		boolean beamerAvailableBool;
		
//		Die Update Methode erwartet alle Parameter, wenn nicht alle Parameter neu verändert werden sollen werden die Parameter von vorher gezogen und nochmals neu gesetzt
		if (beamerAvailable != null) beamerAvailableBool = IntBoolHelper.intToBool(beamerAvailable);
		else beamerAvailableBool = oldRoom.getBeamerAvailable();
		
		Roomsize roomsize = RoomsizeDao.getRoomsizeById(sizeId);
		if (roomsize == null) roomsize = oldRoom.getRoomsize();
		if (price == null) price = oldRoom.getPrice();
		if (info == null) info = oldRoom.getInfo();
		if (name != null){
			if (RoomDao.getRoomByName(name) != null) return null;
		} else name = oldRoom.getName();
		
		return RoomDao.updateRoom(id, roomsize, beamerAvailableBool, price, info, name);
	}
	
}


