package com.draculavenom.schedulingSystem.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	public List<Appointment> findAllByUserId(Integer id);
	
	public List<Appointment> findAllByStatusIn(List<AppointmentStatus> appointmentStatuses);

	public List<Appointment> findAllByStatusInAndDate(List<AppointmentStatus> statuses, LocalDate date);
}
