package com.dns.resttestbuilder.tests;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
	
	List<Test> findByUserID(String userID);
}
