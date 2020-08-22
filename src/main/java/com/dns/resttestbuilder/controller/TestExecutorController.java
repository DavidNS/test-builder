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
import com.dns.resttestbuilder.entity.Result;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embeddedresult.Evaluation;
import com.dns.resttestbuilder.entity.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.model.JsonStepParser;
import com.dns.resttestbuilder.model.TestExecution;
import com.dns.resttestbuilder.model.steps.Times;

@RestController
@RequestMapping(path = "/users/{userID}")
public class TestExecutorController {

	@Autowired
	TestController testController;

	@Autowired
	JsonStepParser parser;
	
	@Autowired
	AsyncConfiguration asyncConfiguration;

	@Autowired
	TestResultController testResultController;

	@Autowired
	ResultController resultController;

	@Autowired
	ConfigurableApplicationContext context;

	@GetMapping("/tests/{testID}/execute")
	TestResult executeTest(@PathVariable Long userID, @PathVariable Long testID) {
		Test t = testController.getOrThrow(userID, testID);
		TestResult tr = createTestResult(t, userID);
		t.getTestResult().add(tr);
		testResultController.saveFull(tr);
		testController.saveFull(t);
		startAsyncTestExecution(t, tr);
		return tr;
	}


	public synchronized void updateTestResults(Long testResultID, Result result, Times tt, Times pt) {
		TestResult testResult = testResultController.findById(testResultID);
		ExpectedPerformaceResults exRS = testResult.getMainRequestStepModel().getExpectedPerformaceResults();
		result=resultController.saveFull(result);
		testResult.getResults().add(result);
		testResultController.saveFull(testResult);
		resultController.updateTimes(exRS, result, tt, pt);	
		testResult = testResultController.findById(testResultID);
		updateTimesOKKOCount(testResult);
		testResultController.saveFull(testResult);
		
	}

	private void updateTimesOKKOCount(TestResult testResult) {
		List<Result> rs=testResult.getResults();
		for (Result r : rs) {
			Evaluation eval=r.getEvaluation();
			if(eval.getTimePassed()!=null) {
				if(eval.getTimePassed()) {
					Long ok=testResult.getTimeOK();
					ok++;
					testResult.setFormatOK(ok);
				}else {
					Long ko=testResult.getTimeKO();
					ko++;
					testResult.setFormatKO(ko);
				}
			}
		}
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
		Step mainRequestStep=steps.get(steps.size() - 1);
		MainRequestStepModel mr= parser.dbObjectToModel(mainRequestStep.getStepModel(),MainRequestStepModel.class,mainRequestStep::setStepModel);
		return mr;
	}

	private void sortByOrderNumber(List<Step> steps) {
		Collections.sort(steps, (one, two) -> {
			return one.getStepOrder().intValue() - two.getStepOrder().intValue();
		});
	}
}
