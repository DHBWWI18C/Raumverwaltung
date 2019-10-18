package com.Mosbach.Raumverwaltung.DAO;

import com.Mosbach.Raumverwaltung.Helper.Connect;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
	
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
		return User.buildUserFromDB(Connect.getResultSet(sql));
	}
	
	private static List<User> getUsers(String sql){
		List<User> users = new ArrayList<>();
		ResultSet resultSet = Connect.getResultSet(sql);
		try{
			while (resultSet.next()){
				users.add(User.buildUserFromDB(resultSet));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return users;
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
	
	
}
