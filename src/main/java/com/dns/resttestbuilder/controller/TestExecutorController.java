package com.dns.resttestbuilder.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.configuration.AsyncConfiguration;
import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embedded.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embedded.mainRequest.Result;
import com.dns.resttestbuilder.model.TestExecution;
import com.dns.resttestbuilder.repository.TestResultRepository;
import com.google.gson.Gson;

@RestController
@RequestMapping(path = "/users/{userID}")
public class TestExecutorController {

	@Autowired
	TestController testController;

	@Autowired
	DefaultData defaultData;

	@Autowired
	AsyncConfiguration asyncConfiguration;

	@Autowired
	TestResultRepository resultRepository;
	
	@Autowired
	ConfigurableApplicationContext context;

	@GetMapping("/tests/{testID}/execute")
	TestResult executeTest(@PathVariable Long userID, @PathVariable Long testID) {
		Test t = testController.getOrThrow(userID, testID);
		TestResult tr = createTestResult(t, userID);
		t.getTestResult().add(tr);
		resultRepository.save(tr);
		testController.saveFull(t);
		startAsyncTestExecution(t, tr);
		return tr;
	}

	@GetMapping("/testresults/{testResultID}")
	TestResult getOrTrow(@PathVariable Long userID, @PathVariable Long testResultID) {
		TestResult testResult = resultRepository.findById(testResultID).map((tr) -> {
			defaultData.handleNotValidUserID(TestResult.class, testResultID, tr.getUserID(), userID);
			return tr;
		}).orElseThrow(defaultData.getNotFoundSupplier(TestResult.class, testResultID));
		return testResult;
	}

	public void updateTestResults(Long testResultID, Result result) {
		TestResult testResult = resultRepository.findById(testResultID).get();
		testResult.getResults().add(result);
		resultRepository.save(testResult);
//		Test test = testController.get(testID);
//		List<TestResult> trs=test.getTestResult();
//		trs.removeIf(tr -> {
//			return tr.getId().equals(testID);
//		});
//		trs.add(testResult);
//		testController.saveFull(test);

	}

	private TestResult createTestResult(Test test, Long userID) {
		TestResult tr = new TestResult();
		List<Step> steps = test.getSteps();
		sortByOrderNumber(steps);
		MainRequestStepModel mainRequestStepModel = getMainRequestStepModel(steps);
		tr.setMainRequestStepModel(mainRequestStepModel);
		tr.setUserID(userID);
		tr.setResults(new ArrayList<>());
		return tr;
	}

	public void startAsyncTestExecution(Test testToExecute, TestResult tr) {
		asyncConfiguration.getTestAsyncExecutor()
				.execute(new TestExecution(context, testToExecute, tr, asyncConfiguration.getStressAsyncExecutor()));
	}

	private MainRequestStepModel getMainRequestStepModel(List<Step> steps) {
		return new Gson().fromJson(steps.get(steps.size() - 1).getStepModel(), MainRequestStepModel.class);
	}

	private void sortByOrderNumber(List<Step> steps) {
		Collections.sort(steps, (one, two) -> {
			return one.getStepOrder().intValue()- two.getStepOrder().intValue();
		});
	}
}
