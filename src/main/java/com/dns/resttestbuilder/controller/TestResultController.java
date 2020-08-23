package com.dns.resttestbuilder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.repository.TestResultRepository;
import com.dns.resttestbuilder.validation.DefaultData;

@RestController
@RequestMapping(path = "/users/{userID}")
public class TestResultController {

	@Autowired
	DefaultData defaultData;

	@Autowired
	TestResultRepository repository;
	
	
	@Autowired
	TestController testCntroller;
	
	@GetMapping("/testresults/{testResultID}")
	TestResult getOrTrow(@PathVariable Long userID, @PathVariable Long testResultID) {
		TestResult testResult = repository.findById(testResultID).map((tr) -> {
			defaultData.handleNotValidUserID(TestResult.class, testResultID, tr.getUserID(), userID);
			return tr;
		}).orElseThrow(defaultData.getNotFoundSupplier(TestResult.class, testResultID));
		return testResult;
	}


	
	@DeleteMapping("/tests/{projectID}/tests/{id}")
	void delete(@PathVariable Long userID,@PathVariable Long projectID,@PathVariable Long id) {
		Test project=testCntroller.getOrThrow(userID, projectID);
		project.getTestResults().removeIf((t)->{return t.getId().equals(id);});
		testCntroller.saveFull(project);
	}
	
	public TestResult saveFull(TestResult tr) {
		return repository.save(tr);
		
	}


	public TestResult findById(Long testResultID) {
		return  repository.findById(testResultID).get();
	}
	
}
