package com.dns.resttestbuilder.testresults;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
	
	List<TestResult> findByUserID(Long userID);
}

