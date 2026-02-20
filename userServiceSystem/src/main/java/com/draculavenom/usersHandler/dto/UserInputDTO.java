package com.draculavenom.usersHandler.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDTO {
	private int id;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String phoneNumber;
	private LocalDate dateOfBirth;
	private int managedBy;
	private String role;
}
