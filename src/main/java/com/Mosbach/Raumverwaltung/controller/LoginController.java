package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

//	todo hendrik: api abklären ob es nicht sinnvoller ist den User zu übertragen
	@PostMapping(path = "/auth")
	public boolean checkUserLoginData(
	                  @RequestParam(value = "username", required = true) String name,
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
