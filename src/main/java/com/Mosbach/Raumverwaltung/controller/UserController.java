package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.DAO.TokenDao;
import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.domain.Token;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
//	CREATE
	@RequestMapping(method = RequestMethod.POST, path = "/user")
	public Token createUser(@RequestParam(value = "firstName", required = true) String firstName,
						   @RequestParam(value = "secondName", required = true) String secondName,
						   @RequestParam(value = "mail", required = true) String mail,
						   @RequestParam(value = "userName", required = true) String userName,
						   @RequestParam(value = "admin", required = false, defaultValue = "0") int admin,
						   @RequestParam(value = "password", required = true) String password){
		/*
		Info:
		wenn ein required value nicht übermittelt wird wird Statuscode 400 geworfen
		message: Required parameter XYZ is not present
		 */
		boolean booladmin = IntBoolHelper.intToBool(admin);
		User user = UserDao.createUser(firstName, secondName, mail, userName, booladmin, password);
		if (user == null) return null;
		else return Token.registerToken(user);
	}
	
//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/user")
	public User getUser(@RequestParam(value = "token", required = true) String tokenString) {
		Token token = TokenDao.getTokenFromTokenString(tokenString);
		if (token.isValid()){
			return UserDao.getUserById(token.getUserId());
		}
		else return null;
	}
	
//	READ ALL
	@RequestMapping(method = RequestMethod.GET, path = "/users")
	public List<User> getUsers() {
		return UserDao.getUsers();
	}
	
	
	//	UPDATE
	@RequestMapping(method = RequestMethod.PUT, path = "/user")
	public User updateUser(@RequestParam(value = "token", required = true) String tokenString,
						   @RequestParam(value = "userId", required = true) int id,
						   @RequestParam(value = "firstName", required = true) String firstName,
						   @RequestParam(value = "secondName", required = true) String lastName,
						   @RequestParam(value = "mail", required = true) String mail,
						   @RequestParam(value = "userName", required = true) String userName,
						   @RequestParam(value = "admin", required = false, defaultValue = "0") int admin,
						   @RequestParam(value = "password", required = true) String password){
		boolean booladmin = IntBoolHelper.intToBool(admin);
		Token token = TokenDao.getTokenFromTokenString(tokenString);
		if (token.isValid()){
			return UserDao.updateUser(id, firstName, lastName, mail, userName, booladmin, password);
		}
		else return null;
	}
	
	
}
