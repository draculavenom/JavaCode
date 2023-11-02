package com.draculavenom.schedulingSystem.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerOptionsRepository extends JpaRepository<ManagerOptions, Integer>{
	//I need a method that gives back the latest ManagerOption for an specific managerId sorting by Id
	//public ManagerOptions findTopbyManagerIdOrderByIdDesc(int id);
	
	public List<ManagerOptions> findAllByManagerId(int id);
}
