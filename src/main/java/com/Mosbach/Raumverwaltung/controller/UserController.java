package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.Helper.IntBoolHelper;
import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
//	CREATE
	@RequestMapping(method = RequestMethod.POST, path = "/user")
	public User createUser(@RequestParam(value = "firstName", required = true) String firstName,
						   @RequestParam(value = "secondName", required = true) String lastName,
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
		return UserDao.createUser(firstName, lastName, mail, userName, booladmin, password);
	}
	
//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/user")
	public User getUser(@RequestParam(value = "id", required = false) Integer id,
						HttpSession session) {
		if (id == null) id = (Integer) session.getAttribute("user");
		return UserDao.getUserById(id);
	}
	
//	READ ALL
	@RequestMapping(method = RequestMethod.GET, path = "/users")
	public List<User> getUsers() {
		return UserDao.getUsers();
	}
	
	
	//	UPDATE
	@RequestMapping(method = RequestMethod.PUT, path = "/user")
	public User updateUser(@RequestParam(value = "id", required = true) int id,
						   @RequestParam(value = "firstName", required = true) String firstName,
						   @RequestParam(value = "secondName", required = true) String lastName,
						   @RequestParam(value = "mail", required = true) String mail,
						   @RequestParam(value = "userName", required = true) String userName,
						   @RequestParam(value = "admin", required = false, defaultValue = "0") int admin,
						   @RequestParam(value = "password", required = true) String password){
		boolean booladmin = IntBoolHelper.intToBool(admin);
		
		return UserDao.updateUser(id, firstName, lastName, mail, userName, booladmin, password);
	}
	
	
}
