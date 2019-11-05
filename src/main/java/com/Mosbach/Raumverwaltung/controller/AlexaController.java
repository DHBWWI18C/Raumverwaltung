package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.Alexa.AlexaRO;
import com.Mosbach.Raumverwaltung.Alexa.OutputSpeechRO;
import com.Mosbach.Raumverwaltung.Alexa.ResponseRO;
import com.Mosbach.Raumverwaltung.DAO.BookingDao;
import com.Mosbach.Raumverwaltung.DAO.TokenDao;
import com.Mosbach.Raumverwaltung.domain.Booking;
import com.Mosbach.Raumverwaltung.domain.Token;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlexaController {

	@PostMapping(path = "/alexa")
	public AlexaRO getBookings(@RequestParam (value = "token") String tokenString,
							   @RequestBody AlexaRO alexaRO) {
		Token token = TokenDao.getTokenFromTokenString(tokenString);
		String outText = "HALLO HENDRIK! ";
		
		if (alexaRO.getRequest().getType().equalsIgnoreCase("LaunchRequest")) {
			outText = outText + "Willkommen bei ihrer Raumverwaltung";
			alexaRO = prepareResponse(alexaRO, outText, true);
		} else {
			if (alexaRO.getRequest().getType().equalsIgnoreCase("IntentRequest") && (alexaRO.getRequest().getIntent().getName().equalsIgnoreCase("TaskReadIntent"))) {
				if (token.isValid()) {
					List<Booking> bookings = BookingDao.getBookingsByUserId(token.getUserId());
					
					outText = outText + "Du hast folgende Buchungen";
					
					for (Booking booking : bookings) {
						outText = outText + "Buchungsnummer " + booking.getId() + " , ";
						outText = outText + "im Raum " + booking.getRoom().getName() + " ";
						outText = outText + "vom " + booking.getStartDate() + " ";
						outText = outText + "bis " + booking.getEndDate() + " , ";
					}
				} else outText = outText + "Leider ist ihr Login nicht mehr gültig. Bitte melden Sie sich erneut an.";
			}
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
