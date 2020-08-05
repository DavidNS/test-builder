package com.dns.resttestbuilder.gui.workspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.repository.ProfileRepository;
import com.dns.resttestbuilder.repository.ProjectRepository;
import com.dns.resttestbuilder.repository.UserRepository;
import com.dns.resttestbuilder.repository.WorkspaceRepository;

@Component
@Scope("Singleton")
public class WorskpaceModel {


	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	WorkspaceRepository workspaceRepository;

	@Autowired
	UserRepository userRepository;
	
}
