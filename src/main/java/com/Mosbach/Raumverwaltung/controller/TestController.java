package com.Mosbach.Raumverwaltung.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.Mosbach.Raumverwaltung.Greeting;
import com.Mosbach.Raumverwaltung.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class TestController {
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name, HttpSession session) {
		System.out.println("Gespeicherte NutzerID in session: " + session.getAttribute("user"));
		return new Greeting(counter.incrementAndGet(),
				String.format(template, name));
	}
	
	
	
	
	
	
}