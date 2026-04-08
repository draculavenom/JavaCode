package com.draculavenom.schedulingSystem.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.draculavenom.company.CompanyName;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	public List<Appointment> findAllByUserId(Integer id);
	
	public List<Appointment> findAllByStatusIn(List<AppointmentStatus> appointmentStatuses);

	public List<Appointment> findAllByStatusInAndDate(List<AppointmentStatus> statuses, LocalDate date);
	
	public List<Appointment> findAllByUserIdInAndDateAndStatusNot(List<Integer> userIds, LocalDate date, AppointmentStatus status);
	
	@Query("SELECT a.time FROM Appointment a WHERE a.userId IN :userIds AND a.date = :date AND a.status <> :status")
	public List<LocalTime> findTimesByUserIdInAndDateAndStatusNot(List<Integer> userIds, LocalDate date, AppointmentStatus status);

	public List<Appointment> findByManagerId(Integer managerId);

	public List<Appointment> findByManagerCompany(CompanyName company);
	
	public List<Appointment> findByManagerIdAndManagerCompany(Integer managerId, CompanyName company);

	public List<Appointment> findByManagerIdAndStatusIn(Integer managerId, List<AppointmentStatus> statuses);
}
