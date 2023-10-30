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
public class UserDTO {
	private int id;
	private String email;
	private String name;
	private String phoneNumber;
	private LocalDate dateOfBirth;
	private int managedBy;
}
