package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.DAO.TokenDao;
import com.Mosbach.Raumverwaltung.DAO.UserDao;
import com.Mosbach.Raumverwaltung.Helper.TokenJSON;
import com.Mosbach.Raumverwaltung.domain.Token;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	@PostMapping(path = "/auth")
	public Token checkUserLoginData(@RequestParam(value = "username", required = true) String name,
									@RequestParam(value = "password", required = true) String password,
									HttpSession session) {
		User user = UserDao.getUserByUserName(name);
		System.out.println("Anfrage:" +
				"\nUser: " + user +
				"\nPassword: " + password + "\n");

		if (user == null) return null;
		if (user.getPassword().equals(password)) {
			session.setAttribute("user", user.getId());
			System.out.println("Gespeicherte NutzerID in session: " + session.getAttribute("user"));
			return Token.registerToken(user);
		}
		System.out.println("Gespeicherte NutzerID in session: " + session.getAttribute("user"));
		return null;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/logout")
	public boolean logout(@RequestParam(value = "token") String tokenString,
						  HttpSession session) {
		System.out.println("SessionUser: " + session.getAttribute("user"));
		session.removeAttribute("user");
		Token token = TokenDao.getTokenFromTokenString(tokenString);
		TokenDao.updateToken(token.getUserId(), token.getToken(), LocalDateTime.now());
		return true;
	}
	
	
}
