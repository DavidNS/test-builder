package com.dns.resttestbuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	List<User> findByName(String name);
	
}
