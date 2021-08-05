package com.dns.resttestbuilder.users;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.configuration.Validation;
import com.dns.resttestbuilder.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	Validation validation;

	@Autowired
	UserRepository repository;

	@GetMapping
	public User getOrNew(Principal principal) {		
		return repository.findById(principal.getName()).orElseGet(()->{
			log.info("User not found {}, creating new...",principal.getName());
			return repository.save(handle(new User(), principal));
		});
	}
	
	public User saveFull(User user) {
		return repository.save(user);
	}


	public User getOrThrow(String id) throws NotFoundException {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
	}
	
	public User handle(User dataToSave,Principal principal) {
		dataToSave.setId(principal.getName());
		validation.handleNullProperty(dataToSave::getWorkspaces, ArrayList::new, dataToSave::setWorkspaces);
		validation.handleNullProperty(dataToSave::getUserPreferences, HashMap::new, dataToSave::setUserPreferences);
		return dataToSave;
	}

}