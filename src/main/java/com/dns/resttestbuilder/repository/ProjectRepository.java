package com.dns.resttestbuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	
	List<Project> findByUserID(Long userID);
	
}
