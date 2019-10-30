package com.Mosbach.Raumverwaltung.domain;

import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private int id;
	private String firstName;
	private String secondName;
	private String mail;
	private String userName;
	private boolean admin;
	private String password;
	
	private User(int id, String firstName, String secondName, String mail, String userName, boolean admin, String password) {
		this.id = id;
		this.firstName = firstName;
		this.secondName = secondName;
		this.mail = mail;
		this.userName = userName;
		this.admin = admin;
		this.password = password;
	}
	
	public static User buildUserFromDB(ResultSet resultSet){
		User user = null;
		try {
			user = new User(
					resultSet.getInt(1),
					resultSet.getString(2),
					resultSet.getString(3),
					resultSet.getString(4),
					resultSet.getString(5),
					IntBoolHelper.intToBool(resultSet.getInt(5)),
					resultSet.getString(7));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return user;
	}
	
	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", secondName='" + secondName + '\'' +
				", mail='" + mail + '\'' +
				", userName='" + userName + '\'' +
				", admin=" + admin +
				", password='" + password + '\'' +
				'}';
	}
	
	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getSecondName() {
		return secondName;
	}
	
	public String getMail() {
		return mail;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public String getPassword() {
		return password;
	}
	
	public static User identifyUserByToken(Token token){
		if (token.isValid()){
			return UserDao.getUserById(token.getUserId());
		}
		else return null;
	}
}
