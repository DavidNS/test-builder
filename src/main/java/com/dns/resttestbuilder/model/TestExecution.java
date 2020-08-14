package com.dns.resttestbuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.dns.resttestbuilder.controller.TestExecutorController;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embedded.Result;
import com.dns.resttestbuilder.entity.embedded.StressConditions;
import com.dns.resttestbuilder.entity.stepModel.MainRequestStepModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class TestExecution implements Runnable {

	
	TestExecutorController testExecutorController;
	
	Test t;
	
	TestResult tr;
	
	ThreadPoolExecutor stressAsyncExecutor;

	@Override
	public void run() {
		List<Step> steps = t.getSteps();
		MainRequestStepModel mainRequestStepModel=tr.getMainRequestStepModel();
		List<Result> mainRequests = generateMainRequest(steps, mainRequestStepModel);
		executeStessTest(mainRequestStepModel, mainRequests);
	}

	private List<Result> generateMainRequest(List<Step> steps, MainRequestStepModel mainRequestStepModel) {
		List<Result> mainRequests=new ArrayList<>();
		StressConditions sc=mainRequestStepModel.getStressConditions();
		Long parallelRequest=sc.getNumberOfParallelRequest();
		Long repetitions=sc.getNumberOfTest();
		for (long i = 0; i < repetitions; i++) {
			for (long j = 0; j < parallelRequest; j++) {
				 mainRequests=generateMainRequest(steps,mainRequests,mainRequestStepModel);
			}
		}
		return mainRequests;
	}
	
	private void executeStessTest(MainRequestStepModel mainRequestStepModel, List<Result> mainRequests) {
		StressConditions sc=mainRequestStepModel.getStressConditions();
		Long parallelRequest=sc.getNumberOfParallelRequest();
		Long repetitions=sc.getNumberOfTest();
		Long requestDelay=sc.getDelayBetweenParallelRequest();
		Long parallelDelay=sc.getDelayBetweenParallelRequest();
		Long totalTest = parallelRequest * repetitions;
		for (long i = 0; i < repetitions; i++) {
			for (long j = 0; j < parallelRequest; j++) {
				stressAsyncExecutor.execute(new StressExecution(repetitions, parallelRequest ,tr.getId(),mainRequests.get((int)(i*parallelRequest+j)),mainRequestStepModel,testExecutorController ));
				trySleep(requestDelay);
			}
			trySleep(parallelDelay);
		}
	}



	private List<Result> generateMainRequest(List<Step> steps, List<Result> mainRequests, MainRequestStepModel mainRequestStepModel) {
		HashMap<Long, String> stepNumberVSOutJSON=new HashMap<>();
		HashMap<Long, HashMap<Long,String>> stepNumberVSInNumberVSInJSON=new HashMap<>();
		processSteps(steps, stepNumberVSOutJSON, stepNumberVSInNumberVSInJSON);
		Result result=generateMainRequest(stepNumberVSInNumberVSInJSON,stepNumberVSOutJSON,mainRequestStepModel);
		mainRequests.add(result);
		return mainRequests;
	}

	private void processSteps(List<Step> steps, HashMap<Long, String> stepNumberVSOutJSON,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON) {
		for (int i = 0; i < steps.size()-1; i++) {
			Step step=steps.get(i);
			processStep(step,stepNumberVSInNumberVSInJSON,stepNumberVSOutJSON);
		}
	}

	//TODO
	private Result generateMainRequest(HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, MainRequestStepModel mainRequestStepModel) {
		Result result=new Result();
		result.setMainURL(mainRequestStepModel.getRequestStepModel().getUrl());
		result.setMainRequest(mainRequestStepModel.getRequestStepModel().getInJson());
		return result;
	}

	//TODO
	private void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInputNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {

		
	}

	private void trySleep(long delayBetweenRequest) {
		try {
			Thread.sleep(delayBetweenRequest);
		} catch (InterruptedException e) {
			log.error("Error while trying to sleep, caused by ", e);
		}
	}


	
}
