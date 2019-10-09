package com.Mosbach.Raumverwaltung.controller;

import com.Mosbach.Raumverwaltung.domain.IntBoolHelper;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class UserController {

//	todo: Allgemeine Absprache: Wo ist es sinnvoller die Daten zu validieren?
//	Aktuell wird im Controller über required Felder definiert was zwingend benötigt wird
//	Die Prüfung ob korrekte Fremdschlüssel übergeben wurde findet aber dann wieder in den Domain Klassen statt
	
	/*
	Es wird CRUD implementiert
	Ausname: Delete wird weggelassen
	Unsicherheit ob über Datenbankfeld oder vollständige Löschung
	bei vollständiger Löschung würden die Fremdschlüssel verloren gehen
	Kompromiss: Alle Daten (außer der ID) werden überschrieben mit DELETED
	 */
	
//	CREATE
	@RequestMapping(method = RequestMethod.POST, path = "/createUser")
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
//		todo: was passiert wenn ich beim admin einen String übergebe? Prüfen und ggf. beheben
		boolean booladmin = IntBoolHelper.intToBool(admin);
		return User.createUser(firstName, lastName, mail, userName, booladmin, password);
	}
	
//	READ
	@RequestMapping(method = RequestMethod.GET, path = "/getUser")
	public User getUser(@RequestParam(value = "id", defaultValue = "1") int id) {
		return User.getUserById(id);
	}
	
//	READ ALL
	@RequestMapping(method = RequestMethod.GET, path = "/getUsers")
	public List<User> getUsers(@RequestParam(value = "id", defaultValue = "1") int id) {
		return User.getUsers();
	}
	
	
	//	UPDATE
	@RequestMapping(method = RequestMethod.PUT, path = "/updateUser")
	public User updateUser(@RequestParam(value = "id", required = true) int id,
						   @RequestParam(value = "firstName", required = true) String firstName,
						   @RequestParam(value = "secondName", required = true) String lastName,
						   @RequestParam(value = "mail", required = true) String mail,
						   @RequestParam(value = "userName", required = true) String userName,
						   @RequestParam(value = "admin", required = false, defaultValue = "0") int admin,
						   @RequestParam(value = "password", required = true) String password){
//		todo: was passiert wenn ich beim admin einen String übergebe? Prüfen und ggf. beheben
		boolean booladmin = IntBoolHelper.intToBool(admin);
		
		return User.updateUser(id, firstName, lastName, mail, userName, booladmin, password);
	}
	
	
}
