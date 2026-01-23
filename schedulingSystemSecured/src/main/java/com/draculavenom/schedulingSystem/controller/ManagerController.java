package com.draculavenom.schedulingSystem.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

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

import com.draculavenom.company.CompanyName;
import com.draculavenom.company.CompanyNameRepository;
import com.draculavenom.schedulingSystem.dto.ManagerDTO;
import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.UserDTO;

@RestController
@RequestMapping("/api/v1/Manager")
public class ManagerController {
	
	@Autowired private ManagerOptionsRepository managerRepository;
	@Autowired private UserRepository repository;
	@Autowired private CompanyNameRepository companyNameRepository;
	
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('admin:read')")
	public ResponseEntity<ManagerDTO> get(@PathVariable Integer id){
		ManagerDTO manager = new ManagerDTO();
		User userManager = repository.getById(id);
		manager.setName(userManager.getFirstName() + " " + userManager.getLastName());
		companyNameRepository.findByUserId(id).ifPresent(c -> manager.setCompanyName(c.getNameCompany()));
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
		return new ResponseEntity<ManagerDTO>(manager, HttpStatusCode.valueOf(200));
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	public ResponseEntity<ManagerDTO> create(@RequestBody ManagerDTO manager){
		ManagerOptions managerOptions = new ManagerOptions(0, manager.getAdminId(), manager.getManagerId(), manager.getAmmountPaid(), manager.getActiveDate(), manager.getComments());
		managerOptions = managerRepository.save(managerOptions);
		manager.setId(managerOptions.getId());
		if(manager.getCompanyName() != null && !manager.getCompanyName().isBlank()){
			User managerUser = repository.getById(manager.getManagerId());
			CompanyName companyName = new CompanyName(manager.getCompanyName(), managerUser);
			companyNameRepository.save(companyName);
		}
		return new ResponseEntity<ManagerDTO>(manager, HttpStatusCode.valueOf(200));
	}
	
	@GetMapping("/select")
	public ResponseEntity<List<ManagerDTO>> getManagerSelect(){
		List<User> list = repository.getAllByRole(Role.MANAGER).orElseThrow();
		return new ResponseEntity<List<ManagerDTO>>(list.stream().map(u -> {
			ManagerDTO manager = new ManagerDTO();
			manager.setManagerId(u.getId());
			manager.setName(u.getCompanyName() != null 
				? u.getCompanyName().getNameCompany() 
				: "WITHOUT COMPANY");
			return manager;
		}).collect(Collectors.toList()), HttpStatusCode.valueOf(200));
	}

	@GetMapping("/{managerId}/persons")
	@PreAuthorize("hasAuthority('manager:read')")
	public List<UserDTO> getPersonsByManager(@PathVariable Integer managerId){
		List<User> users = repository.findAllByManagedBy(managerId).orElse(new ArrayList<>());
		return users.stream()
            .map(u -> UserDTO.builder()
				.id(u.getId())
				.name(u.getFirstName() + " " + u.getLastName()) 
				.email(u.getEmail())
				.phoneNumber(u.getPhoneNumber())
				.dateOfBirth(u.getDateOfBirth())
				.managedBy(u.getManagedBy())
				.role(u.getRole().name()) 
				.passwordChange(u.getPasswordChange())
				.build())
			.collect(Collectors.toList());					
	}
	
	@GetMapping("/{managerId}/options")
	@PreAuthorize("hasAuthority('admin:read')")
	public List<ManagerDTO> getManagerOptions(@PathVariable Integer managerId) {
		List<ManagerOptions> managerOptions = managerRepository.findAllByManagerId(managerId);
		return managerOptions.stream()
			.map(o -> ManagerDTO.builder()
				.id(o.getId())
				.managerId(o.getManagerId())
				.adminId(o.getUserId())
				.ammountPaid(o.getAmmountPaid())
				.activeDate(o.getActiveDate())
				.comments(o.getComments())
				.build())
			.collect(Collectors.toList());
	}
}
