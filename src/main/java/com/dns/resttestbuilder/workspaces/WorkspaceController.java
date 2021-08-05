package com.dns.resttestbuilder.workspaces;

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

import com.dns.resttestbuilder.configuration.Validation;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.users.User;
import com.dns.resttestbuilder.users.UserController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/workspaces")
public class WorkspaceController {

	@Autowired
	Validation validation;

	@Autowired
	WorkspaceRepository repository;

	@Autowired
	UserController userController;

	@GetMapping
	List<Workspace> getAll(Principal user) {
		return repository.findByUserID(user.getName());
	}
	
	public Workspace saveFull(Workspace workspace) {
		return repository.save(workspace);
	}

	@PostMapping
	Workspace newItem(Principal principal, @RequestBody Workspace workspace)  {
		log.info("Creating new workspace {}",workspace.getName());
		User user = userController.getOrThrow(principal.getName());
		workspace = repository.save(handle(principal, new Workspace(), workspace));
		user.getWorkspaces().add(workspace);
		userController.saveFull(user);
		return workspace;
	}

	@GetMapping("/{id}")
	public Workspace getOrThrow(Principal principal, @PathVariable Long id) {
		return repository.findById(id).map((ws) -> {
			validation.handleNotValidUserID(Workspace.class, id, ws.getUserID(), principal.getName());
			return ws;
		}).orElseThrow(validation.getNotFoundSupplier(Workspace.class, id));
	}

	@PutMapping("/{id}")
	Workspace replace(Principal principal, @PathVariable Long id, @RequestBody Workspace workspace)  {
		return repository.findById(id).map(ws -> {
			return repository.save(handle(principal, ws, workspace));
		}).orElseThrow(validation.getNotFoundSupplier(Workspace.class, id));
	}

	@DeleteMapping("/{id}")
	void delete(Principal principal, @PathVariable Long id) throws NotFoundException {
		User user = userController.getOrThrow(principal.getName());
		user.getWorkspaces().removeIf((wksUser) -> {
			return wksUser.getId().equals(id);
		});
		userController.saveFull(user);
	}
	


	public Workspace handle(Principal principal, Workspace dataToSave, Workspace newData) {
		String userID=principal.getName();
		validation.handleCreatingObjectBeforeCreatingUser(userID);
		validation.handleNotValidUserID(Workspace.class, dataToSave.getId(), dataToSave.getUserID(), userID);

		dataToSave.setName(newData.getName());
		dataToSave.setUserID(userID);

		validation.handleNullProperty(dataToSave::getProjects, ArrayList::new, dataToSave::setProjects);

		return dataToSave;
	}

}
