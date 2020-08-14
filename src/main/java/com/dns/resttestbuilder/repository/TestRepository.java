package com.dns.resttestbuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
	
	List<Test> findByUserID(Long userID);
}
