package com.Mosbach.Raumverwaltung.domain;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public static User createUser(String firstName, String secondName, String mail, String userName, boolean admin, String password) {
		/*
		Durch eine vorherige Abfrage nach dem Usernamen soll verhindert werden,
		dass man so an das Passwort von einem fremden Account gelangt
		Durch die Abfrage nach der Mail wird sichergestellt dass der Wert Unique ist
		 */
		if (getUserByUserName(userName) != null) {
			System.out.println("Username schon vergeben");
			return null;
		}
		if (getUserByMail(mail) != null){
			System.out.println("Mail schon vergeben");
			return null;
		}
		String sql = "INSERT into users (firstName, secondName, email, userName, admin, password)" +
				"VALUES ('" + firstName  + "', " +
				"'" + secondName + "', " +
				"'" + mail + "', " +
				"'" + userName + "',  " +
				"" + IntBoolHelper.boolToInt(admin) + ", " +
				"'" + password + "');";
		
		Connect.getResultSet(sql);
		return getUserByUserName(userName);
	}
	
	public static List<User> getUsers(){
		String sql = "SELECT * from users ;";
		return getUsers(sql);
	}
	
	public static User getUserById(int id){
		String sql = "SELECT * from users WHERE id = " + id + ";";
		return getUser(sql);
	}
	
	public static User getUserByUserName(String userName){
		String sql = "SELECT * from users WHERE userName = '" + userName + "';";
		return getUser(sql);
	}
	
	public static User getUserByMail(String mail){
		String sql = "SELECT * from users WHERE email = '" + mail + "';";
		return getUser(sql);
	}
	
	private static User getUser(String sql){
		return buildUserFromDB(Connect.getResultSet(sql));
	}
	
	private static List<User> getUsers(String sql){
		List<User> users = new ArrayList<>();
		ResultSet resultSet = Connect.getResultSet(sql);
		try{
			while (resultSet.next()){
				users.add(buildUserFromDB(resultSet));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return users;
	}
	
	private static User buildUserFromDB(ResultSet resultSet){
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
	
	public static User updateUser(int id, String firstName, String secondName, String mail, String userName, boolean admin, String password){
		String sql = "UPDATE users " +
				"SET firstName =  '" + firstName +
				"', secondName = '" + secondName +
				"', email = '" + mail +
				"', userName = '" + userName +
				"', admin = " + IntBoolHelper.boolToInt(admin) +
				", password = '" + password +
				"' WHERE id = " + id + ";";
		Connect.getResultSet(sql);
		return getUserById(id);
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
}
