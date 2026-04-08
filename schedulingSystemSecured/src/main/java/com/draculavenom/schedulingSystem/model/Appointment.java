package com.draculavenom.schedulingSystem.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.draculavenom.security.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Appointment {
	private @Id @GeneratedValue Integer id;
	private Integer userId;
	@ManyToOne
	@JoinColumn(name = "manager_id")
	private User manager;
	private LocalDate date;
	private LocalTime time;
	private AppointmentStatus status;
	private String comment;
	
	public Appointment cancelAppointment() {
		status = AppointmentStatus.CANCELLED;
		return this;
	}
}
