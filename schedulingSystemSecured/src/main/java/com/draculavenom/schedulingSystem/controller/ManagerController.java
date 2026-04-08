package com.draculavenom.schedulingSystem.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.company.CompanyName;
import com.draculavenom.company.CompanyNameRepository;
import com.draculavenom.schedulingSystem.dto.CompanyNameDTO;
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
	@Autowired private ManagerOptionsService managerOptsService;
	
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('admin:read')")
	public ResponseEntity<ManagerDTO> get(@PathVariable Integer id){
		ManagerDTO dto = new ManagerDTO();
		CompanyName company = companyNameRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Company not found"));
		dto.setCompanyId(company.getId());
    	dto.setCompanyName(company.getNameCompany());
		List<ManagerOptions> options = managerRepository.findAllByCompanyId(id);
		
		if(!options.isEmpty()) {
			ManagerOptions latest = options.stream().sorted((m1, m2) -> m1.getId()-m2.getId()).findFirst().orElse(null);	
			System.out.println(latest.toString());
			dto.setId(latest.getId());
			dto.setActiveDate(latest.getActiveDate());
			dto.setAdminId(latest.getUserId());
			dto.setCompanyId(company.getId());
			dto.setAmmountPaid(latest.getAmmountPaid());
			dto.setComments(latest.getComments());		
			
		}
		return new ResponseEntity<ManagerDTO>(dto, HttpStatusCode.valueOf(200));
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	public ResponseEntity<ManagerDTO> create(@RequestBody ManagerDTO manager){
		if(manager.getCompanyId() == null){
			throw new RuntimeException("Company is required");
		}
		ManagerOptions managerOptions = new ManagerOptions(0, manager.getAdminId(), manager.getCompanyId(), manager.getSellerId(), manager.getAmmountPaid(), manager.getActiveDate(), manager.getComments());
		managerOptions = managerRepository.save(managerOptions);
		manager.setId(managerOptions.getId());
		return new ResponseEntity<ManagerDTO>(manager, HttpStatusCode.valueOf(200));
	}

	@PutMapping("/company/{companyId}")
	@PreAuthorize("hasAuthority('admin:update')")
	public ResponseEntity<Void> updateCompany(@PathVariable Integer companyId, @RequestBody CompanyNameDTO companyDTO) {
		if(companyDTO.getCompanyName() == null || companyDTO.getCompanyName().isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		CompanyName companyName = companyNameRepository.findById(companyId)
			.orElseThrow(() -> new RuntimeException("Company not found"));

		companyName.setNameCompany(companyDTO.getCompanyName());
		//companyName.setAdmin(manager);
		companyName.setMaxManager(companyDTO.getMaxManager());
	
		companyNameRepository.save(companyName);

		return ResponseEntity.ok().build();
	}
	
	
	@GetMapping("/select")
	public ResponseEntity<List<ManagerDTO>> getCompanySelect(){
		List<CompanyName> companies = companyNameRepository.findAll();
		List<ManagerDTO> result = companies.stream().map(u -> {
			ManagerDTO dto = new ManagerDTO();
			dto.setCompanyId(u.getId());
			/*dto.setName(u.getCompany() != null 
				? u.getCompany().getNameCompany() 
				: "WITHOUT COMPANY");]*/
			dto.setName(u.getNameCompany());
			return dto;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/persons")
	@PreAuthorize("hasAnyAuthority('manager:read', 'owner:read')")
	public List<UserDTO> getPersons(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) auth.getPrincipal();
		List<User> users = managerOptsService.getPersons(currentUser);
		return users.stream()
            .map(u -> UserDTO.builder()
				.id(u.getId())
				.name(u.getFirstName() + " " + u.getLastName()) 
				.email(u.getEmail())
				.phoneNumber(u.getPhoneNumber())
				.dateOfBirth(u.getDateOfBirth())
				.company(u.getCompany().getId())
				.role(u.getRole().name()) 
				.passwordChange(u.getPasswordChange())
				.build())
			.collect(Collectors.toList());					
	}
	
	@GetMapping("/{managerId}/options")
	@PreAuthorize("hasAuthority('admin:read')")
	public List<ManagerDTO> getManagerOptions(@PathVariable Integer managerId) {
		User manager = repository.getById(managerId);
		if(manager.getCompany() == null){
			return List.of();
		}
		List<ManagerOptions> managerOptions = managerRepository.findAllByCompanyId(manager.getCompany().getId());
		return managerOptions.stream()
			.map(o -> {
				String sellerName = null;

				if(o.getSellerId() != null){
					User seller = repository.getById(o.getSellerId());
					sellerName = seller.getFirstName() + " " + seller.getLastName();
				}
				
				return ManagerDTO.builder()
				.id(o.getId())
				.adminId(o.getUserId())
				.companyId(o.getCompanyId())
				.sellerId(o.getSellerId())
				.sellerName(sellerName)
				.ammountPaid(o.getAmmountPaid())
				.activeDate(o.getActiveDate())
				.comments(o.getComments())
				.build();
			})
			.collect(Collectors.toList());
	}

	@GetMapping("/company")
	@PreAuthorize("hasAuthority('admin:read')")
	public ResponseEntity<List<CompanyNameDTO>> getManagerCompany() {
		List<CompanyNameDTO> result = repository.getAllByRole(Role.MANAGER)
			.orElseThrow()
			.stream()
			.filter(u -> u.getCompany() != null)
			.map(u -> {
				CompanyNameDTO companyDTO = new CompanyNameDTO();
				companyDTO.setManagerId(u.getId());
				companyDTO.setCompanyName(u.getCompany().getNameCompany());
				return companyDTO;
			})
			.collect(Collectors.toList());
			
		return ResponseEntity.ok(result);
	}
	
}
