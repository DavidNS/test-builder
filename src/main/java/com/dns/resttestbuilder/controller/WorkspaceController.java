package com.dns.resttestbuilder.controller;

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

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.entity.User;
import com.dns.resttestbuilder.entity.Workspace;
import com.dns.resttestbuilder.repository.WorkspaceRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/users/{userID}/workspaces")
public class WorkspaceController {

	@Autowired
	DefaultData defaultData;

	@Autowired
	WorkspaceRepository repository;

	@Autowired
	UserController userController;

	@GetMapping
	List<Workspace> getAll(@PathVariable Long userID) {
		return repository.findByUserID(userID);
	}
	
	Workspace saveFull(Workspace workspace) {
		return repository.save(workspace);
	}

	@PostMapping
	Workspace newItem(@PathVariable Long userID, @RequestBody Workspace workspace) {
		log.info("Creating new workspace {}",workspace.getName());
		User user = userController.getOrThrow(userID);
		workspace = repository.save(handle(userID, new Workspace(), workspace));
		user.getWorkspaces().add(workspace);
		userController.saveFull(user);
		return workspace;
	}

	@GetMapping("/{id}")
	Workspace getOrThrow(@PathVariable Long userID, @PathVariable Long id) {
		return repository.findById(id).map((ws) -> {
			defaultData.handleNotValidUserID(Workspace.class, id, ws.getUserID(), userID);
			return ws;
		}).orElseThrow(defaultData.getNotFoundSupplier(Workspace.class, id));
	}

	@PutMapping("/{id}")
	Workspace replace(@PathVariable Long userID, @PathVariable Long id, @RequestBody Workspace workspace) {
		return repository.findById(id).map(ws -> {
			return repository.save(handle(userID, ws, workspace));
		}).orElseThrow(defaultData.getNotFoundSupplier(Workspace.class, id));
	}

	@DeleteMapping("/{id}")
	void delete(@PathVariable Long userID, @PathVariable Long id) {
		User user = userController.getOrThrow(userID);
		user.getWorkspaces().removeIf((wksUser) -> {
			return wksUser.getId().equals(id);
		});
		userController.getOrReplaceFields(user);
	}
	


	public Workspace handle(Long userID, Workspace dataToSave, Workspace newData) {
		defaultData.handleCreatingObjectBeforeCreatingUser(userID);
		defaultData.handleNotValidUserID(Workspace.class, dataToSave.getId(), dataToSave.getUserID(), userID);

		dataToSave.setName(newData.getName());
		dataToSave.setUserID(userID);

		defaultData.handleNullProperty(dataToSave::getProjects, ArrayList::new, dataToSave::setProjects);
		defaultData.handleNullProperty(dataToSave::getEnvironments, ArrayList::new, dataToSave::setEnvironments);
		defaultData.handleNullProperty(dataToSave::getGlobalVars, ArrayList::new, dataToSave::setGlobalVars);

		return dataToSave;
	}

}
