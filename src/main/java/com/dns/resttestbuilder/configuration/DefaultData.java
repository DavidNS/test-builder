package com.dns.resttestbuilder.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.data.Project;
import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.data.Workspace;
import com.dns.resttestbuilder.repository.ProjectRepository;
import com.dns.resttestbuilder.repository.WorkspaceRepository;

@Component
public class DefaultData {

	@Autowired
	ProjectRepository projectRepository;
	

	
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



	public Workspace save(Workspace workspace,List<Workspace> workspaces, User user) {
		workspace=workspaceRepository.save(workspace);
		user.setSelectWorkspaceID(workspace.getId());
		user.setWorkspaces(workspaces);
		return workspace;
	}


	public Project save(Project project,List<Project> projects, Workspace workspace) {
		project=projectRepository.save(project);
		workspace.setSelectProjectID(project.getId());
		workspace.setProjects(projects);
		return project;
	}
	
}
