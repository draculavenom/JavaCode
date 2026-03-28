package com.draculavenom.schedulingSystem.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	public List<Appointment> findAllByUserId(Integer id);
	
	public List<Appointment> findAllByStatusIn(List<AppointmentStatus> appointmentStatuses);

	public List<Appointment> findAllByStatusInAndDate(List<AppointmentStatus> statuses, LocalDate date);
	
	public List<Appointment> findAllByUserIdInAndDateAndStatusNot(List<Integer> userIds, LocalDate date, AppointmentStatus status);

	List<Appointment> findAllByUserIdAndStatusIn(Integer userId, List<AppointmentStatus> statuses);
	
	@Query("SELECT a.time FROM Appointment a WHERE a.userId IN :userIds AND a.date = :date AND a.status <> :status")
	public List<LocalTime> findTimesByUserIdInAndDateAndStatusNot(List<Integer> userIds, LocalDate date, AppointmentStatus status);
}
