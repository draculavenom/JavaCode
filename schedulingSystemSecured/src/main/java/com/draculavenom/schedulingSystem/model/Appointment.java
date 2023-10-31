package com.draculavenom.schedulingSystem.model;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class Appointment {
	private @Id @GeneratedValue Integer id;
	private Integer userId;
	private LocalDate date;
	private LocalTime time;
	private AppointmentStatus status;
	
	public Appointment cancelAppointment() {
		status = AppointmentStatus.CANCELLED;
		return this;
	}
}
