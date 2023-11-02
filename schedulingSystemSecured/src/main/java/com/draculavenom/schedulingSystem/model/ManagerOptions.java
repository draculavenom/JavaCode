package com.draculavenom.schedulingSystem.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class ManagerOptions {
	@Id @GeneratedValue private int id;
	private int userId;
	private int managerId;
	private double ammountPaid;
	private LocalDate activeDate;
	private String comments;
	
	/*
	 Ideas: 
	 	Have the custom design here like the background image, leftbar image maybe a logo to present somewhere in the screen.
	 	If we include special features, like birthday promotions or things like that, we could also have them here.
	 	Special feature: send a message to the manager when a user has a birthday that day.
	 	
	 */
}
