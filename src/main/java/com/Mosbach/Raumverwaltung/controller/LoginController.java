package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
	
	@RequestMapping(method = RequestMethod.POST, path = "/auth")
	public boolean checkUserLoginData(@RequestParam(value = "userName", required = true) String name,
									  @RequestParam(value = "password", required = true) String password,
									  HttpSession session) {
		User user = UserDao.getUserByUserName(name);
		System.out.println("Anfrage:" +
				"\nUser: " + user +
				"\nPassword: " + password + "\n");
		if (user == null) return false;
		if (user.getPassword().equals(password)) {
			session.setAttribute("user", user.getId());
			System.out.println("Gespeicherte NutzerID in session: " + session.getAttribute("user"));
			return true;
		}
		System.out.println("Gespeicherte NutzerID in session: " + session.getAttribute("user"));
		return false;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/logout")
	public void logout(HttpSession session) {
		session.removeAttribute("user");
	}
	
	
}
