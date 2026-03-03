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
	private String companyName;
	private Integer managerId;
	private Integer adminId;
	private Integer sellerId;
	private String sellerName;
	private double ammountPaid;
	private LocalDate activeDate;
	private String comments;
	
}
