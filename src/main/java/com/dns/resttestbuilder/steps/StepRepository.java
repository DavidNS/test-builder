package com.dns.resttestbuilder.steps;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Long> {
	
	List<Step> findByUserID(Long userID);
}
