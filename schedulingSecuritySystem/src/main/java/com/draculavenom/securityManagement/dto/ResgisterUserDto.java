package com.draculavenom.securityManagement.dto;

public class ResgisterUserDto extends UserDto{
	protected String lastName;
	protected String email;
	public ResgisterUserDto() {}
	public ResgisterUserDto(String name, String username, String password, String lastName, String email) {
		super(name, username, password);
		this.lastName = lastName;
		this.email = email;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "ResgisterUserDto [name=" + name + ", username=" + username + ", password=" + password + ", lastName=" 
				+ lastName + ", email=" + email + "]";
	}
}
