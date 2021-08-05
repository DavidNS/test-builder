package com.dns.resttestbuilder.tests;

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
import com.dns.resttestbuilder.projects.Project;
import com.dns.resttestbuilder.projects.ProjectController;
import com.dns.resttestbuilder.users.UserController;

@RestController
@RequestMapping
public class TestController {
	
	@Autowired
	Validation validation;
	
	@Autowired
	TestRepository repository;
	
	
	@Autowired
	ProjectController projectController;

	@Autowired
	UserController userController;
	

	@GetMapping("/tests")
	List<Test> getAll(Principal principal) {
		return repository.findByUserID(principal.getName());
	}
	
	@GetMapping("/tests/{id}")
	public Test getOrThrow(Principal principal,@PathVariable Long id)  {
		return repository.findById(id).map((pj)->{
			validation.handleNotValidUserID(Test.class, id, pj.getUserID(), principal.getName());
			return pj;
		}).orElseThrow(validation.getNotFoundSupplier(Test.class, id));
	}
	
	@PutMapping("/tests/{id}")
	Test replace(Principal principal, @PathVariable Long id, @RequestBody Test test)  {
		return repository.findById(id).map(pj -> {
			return repository.save(handle(principal,pj,test));
		}).orElseThrow(validation.getNotFoundSupplier(Test.class, id));
	}
	
	@PostMapping("/projects/{projectID}/tests")
	Test newItem(Principal principal, @PathVariable Long projectID,@RequestBody Test test)  {
		Project project=projectController.getOrThrow(principal,projectID);
		test=repository.save(handle(principal, new Test(), test));
		project.getTests().add(test);
		projectController.saveFull(project);
		return test;
	}
	
	@DeleteMapping("/projects/{projectID}/tests/{id}")
	void delete(Principal principal,@PathVariable Long projectID,@PathVariable Long id) {
		Project project=projectController.getOrThrow(principal, projectID);
		project.getTests().removeIf((t)->{return t.getId().equals(id);});
		projectController.saveFull(project);
	}

	
	public Test handle(Principal principal, Test dataToSave, Test newData)  {
		String userID=principal.getName();
		validation.handleCreatingObjectBeforeCreatingUser(userID);
		validation.handleNotValidUserID(Test.class, dataToSave.getId(), dataToSave.getUserID(), userID);
		
		dataToSave.setName(newData.getName());
		dataToSave.setUserID(userID);
		
		validation.handleNullProperty(dataToSave::getSteps, ArrayList::new, dataToSave::setSteps);
		validation.handleNullProperty(dataToSave::getTestResults, ArrayList::new, dataToSave::setTestResults);
		return dataToSave;
	}

	public void saveFull(Test project) {
		repository.save(project);
		
	}

	Test get(Long testID) {
		return repository.findById(testID).get();
	}

}
