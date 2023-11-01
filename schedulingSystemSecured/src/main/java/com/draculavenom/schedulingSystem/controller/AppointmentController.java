package com.draculavenom.schedulingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.schedulingSystem.manager.AppointmentManager;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;


@RestController
@RequestMapping("/api/v1/Appointments")
public class AppointmentController {
	
	@Autowired private AppointmentRepository repository;
	@Autowired private AppointmentManager manager;
	
	@GetMapping
	public ResponseEntity<List<Appointment>> getAll(){
		return new ResponseEntity<List<Appointment>>(repository.findAll(), HttpStatusCode.valueOf(200));
	}
	
	@GetMapping("/byUserId/{id}")
	public ResponseEntity<List<Appointment>> getAllByUserId(@PathVariable Integer id){
		return new ResponseEntity<List<Appointment>>(repository.findAllByUserId(id), HttpStatusCode.valueOf(200));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Appointment> get(@PathVariable Integer id) {
		return new ResponseEntity<Appointment>(repository.findById(id).orElse(null), HttpStatusCode.valueOf(200));
	}
	
	@PostMapping
	public ResponseEntity<Appointment> create(@RequestBody Appointment ap) {
		try {
			return new ResponseEntity<Appointment>(manager.create(ap), HttpStatusCode.valueOf(200));
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(400));
		}
	}
	
	@PutMapping("")
	public ResponseEntity<Appointment> update(@RequestBody Appointment ap) {
		try {
			return new ResponseEntity<Appointment>(manager.update(ap), HttpStatusCode.valueOf(200));
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(400));
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Appointment> cancel(@PathVariable Integer id) {
		return new ResponseEntity<Appointment>(manager.getAppointmentAndCancelIt(id), HttpStatusCode.valueOf(200));
	}
}
