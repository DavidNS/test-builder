package com.dns.resttestbuilder.testexecutions;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dns.resttestbuilder.configuration.AsyncConfiguration;
import com.dns.resttestbuilder.results.Result;
import com.dns.resttestbuilder.results.ResultController;
import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.testexecutions.evaluation.FormatEvaluation;
import com.dns.resttestbuilder.testexecutions.evaluation.TimeEvaluation;
import com.dns.resttestbuilder.testexecutions.execution.TestExecution;
import com.dns.resttestbuilder.testexecutions.execution.steps.Times;
import com.dns.resttestbuilder.testresults.TestResult;
import com.dns.resttestbuilder.testresults.TestResultController;
import com.dns.resttestbuilder.tests.Test;
import com.dns.resttestbuilder.tests.TestController;

@RestController
@RequestMapping
public class TestExecutorController {

	@Autowired
	TestController testController;

	@Autowired
	JsonObjectParser parser;

	@Autowired
	AsyncConfiguration asyncConfiguration;

	@Autowired
	TestResultController testResultController;

	@Autowired
	TimeEvaluation timeEvaluation;

	@Autowired
	FormatEvaluation formatEvaluation;

	@Autowired
	ResultController resultController;

	@Autowired
	ConfigurableApplicationContext context;

	@GetMapping("/tests/{testID}/execute")
	TestResult executeTest(Principal principal, @PathVariable Long testID) {
		Test t = testController.getOrThrow(principal, testID);
		TestResult tr = createTestResult(t, principal);
		t.getTestResults().add(tr);
		testResultController.saveFull(tr);
		testController.saveFull(t);
		startAsyncTestExecution(t, tr);
		return tr;
	}

	public synchronized void updateTestResults(Long testResultID, Result result, Times tt, Times pt,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		TestResult testResult = testResultController.findById(testResultID);
		ArrayList<Result> updatedTimes = timeEvaluation.evaluateTimes(testResult, result, tt, pt);
		formatEvaluation.evaluateFormat(testResult,result, stepNumberVSInNumberVSInJSON,
				stepNumberVSOutJSON);
		result = resultController.saveFull(result);
		testResult.getResults().add(result);
		testResultController.saveFull(testResult);
		resultController.saveAllFull(updatedTimes);
	}

	private TestResult createTestResult(Test test, Principal principal) {
		TestResult tr = new TestResult();
		List<Step> steps = test.getSteps();
		sortByOrderNumber(steps);
		MainRequestStepModel mainRequestStepModel = getMainRequestStepModel(steps);
		tr.setMainRequestStepModel(mainRequestStepModel);
		tr.setUserID(principal.getName());
		tr.setResults(new ArrayList<>());
		return tr;
	}

	public void startAsyncTestExecution(Test testToExecute, TestResult tr) {
		asyncConfiguration.getTestAsyncExecutor()
				.execute(new TestExecution(context, testToExecute, tr, asyncConfiguration.getStressAsyncExecutor()));
	}

	private MainRequestStepModel getMainRequestStepModel(List<Step> steps) {
		Step mainRequestStep = steps.get(steps.size() - 1);
		MainRequestStepModel mr = parser.objectToModel(mainRequestStep.getStepModel(), MainRequestStepModel.class,
				mainRequestStep::setStepModel);
		return mr;
	}

	private void sortByOrderNumber(List<Step> steps) {
		Collections.sort(steps, (one, two) -> {
			return one.getStepOrder().intValue() - two.getStepOrder().intValue();
		});
	}



}
