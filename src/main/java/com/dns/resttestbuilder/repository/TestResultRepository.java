package com.dns.resttestbuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
	
	List<TestResult> findByUserID(Long userID);
}

