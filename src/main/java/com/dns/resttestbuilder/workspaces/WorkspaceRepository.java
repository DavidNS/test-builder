package com.dns.resttestbuilder.workspaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>{

	List<Workspace> findByUserID(String userID);
	
}
