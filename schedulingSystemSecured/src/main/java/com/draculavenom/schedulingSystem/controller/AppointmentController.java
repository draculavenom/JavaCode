package com.draculavenom.schedulingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.schedulingSystem.dto.AppointmentResponseDTO;
import com.draculavenom.schedulingSystem.manager.AppointmentManager;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;
import com.draculavenom.schedulingSystem.utilities.EmailService;
import com.draculavenom.security.user.User;


@RestController
@RequestMapping("/api/v1/Appointments")
public class AppointmentController {
	
	@Autowired private AppointmentRepository repository;
	@Autowired private AppointmentManager manager;
	@Autowired private EmailService emailService;
	@Autowired private AppointmentService appointmentService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('admin:read')")
	public ResponseEntity<List<AppointmentResponseDTO>> getAll(){
		return ResponseEntity.ok(appointmentService.getAllWithCompany());
	}
	
	@GetMapping("/byUserId/{id}")
	public ResponseEntity<List<AppointmentResponseDTO>> getAllByUserId(@PathVariable Integer id){
		List<Appointment> appointments = repository.findAllByUserId(id);
		return ResponseEntity.ok(manager.toDTOList(appointments));
	}
	
	@GetMapping("/byManagerId/{id}")
	@PreAuthorize("hasAuthority('manager:read')")
	public ResponseEntity<List<AppointmentResponseDTO>> getAllByManagerId(@PathVariable Integer id){
		List<Appointment> appointments = repository.findByManagerId(id);
		return ResponseEntity.ok(manager.toDTOList(appointments));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<AppointmentResponseDTO> get(@PathVariable Integer id) {
		Appointment ap = repository.findById(id).orElse(null);
		return ResponseEntity.ok(manager.toDTO(ap));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Appointment ap) {
		try {
			Appointment saved = manager.create(ap);
			return ResponseEntity.ok(manager.toDTO(saved));
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(400));
		}
	}
	
	@PutMapping("")
	public ResponseEntity<?> update(@RequestBody Appointment ap) {
		try {
			Appointment saved = manager.update(ap);
			return ResponseEntity.ok(manager.toDTO(saved));
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(400));
		}
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<AppointmentResponseDTO> cancel(@PathVariable Integer id, @RequestParam String comment) {
		Appointment ap = manager.getAppointmentAndCancelIt(id, comment);
		return ResponseEntity.ok(manager.toDTO(ap));
	}
	
	@PutMapping("/updateStatus")
	public ResponseEntity<AppointmentResponseDTO> updateStatus(@RequestBody Appointment ap) {
		Appointment updated = manager.updateStatus(ap.getId(), ap.getStatus(), ap.getComment());
		return ResponseEntity.ok(manager.toDTO(updated));
	}

	@GetMapping("/company")
	@PreAuthorize("hasAuthority('owner:read')")
	public ResponseEntity<List<AppointmentResponseDTO>> getCompanyAppointments(){
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Appointment> appointments = repository.findByManagerCompany(currentUser.getCompany());
		return ResponseEntity.ok(manager.toDTOList(appointments));
	}

	@GetMapping("/company/manager/{managerId}")
	@PreAuthorize("hasAuthority('owner:read')")
	public ResponseEntity<List<AppointmentResponseDTO>> getByManager(@PathVariable Integer managerId){
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Appointment> appointments = repository.findByManagerIdAndManagerCompany(managerId, currentUser.getCompany());
		return ResponseEntity.ok(manager.toDTOList(appointments));
	}
	
	@GetMapping("/email")
	public ResponseEntity<Boolean> sendEmail(){
		emailService.sendSimpleMessage("antunez.jmf@gmail.com", "Test", "This is just a test to make sure the email service is working.");
		return new ResponseEntity<Boolean>(true, HttpStatusCode.valueOf(200));
	}
}
