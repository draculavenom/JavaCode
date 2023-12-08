package com.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
	private long id;
	private String username;
	private String password;
	private String name;
	private String lastName;
	private Date dateOfBirth;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return "ID: " + id + "\n"
				+ "Username: " + username + "\n"
				+ "Password: " + password + "\n"
				+ "Name: " + name + "\n"
				+ "Last Name: " + lastName + "\n"
				+ "Date of Birth: " + formatter.format(dateOfBirth) + "\n"
				;
	}
}
