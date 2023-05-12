package com.draculavenom.securityManagement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;

@Entity
public class Users {
	private @Id @GeneratedValue Long id;
	private String name;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private boolean enabled;
	private LocalDate creationDate;
	private LocalDate updateDate;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Roles> roles = new ArrayList<>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public LocalDate getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDate getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(LocalDate updateDate) {
		this.updateDate = updateDate;
	}
	public List<Roles> getRoles() {
		return roles;
	}
	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
	public Users(Long id, String name, String lastName, String username, String password, String email, boolean enabled,
			LocalDate creationDate, LocalDate updateDate) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
	}
	public Users(String name, String lastName, String username, String password, String email) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = true;
		this.creationDate = LocalDate.now();
		this.updateDate = LocalDate.now();
	}
	public Users(String name, String lastName, String username, String password, String email, LocalDate updateDate) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = true;
		this.creationDate = LocalDate.now();
		this.updateDate = updateDate;
	}
	public Users() {
		this.enabled = true;
		this.creationDate = LocalDate.now();
		this.updateDate = LocalDate.now();
	}
	@Override
	public String toString() {
		return "Users [id=" + id + ", name=" + name + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", email=" + email + ", enabled=" + enabled + ", creationDate="
				+ creationDate + ", updateDate=" + updateDate + "]";
	}
}
