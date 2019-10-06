package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
	
	@RequestMapping("/getUserById")
	public User getUserById(@RequestParam(value = "id", defaultValue = "1") int id) {
		return User.getUserById(id);
	}
	
	@RequestMapping("/auth")
	public boolean checkUserLoginData(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password, HttpSession session) {
		User user = User.getUserByUserName(name);
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
	
	@RequestMapping("/logout")
	public void logout(HttpSession session) {
		session.removeAttribute("user");
	}
	
	
}
