package com.dns.resttestbuilder.results;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {
	
	List<Result> findByUserID(String userID);
}
