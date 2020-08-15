package com.dns.resttestbuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.ConfigurableApplicationContext;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.controller.TestExecutorController;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.StepKind;
import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embedded.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embedded.mainRequest.Result;
import com.dns.resttestbuilder.entity.embedded.mainRequest.StressConditions;
import com.dns.resttestbuilder.model.steps.EditFieldStep;
import com.dns.resttestbuilder.model.steps.MergeFieldsStep;
import com.dns.resttestbuilder.model.steps.SendRequestStep;
import com.dns.resttestbuilder.model.steps.StressExecution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TestExecution implements Runnable {

	ConfigurableApplicationContext context;

	Test t;

	TestResult tr;

	ThreadPoolExecutor stressAsyncExecutor;

	final HashMap<StepKind, ProcessStep> stepKindVsProcessStepMap = new HashMap<>();

	@FunctionalInterface
	interface ProcessStep {
		void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
				HashMap<Long, String> stepNumberVSOutJSON);
	}

	@Override
	public void run() {
		buildMap();
		List<Step> steps = t.getSteps();
		MainRequestStepModel mainRequestStepModel = tr.getMainRequestStepModel();
		List<Result> mainRequests = generateMainRequest(steps, mainRequestStepModel);
		executeStessTest(mainRequestStepModel, mainRequests);
	}

	private void buildMap() {
		stepKindVsProcessStepMap.put(StepKind.EDIT_FIELD, context.getBean(EditFieldStep.class)::processStep);
		stepKindVsProcessStepMap.put(StepKind.MERGE_FIELD, context.getBean(MergeFieldsStep.class)::processStep);
		stepKindVsProcessStepMap.put(StepKind.SEND_REQUEST, context.getBean(SendRequestStep.class)::processStep);
	}

	private List<Result> generateMainRequest(List<Step> steps, MainRequestStepModel mainRequestStepModel) {
		List<Result> mainRequests = new ArrayList<>();
		StressConditions sc = mainRequestStepModel.getStressConditions();
		Long parallelRequest = sc.getNumberOfParallelRequest();
		Long repetitions = sc.getNumberOfTest();
		for (long i = 0; i < repetitions; i++) {
			for (long j = 0; j < parallelRequest; j++) {
				mainRequests = generateMainRequest(steps, mainRequests, mainRequestStepModel);
			}
		}
		return mainRequests;
	}

	private List<Result> generateMainRequest(List<Step> steps, List<Result> mainRequests,
			MainRequestStepModel mainRequestStepModel) {
		HashMap<Long, String> stepNumberVSOutJSON = new HashMap<>();
		HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON = new HashMap<>();
		processSteps(steps, stepNumberVSOutJSON, stepNumberVSInNumberVSInJSON);
		Result result = generateMainRequest(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, mainRequestStepModel);
		mainRequests.add(result);
		return mainRequests;
	}

	private void processSteps(List<Step> steps, HashMap<Long, String> stepNumberVSOutJSON,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON) {
		for (int i = 0; i < steps.size() - 1; i++) {
			Step step = steps.get(i);
			stepKindVsProcessStepMap.get(step.getStepKind()).processStep(step, stepNumberVSInNumberVSInJSON,
					stepNumberVSOutJSON);
		}
	}

	// TODO -> Default data shall process url -> Needs to update the variables in url
	private Result generateMainRequest(HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, MainRequestStepModel mainRequestStepModel) {
		DefaultData dd=context.getBean(DefaultData.class);
		Result result = new Result();
		result.setMainURL(mainRequestStepModel.getRequestStepModel().getUrl());
		result.setMainRequest(dd.getInputJson(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,  mainRequestStepModel.getRequestStepModel().getInJson()).toString());
		return result;
	}

	private void executeStessTest(MainRequestStepModel mainRequestStepModel, List<Result> mainRequests) {
		StressConditions sc = mainRequestStepModel.getStressConditions();
		Long parallelRequest = sc.getNumberOfParallelRequest();
		Long repetitions = sc.getNumberOfTest();
		Long requestDelay = sc.getDelayBetweenParallelRequest();
		Long parallelDelay = sc.getDelayBetweenParallelRequest();
//		Long totalTest = parallelRequest * repetitions;
		for (long i = 0; i < repetitions; i++) {
			for (long j = 0; j < parallelRequest; j++) {
				stressAsyncExecutor.execute(new StressExecution(repetitions, parallelRequest, tr.getId(),
						mainRequests.get((int) (i * parallelRequest + j)), mainRequestStepModel,
						context.getBean(TestExecutorController.class)));
				trySleep(requestDelay);
			}
			trySleep(parallelDelay);
		}
	}

	private void trySleep(long delayBetweenRequest) {
		try {
			Thread.sleep(delayBetweenRequest);
		} catch (InterruptedException e) {
			log.error("Error while trying to sleep, caused by ", e);
		}
	}

}
