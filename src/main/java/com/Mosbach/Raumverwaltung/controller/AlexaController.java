package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.Alexa.AlexaRO;
import com.Mosbach.Raumverwaltung.Alexa.OutputSpeechRO;
import com.Mosbach.Raumverwaltung.Alexa.ResponseRO;
import com.Mosbach.Raumverwaltung.DAO.BookingDao;
import com.Mosbach.Raumverwaltung.DAO.RoomDao;
import com.Mosbach.Raumverwaltung.DAO.TokenDao;
import com.Mosbach.Raumverwaltung.domain.Booking;
import com.Mosbach.Raumverwaltung.domain.Room;
import com.Mosbach.Raumverwaltung.domain.Token;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlexaController {

  public String begruessung = "Guten Morgen! ";


	@PostMapping(path = "/alexaReadBookings")
	public AlexaRO getBookings(@RequestParam (value = "token") String tokenString,
							   @RequestBody AlexaRO alexaRO) {
		Token token = TokenDao.getTokenFromTokenString(tokenString);
		String outText = begruessung;
		
		if (alexaRO.getRequest().getType().equalsIgnoreCase("LaunchRequest")) {
			outText = outText + "Willkommen bei Ihrer Raumverwaltung";
			alexaRO = prepareResponse(alexaRO, outText, true);
		} else {
			if (alexaRO.getRequest().getType().equalsIgnoreCase("IntentRequest") && (alexaRO.getRequest().getIntent().getName().equalsIgnoreCase("TaskReadBookings"))) {
				if (token.isValid()) {
					List<Booking> bookings = BookingDao.getBookingsByUserId(token.getUserId());
					
					outText = outText + "Sie haben folgende Buchungen: ";
					
					for (Booking booking : bookings) {
						outText = outText + "Buchungsnummer " + booking.getId() + " , ";
						outText = outText + "im Raum " + booking.getRoom().getName() + " ";
						outText = outText + "vom " + booking.getStartDate() + " ";
						outText = outText + "bis " + booking.getEndDate() + " , ";
					}
				} else outText = outText + "Leider ist Ihr Login nicht mehr gültig. Bitte melden Sie sich erneut an.";
			}
			alexaRO = prepareResponse(alexaRO, outText, true);
		}
		return alexaRO;
	}

  @PostMapping(path = "/alexaReadRooms")
  public AlexaRO getRooms(@RequestBody AlexaRO alexaRO) {
    String outText = begruessung;

    if (alexaRO.getRequest().getType().equalsIgnoreCase("LaunchRequest")) {
      outText = outText + "Willkommen bei Ihrer Raumverwaltung";
      alexaRO = prepareResponse(alexaRO, outText, true);
    } else {
      if (alexaRO.getRequest().getType().equalsIgnoreCase("IntentRequest") && (alexaRO.getRequest().getIntent().getName().equalsIgnoreCase("TaskReadRooms"))) {
          List<Room> rooms = RoomDao.getAllRooms();

          outText = outText + "Folgende Räume stehen zur Verfügung: ";

          for (Room room : rooms) {
            outText = outText + "Raumnummer " + room.getId() + " , ";
            outText = outText + "Raumgröße  " + room.getRoomsize() + " ";
            outText = outText + "Raumname  " + room.getName() + ". ";
            outText = outText + "Der Raumpreis beträgt  " + room.getPrice() + " pro Tag. ";
            outText = outText + room.getInfo() + " ";
            if (room.getBeamerAvailable()) {
              outText = outText + "Ein Beamer ist verfügbar. ";
            }
            else {
              outText = outText + "Für diesen Raum ist kein Beamer verfügbar.";
            }
          }
      }
      alexaRO = prepareResponse(alexaRO, outText, true);
    }
    return alexaRO;
  }


  @PostMapping(path = "/alexaReadUserData")
  public AlexaRO getUserData(@RequestParam (value = "token") String tokenString,
                             @RequestBody AlexaRO alexaRO) {
    Token token = TokenDao.getTokenFromTokenString(tokenString);
    UserController userController = new UserController();
    User user = userController.getUser(tokenString);

    String outText = begruessung;

    if (alexaRO.getRequest().getType().equalsIgnoreCase("LaunchRequest")) {
      outText = outText + "Willkommen bei Ihrer Raumverwaltung";
      alexaRO = prepareResponse(alexaRO, outText, true);
    } else {
      if (alexaRO.getRequest().getType().equalsIgnoreCase("IntentRequest") && (alexaRO.getRequest().getIntent().getName().equalsIgnoreCase("TaskReadUserData"))) {
        if (token.isValid()) {

          outText = outText + "Hier sind Ihre Userdaten: ";
          outText = outText + "Name: " + user.getFirstName() + " " + user.getSecondName() + " , ";
          outText = outText + "Username: " + user.getUserName() + " ";
          outText = outText + "Email: " + user.getMail() + " ";

        } else outText = outText + "Leider ist Ihr Login nicht mehr gültig. Bitte melden Sie sich erneut an.";
      }
      alexaRO = prepareResponse(alexaRO, outText, true);
    }
    return alexaRO;
  }


	private AlexaRO prepareResponse(AlexaRO alexaRO, String outText, boolean shouldEndSession) {
		
		alexaRO.setRequest(null);
		alexaRO.setSession(null);
		alexaRO.setContext(null);
		OutputSpeechRO outputSpeechRO = new OutputSpeechRO();
		outputSpeechRO.setType("PlainText");
		outputSpeechRO.setText(outText);
		ResponseRO response = new ResponseRO(outputSpeechRO, shouldEndSession);
		alexaRO.setResponse(response);
		return alexaRO;
	}
}
