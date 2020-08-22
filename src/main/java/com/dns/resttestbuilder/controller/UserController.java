package com.dns.resttestbuilder.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.entity.User;
import com.dns.resttestbuilder.exception.NotFoundException;
import com.dns.resttestbuilder.repository.UserRepository;
import com.dns.resttestbuilder.validation.DefaultData;

import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	DefaultData defaultData;

	@Autowired
	UserRepository repository;

	@GetMapping
	List<User> all() {
		return repository.findAll();
	}


	// TODO: This is the currently main mapping, because there is not security added yet ->
	// Secure this app.
	@PostMapping
	User getOrReplaceFields(@Valid @RequestBody User user) {
		return repository.findByName(user.getName()).stream().findFirst().map((u)->{
			log.info("User found {}",user.getName());
			return repository.save(handle(u, user));
		}).orElseGet(() -> {
			log.info("User not found {}, creating new...",user.getName());
			return repository.save(handle(new User(), user));
		});
	}
	
	User saveFull(User user) {
		return repository.save(user);
	}


	User getOrThrow(Long id) throws NotFoundException {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
	}
	
	public User handle(User dataToSave,User newData) {
		dataToSave.setName(newData.getName());
		dataToSave.setUserPreferences(newData.getUserPreferences());
		defaultData.handleNullProperty(dataToSave::getWorkspaces, ArrayList::new, dataToSave::setWorkspaces);
		defaultData.handleNullProperty(dataToSave::getUserPreferences, HashMap::new, dataToSave::setUserPreferences);
		
		return dataToSave;
	}

}