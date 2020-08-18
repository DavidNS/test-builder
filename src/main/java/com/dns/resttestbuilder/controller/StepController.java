
package com.dns.resttestbuilder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.repository.StepRepository;

@RestController
@RequestMapping(path = "/users/{userID}")
public class StepController {

	@Autowired
	DefaultData defaultData;

	@Autowired
	StepRepository repository;

	@Autowired
	TestController testController;

	@Autowired
	UserController userController;

	@GetMapping("/steps")
	List<Step> getAll(@PathVariable Long userID) {
		return repository.findByUserID(userID);
	}

	@GetMapping("/steps/{id}")
	Step getOrThrow(@PathVariable Long userID, @PathVariable Long id) {
		return repository.findById(id).map((s) -> {
			defaultData.handleNotValidUserID(Step.class, id, s.getUserID(), userID);
			return s;
		}).orElseThrow(defaultData.getNotFoundSupplier(Step.class, id));
	}

//	@PutMapping("/steps/{id}")
//	Step replace(@PathVariable Long userID, @PathVariable Long id, @RequestBody Step step) {
//		return repository.findById(id).map(s -> {
//			return repository.save(handle(userID,s,step));
//		}).orElseThrow(defaultData.getNotFoundSupplier(Step.class, id));
//	}

	@PostMapping("/tests/{testID}/steps")
	List<Step> newItem(@PathVariable Long userID, @PathVariable Long testID, @RequestBody List<Step> steps) {
		Test test = testController.getOrThrow(userID, testID);
		handle(userID, steps);
		steps = saveSteps(steps);
		test.getSteps().clear();
		test.getSteps().addAll(steps);
		testController.saveFull(test);
		return steps;
	}

	private List<Step> saveSteps(List<Step> steps) {
		List<Step> stepsSaved = new ArrayList<>();
		for (Step step : steps) {
			stepsSaved.add(repository.save(step));
		}
		return stepsSaved;
	}

	@DeleteMapping("/tests/{testID}/steps/{id}")
	void delete(@PathVariable Long userID, @PathVariable Long testID, @PathVariable Long id) {
		Test test = testController.getOrThrow(userID, testID);
		test.getSteps().removeIf((s) -> {
			return s.getId().equals(id);
		});
		testController.saveFull(test);
	}

	public void handle(Long userID, List<Step> newData) {
		defaultData.handleCreatingObjectBeforeCreatingUser(userID);

		for (Step step : newData) {
			step.setUserID(userID);
		}
		
		// TODO Check that all steps are following the rules

//		return newData;
	}
}
