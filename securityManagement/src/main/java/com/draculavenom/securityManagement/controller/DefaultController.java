package com.draculavenom.securityManagement.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController implements ErrorController{
	@GetMapping("/")
	public String mainPage() {
		return "<h1>Welcome</h1>";
	}
	
	@GetMapping("/error")
	public String errorPage(){
		return "<p>I'm sorry but the page you are looking for doesn't exist.";
	}
}
