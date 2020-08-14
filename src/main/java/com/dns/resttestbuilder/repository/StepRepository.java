package com.dns.resttestbuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.Step;

public interface StepRepository extends JpaRepository<Step, Long> {
	
	List<Step> findByUserID(Long userID);
}
