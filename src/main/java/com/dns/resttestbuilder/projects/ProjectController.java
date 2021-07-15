package com.dns.resttestbuilder.projects;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.steps.validation.DefaultData;
import com.dns.resttestbuilder.users.UserController;
import com.dns.resttestbuilder.workspaces.Workspace;
import com.dns.resttestbuilder.workspaces.WorkspaceController;

@RestController
@RequestMapping
public class ProjectController {

	@Autowired
	DefaultData defaultData;
	
	@Autowired
	ProjectRepository repository;
	
	
	@Autowired
	WorkspaceController workspaceController;

	@Autowired
	UserController userController;
	
	public Project saveFull(Project pj) {
		return repository.save(pj);
	}

	@GetMapping("/projects")
	List<Project> getAll(Principal principal) {
		return repository.findByUserID(principal.getName());
	}
	
	@GetMapping("/projects/{id}")
	public Project getOrThrow(Principal principal,@PathVariable Long id)  {
		return repository.findById(id).map((pj)->{
			defaultData.handleNotValidUserID(Project.class, id, pj.getUserID(), principal.getName());
			return pj;
		}).orElseThrow(defaultData.getNotFoundSupplier(Project.class, id));
	}
	
	@PutMapping("/projects/{id}")
	Project replace(Principal principal, @PathVariable Long id, @RequestBody Project project) {
		return repository.findById(id).map(pj -> {
			return repository.save(handle(principal,pj,project));
		}).orElseThrow(defaultData.getNotFoundSupplier(Project.class, id));
	}
	
	@PostMapping("/workspaces/{workspaceID}/projects")
	Project newItem(Principal principal, @PathVariable Long workspaceID,@RequestBody Project project) {
		Workspace workspace=workspaceController.getOrThrow(principal,workspaceID);
		project=repository.save(handle(principal, new Project(), project));
		workspace.getProjects().add(project);
		workspaceController.saveFull(workspace);
		return project;
	}
	
	@DeleteMapping("/workspaces/{workspaceID}/projects/{id}")
	void delete(Principal principal,@PathVariable Long workspaceID,@PathVariable Long id)  {
		Workspace wk=workspaceController.getOrThrow(principal, workspaceID);
		wk.getProjects().removeIf((pj)->{return pj.getId().equals(id);});
		workspaceController.saveFull(wk);
	}

	
	public Project handle(Principal principal, Project dataToSave, Project newData) {
		String userID=principal.getName();
		defaultData.handleCreatingObjectBeforeCreatingUser(userID);
		defaultData.handleNotValidUserID(Project.class, dataToSave.getId(), dataToSave.getUserID(), userID);
		
		dataToSave.setName(newData.getName());
		dataToSave.setUserID(userID);
		
		defaultData.handleNullProperty(dataToSave::getTests, ArrayList::new, dataToSave::setTests);
		
		return dataToSave;
	}


}
