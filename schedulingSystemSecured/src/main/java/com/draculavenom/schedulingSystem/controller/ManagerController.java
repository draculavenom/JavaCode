package com.draculavenom.schedulingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.schedulingSystem.dto.ManagerDTO;
import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@RestController
@RequestMapping("/api/v1/Manager")
public class ManagerController {
	
	@Autowired private ManagerOptionsRepository managerRepository;
	@Autowired private UserRepository repository;
	
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('admin:read')")
	public ResponseEntity<ManagerDTO> get(@PathVariable Integer id){
		ManagerDTO manager = new ManagerDTO();
		User userManager = repository.getById(id);
		manager.setName(userManager.getFirstName() + " " + userManager.getLastName());
		manager.setManagerId(id);
		List<ManagerOptions> managerOptionsList = managerRepository.findAllByManagerId(id);
		if(managerOptionsList.size() > 0) {
			ManagerOptions managerOptions = managerOptionsList.stream().sorted((m1, m2) -> m1.getId()-m2.getId()).findFirst().orElse(null);	
			System.out.println(managerOptions.toString());
			manager.setId(managerOptions.getId());
			manager.setActiveDate(managerOptions.getActiveDate());
			manager.setAdminId(managerOptions.getUserId());
			manager.setAmmountPaid(managerOptions.getAmmountPaid());
			manager.setComments(managerOptions.getComments());		
		}
		return new ResponseEntity(manager, HttpStatusCode.valueOf(200));
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	public ResponseEntity<ManagerDTO> create(@RequestBody ManagerDTO manager){
		ManagerOptions managerOptions = new ManagerOptions(0, manager.getAdminId(), manager.getManagerId(), manager.getAmmountPaid(), manager.getActiveDate(), manager.getComments());
		managerOptions = managerRepository.save(managerOptions);
		manager.setId(managerOptions.getId());
		return new ResponseEntity(manager, HttpStatusCode.valueOf(200));
	}
}
