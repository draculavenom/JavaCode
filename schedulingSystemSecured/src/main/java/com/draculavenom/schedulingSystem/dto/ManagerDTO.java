package com.draculavenom.schedulingSystem.dto;

import java.time.LocalDate;

import com.draculavenom.usersHandler.dto.UserDTO;

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
public class ManagerDTO {
	private Integer id;
	private String name;
	private Integer managerId;
	private Integer adminId;
	private double ammountPaid;
	private LocalDate activeDate;
	private String comments;
	
}
