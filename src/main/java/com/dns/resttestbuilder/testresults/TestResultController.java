package com.dns.resttestbuilder.testresults;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.configuration.Validation;
import com.dns.resttestbuilder.tests.Test;
import com.dns.resttestbuilder.tests.TestController;

@RestController
@RequestMapping
public class TestResultController {

	@Autowired
	Validation validation;

	@Autowired
	TestResultRepository repository;
	
	
	@Autowired
	TestController testController;
	

	@GetMapping("/testresults")
	List<TestResult> getAll(Principal principal) {
		return repository.findByUserID(principal.getName());
	}
	
	@GetMapping("/testresults/{testResultID}")
	TestResult getOrTrow(Principal principal, @PathVariable Long testResultID) {
		TestResult testResult = repository.findById(testResultID).map((tr) -> {
			validation.handleNotValidUserID(TestResult.class, testResultID, tr.getUserID(), principal.getName());
			return tr;
		}).orElseThrow(validation.getNotFoundSupplier(TestResult.class, testResultID));
		return testResult;
	}


	
	@DeleteMapping("/tests/{testID}/testresults/{id}")
	void delete(Principal principal,@PathVariable Long testID,@PathVariable Long id) {
		Test project=testController.getOrThrow(principal, testID);
		project.getTestResults().removeIf((t)->{return t.getId().equals(id);});
		testController.saveFull(project);
	}
	
	public TestResult saveFull(TestResult tr) {
		return repository.save(tr);
		
	}


	public TestResult findById(Long testResultID) {
		return  repository.findById(testResultID).get();
	}
	
}
