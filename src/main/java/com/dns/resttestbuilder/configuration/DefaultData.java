package com.dns.resttestbuilder.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.data.Endpoint;
import com.dns.resttestbuilder.data.JSONModel;
import com.dns.resttestbuilder.data.MappedValue;
import com.dns.resttestbuilder.data.Project;
import com.dns.resttestbuilder.data.Test;
import com.dns.resttestbuilder.data.User;
import com.dns.resttestbuilder.data.Workspace;

@Component
public class DefaultData {
	

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

	public Workspace getWorkspace(Project project) {
		Workspace workspace=new Workspace();
		List<Project> projects=new ArrayList<>();
		List<MappedValue> environments=new ArrayList<>();
		List<MappedValue> globalVars=new ArrayList<>();
		projects.add(project);
		workspace.setName(DEFAULT);
		workspace.setEnvironments(environments);
		workspace.setGlobalVars(globalVars);
		workspace.setProjects(projects);
		return workspace;
	}
	
	
	public Workspace getWorkspace() {
		return getWorkspace(getProject());
	}

	
	public Project getProject() {
		Project project=new Project();
		project.setName(DEFAULT);
		List<Test> tests=new ArrayList<>();
		List<Endpoint> endpoints=new ArrayList<>();
		List<JSONModel> jsonModels=new ArrayList<>();
		project.setEndpoints(endpoints);
		project.setJsonModels(jsonModels);
		project.setTests(tests);
		return project;
	}

	
	public User getUser(String name, Project project) {
		User user=new User();
		Workspace workspace=getWorkspace(project);
		List<Workspace> workspaces=new ArrayList<>();
		MappedValue userPreferences = getUserPreferences();
		user.setName(name);
		user.setUserPreferences(userPreferences);
		user.setWorkspaces(workspaces);
		workspaces.add(workspace);
		return user;
	}


	private MappedValue getUserPreferences() {
		MappedValue userPreferences=new MappedValue();
		userPreferences.setName(DEFAULT);
		userPreferences.setKeyVsValue(new HashMap<>());
		return userPreferences;
	}
	
}
