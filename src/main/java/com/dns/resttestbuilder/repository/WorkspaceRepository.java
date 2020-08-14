package com.dns.resttestbuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>{

	List<Workspace> findByUserID(Long userID);
	
}
