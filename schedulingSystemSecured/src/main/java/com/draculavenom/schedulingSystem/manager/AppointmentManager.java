package com.draculavenom.schedulingSystem.manager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@Controller
public class AppointmentManager {

	@Autowired private AppointmentRepository repository;
	@Autowired private UserRepository userRepository;
	
	public Appointment create(Appointment ap) throws Exception{
		List<Appointment> appointments = getAppointmentsManagedByUserId(ap.getUserId());
		if(appointments.stream().filter(a -> a.getDate().equals(ap.getDate()) && a.getTime().equals(ap.getTime())).findFirst().isEmpty()) {
			if((ap.getDate().isBefore(LocalDate.now())) || (ap.getDate().equals(LocalDate.now()) && ap.getTime().isBefore(LocalTime.now())))
				throw new Exception("You can schedule appointments in the past. Please select a valid option");
			else
				return repository.save(ap);
		}else
			throw new Exception("That slot is already been scheduled, please try again with a different slot");
	}
	
	public Appointment update(Appointment ap) throws Exception{
		List<Appointment> appointments = getAppointmentsManagedByUserId(ap.getUserId());
		if(appointments.stream().filter(a -> a.getDate().equals(ap.getDate()) && a.getTime().equals(ap.getTime())).findFirst().isEmpty()) {
			if((ap.getDate().isBefore(LocalDate.now())) || (ap.getDate().equals(LocalDate.now()) && ap.getTime().isBefore(LocalTime.now())))
				throw new Exception("You can schedule appointments in the past. Please select a valid option");
			else{
				System.out.println("llega");
				System.out.println(ap);
				Appointment a = repository.findById(ap.getId()).orElseThrow(Exception::new);
				a.setDate(ap.getDate());
				a.setStatus(ap.getStatus());
				a.setTime(ap.getTime());
				return repository.save(a);
			}
		}else
			throw new Exception("That slot is already been scheduled, please try again with a different slot");
	}
	
	public List<Appointment> getAppointmentsManagedByUserId(int userId){
		User user = userRepository.findById(userId).orElseThrow();
		List<Appointment> appointments = repository.findAll();
		if(user.getManagedBy() != null) {
			List<User> users = userRepository.findAllByManagedBy(user.getManagedBy()).orElseThrow();
			appointments = appointments.stream().filter(a -> users.stream().map(u -> u.getId()).collect(Collectors.toList()).contains(a.getUserId())).collect(Collectors.toList());			
		}
		return appointments;
	}
}
