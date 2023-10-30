package com.draculavenom.schedulingSystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.schedulingSystem.model.Person;
import com.draculavenom.schedulingSystem.model.PersonRepository;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {
	
	@Autowired private PersonRepository repository;
	
	@GetMapping("")
	public List<Person> getPersons() {
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Person getPerson(@PathVariable long id) {
		return repository.findById(id).orElse(null);
	}
	
	@PostMapping("")
	public Person createPerson(@RequestBody Person person) {
		return repository.save(person);
	}
	
	@PutMapping("/{id}")
	public Person updatePerson(@PathVariable long id, @RequestBody Person person) {
		return repository.findById(id)
				.map((newPerson) -> repository.save(new Person(person.getTypeOfUser(), person.getName(), person.getLastName(), person.getUsername(), person.getEmail(), person.getPhoneNumber(), person.getDateOfBirth())))
				.orElseGet(() -> repository.save(person));
	}
	
	@DeleteMapping("/{id}")
	public void deletePerson(@PathVariable long id) {
		repository.deleteById(id);
	}
}
