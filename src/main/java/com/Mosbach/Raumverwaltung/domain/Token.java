package com.Mosbach.Raumverwaltung.domain;

import com.Mosbach.Raumverwaltung.DAO.TokenDao;
import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.Helper.Constants;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Token {
	private int userId;
	private String token;
	private LocalDateTime expireDate;
	
	private Token(int userId, String token, LocalDateTime expireDate) {
		this.userId = userId;
		this.token = token;
		this.expireDate = expireDate;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public LocalDateTime getExpireDate() {
		return expireDate;
	}
	
	public void setExpireDate(LocalDateTime expireDate) {
		this.expireDate = expireDate;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Token)) return false;
		Token token1 = (Token) o;
		return userId == token1.userId &&
				token.equals(token1.token) &&
				expireDate.equals(token1.expireDate);
	}
	
	public static Token buildToken(ResultSet resultSet){
		Token token = null;
		try {
			token = new Token(
					resultSet.getInt(1),
					resultSet.getString(2),
					LocalDateTime.parse(resultSet.getString(3)));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return token;
	}
	
	public boolean isValid(){
		if (this.expireDate.compareTo(LocalDateTime.now()) >= 0){
			extendExpireDate(this);
			return true;
		}
		else return false;
	}
	
	private static Token extendExpireDate(Token token){
		TokenDao.updateToken(token.getUserId(), token.getToken(), LocalDateTime.now().plusHours(Constants.tokenValidTimeHours).plusMinutes(Constants.tokenValidTimeMinutes));
		return TokenDao.getTokenFromTokenString(token.getToken());
	}
	
	public static Token registerToken (User user){
		TokenDao.updateToken(user.getId(), UUID.randomUUID().toString(), LocalDateTime.now().plusHours(2));
		return TokenDao.getTokenFromUser(user);
	}
	
}
