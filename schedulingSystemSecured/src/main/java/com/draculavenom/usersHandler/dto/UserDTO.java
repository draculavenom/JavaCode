package com.draculavenom.usersHandler.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Integer id;
	private String email;
	private String name;
	private String phoneNumber;
	private LocalDate dateOfBirth;
	private Integer managedBy;
	private String role;
}
