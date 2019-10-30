package com.Mosbach.Raumverwaltung.DAO;

import com.Mosbach.Raumverwaltung.Helper.Connect;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.controller.BookingController;
import com.Mosbach.Raumverwaltung.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TokenDao {
	
	public static Token createToken(User user) {
		if (user == null) return null;
		String expireTime = LocalDateTime.now().plusHours(2).toString();
		String token = UUID.randomUUID().toString();
		String sql = "Insert into tokens (userId, token, expireTime) " +
				"VALUES (" + user.getId() + ", '" +
				token + "', '" +
				expireTime + "');";
		Connect.getResultSet(sql);
		return getTokenFromUser(user);
	}
	
	public static Token getTokenFromUser(User user){
		String sql = "Select * from tokens Where userId = " + user.getId() + ";";
		Token token = Token.buildToken(Connect.getResultSet(sql));
		if (token == null) {
			createToken(user);
			token = Token.buildToken(Connect.getResultSet(sql));
		}
		return token;
	}
	
	public static Token getTokenFromTokenString(String token){
		String sql = "Select * from tokens Where token = '" + token + "';";
		return Token.buildToken(Connect.getResultSet(sql));
	}
	
	public static Token updateToken(int user, String token, LocalDateTime expireTime){
		String sql = "UPDATE tokens " +
				"SET token =  '" + token +
				"', expireTime = '" + expireTime + "' " +
				"WHERE userId = " + user + ";";
		Connect.getResultSet(sql);
		return getTokenFromUser(UserDao.getUserById(user));
	}
	
	
	
}
