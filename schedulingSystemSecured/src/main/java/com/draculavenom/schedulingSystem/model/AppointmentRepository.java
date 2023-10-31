package com.draculavenom.schedulingSystem.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	public List<Appointment> findAllByUserId(Integer id);
}
