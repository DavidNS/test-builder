package com.dns.resttestbuilder.projects;

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
@RequestMapping(path="/users/{userID}")
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
	List<Project> getAll(@PathVariable Long userID) {
		return repository.findByUserID(userID);
	}
	
	@GetMapping("/projects/{id}")
	public Project getOrThrow(@PathVariable Long userID,@PathVariable Long id)  {
		return repository.findById(id).map((pj)->{
			defaultData.handleNotValidUserID(Project.class, id, pj.getUserID(), userID);
			return pj;
		}).orElseThrow(defaultData.getNotFoundSupplier(Project.class, id));
	}
	
	@PutMapping("/projects/{id}")
	Project replace(@PathVariable Long userID, @PathVariable Long id, @RequestBody Project project) {
		return repository.findById(id).map(pj -> {
			return repository.save(handle(userID,pj,project));
		}).orElseThrow(defaultData.getNotFoundSupplier(Project.class, id));
	}
	
	@PostMapping("/workspaces/{workspaceID}/projects")
	Project newItem(@PathVariable Long userID, @PathVariable Long workspaceID,@RequestBody Project project) {
		Workspace workspace=workspaceController.getOrThrow(userID,workspaceID);
		project=repository.save(handle(userID, new Project(), project));
		workspace.getProjects().add(project);
		workspaceController.saveFull(workspace);
		return project;
	}
	
	@DeleteMapping("/workspaces/{workspaceID}/projects/{id}")
	void delete(@PathVariable Long userID,@PathVariable Long workspaceID,@PathVariable Long id)  {
		Workspace wk=workspaceController.getOrThrow(userID, workspaceID);
		wk.getProjects().removeIf((pj)->{return pj.getId().equals(id);});
		workspaceController.saveFull(wk);
	}

	
	public Project handle(Long userID, Project dataToSave, Project newData) {
		defaultData.handleCreatingObjectBeforeCreatingUser(userID);
		defaultData.handleNotValidUserID(Project.class, dataToSave.getId(), dataToSave.getUserID(), userID);
		
		dataToSave.setName(newData.getName());
		dataToSave.setUserID(userID);
		
		defaultData.handleNullProperty(dataToSave::getTests, ArrayList::new, dataToSave::setTests);
		
		return dataToSave;
	}


}
