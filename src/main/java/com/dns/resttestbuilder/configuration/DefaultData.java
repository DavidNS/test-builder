package com.dns.resttestbuilder.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.data.Profile;
import com.dns.resttestbuilder.data.Project;
import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.data.Workspace;
import com.dns.resttestbuilder.repository.ProfileRepository;
import com.dns.resttestbuilder.repository.ProjectRepository;
import com.dns.resttestbuilder.repository.WorkspaceRepository;

@Component
public class DefaultData {

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	WorkspaceRepository workspaceRepository;
	

	private static final String DEFAULT = "Default";

	
	public ArrayList<Project> getProjects(Project project) {
		ArrayList<Project> projects=new ArrayList<>();
		projects.add(project);
		return projects;
	}

	
	public ArrayList<Workspace> getWorkspaces(Workspace workspace) {
		ArrayList<Workspace> workspaces=new ArrayList<>();
		workspaces.add(workspace);
		return workspaces;
	}

	
	public ArrayList<Profile> getProfiles(Profile profile) {
		ArrayList<Profile> profiles=new ArrayList<>();
		profiles.add(profile);
		return profiles;
	}

	
	public Profile getProfile() {
		Profile profile=new Profile();
		profile.setName(DEFAULT);
		return profile;
	}

	
	public Workspace getWorkspace() {
		Workspace workspace=new Workspace();
		workspace.setName(DEFAULT);
		return workspace;
	}

	
	public Project getProject() {
		Project project=new Project();
		project.setName(DEFAULT);
		return project;
	}

	
	public User getUser(String name ) {
		User user=new User();
		user.setName(name);

		return user;
	}
	

	public Profile save(Profile profile,List<Profile> profiles, User user) {
		profile=profileRepository.save(profile);
		user.setSelectProfileID(profile.getId());
		user.setProfiles(profiles);
		return profile;
	}


	public Workspace save(Workspace workspace,List<Workspace> workspaces, Profile profile) {
		workspace=workspaceRepository.save(workspace);
		profile.setSelectWorkspaceID(workspace.getId());
		profile.setWorkspaces(workspaces);
		return workspace;
	}


	public Project save(Project project,List<Project> projects, Workspace workspace) {
		project=projectRepository.save(project);
		workspace.setSelectProjectID(project.getId());
		workspace.setProjects(projects);
		return project;
	}
	
}
