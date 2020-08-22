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

import com.dns.resttestbuilder.entity.Project;
import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.exception.InvalidUserIDException;
import com.dns.resttestbuilder.exception.UserIDNotFoundException;
import com.dns.resttestbuilder.repository.TestRepository;
import com.dns.resttestbuilder.validation.DefaultData;

@RestController
@RequestMapping(path="/users/{userID}")
public class TestController {
	
	@Autowired
	DefaultData defaultData;
	
	@Autowired
	TestRepository repository;
	
	
	@Autowired
	ProjectController projectController;

	@Autowired
	UserController userController;
	

	@GetMapping("/tests")
	List<Test> getAll(@PathVariable Long userID) {
		return repository.findByUserID(userID);
	}
	
	@GetMapping("/tests/{id}")
	Test getOrThrow(@PathVariable Long userID,@PathVariable Long id)  {
		return repository.findById(id).map((pj)->{
			defaultData.handleNotValidUserID(Test.class, id, pj.getUserID(), userID);
			return pj;
		}).orElseThrow(defaultData.getNotFoundSupplier(Test.class, id));
	}
	
	@PutMapping("/tests/{id}")
	Test replace(@PathVariable Long userID, @PathVariable Long id, @RequestBody Test test)  {
		return repository.findById(id).map(pj -> {
			return repository.save(handle(userID,pj,test));
		}).orElseThrow(defaultData.getNotFoundSupplier(Test.class, id));
	}
	
	@PostMapping("/projects/{projectID}/tests")
	Test newItem(@PathVariable Long userID, @PathVariable Long projectID,@RequestBody Test test)  {
		Project project=projectController.getOrThrow(userID,projectID);
		test=repository.save(handle(userID, new Test(), test));
		project.getTests().add(test);
		projectController.saveFull(project);
		return test;
	}
	
	@DeleteMapping("/projects/{projectID}/tests/{id}")
	void delete(@PathVariable Long userID,@PathVariable Long projectID,@PathVariable Long id) {
		Project project=projectController.getOrThrow(userID, projectID);
		project.getTests().removeIf((t)->{return t.getId().equals(id);});
		projectController.saveFull(project);
	}

	
	public Test handle(Long userID, Test dataToSave, Test newData) throws InvalidUserIDException, UserIDNotFoundException {
		defaultData.handleCreatingObjectBeforeCreatingUser(userID);
		defaultData.handleNotValidUserID(Test.class, dataToSave.getId(), dataToSave.getUserID(), userID);
		
		dataToSave.setName(newData.getName());
		dataToSave.setUserID(userID);
		
		defaultData.handleNullProperty(dataToSave::getSteps, ArrayList::new, dataToSave::setSteps);
		defaultData.handleNullProperty(dataToSave::getTestResult, ArrayList::new, dataToSave::setTestResult);
		return dataToSave;
	}

	public void saveFull(Test project) {
		repository.save(project);
		
	}

	Test get(Long testID) {
		return repository.findById(testID).get();
	}
}
