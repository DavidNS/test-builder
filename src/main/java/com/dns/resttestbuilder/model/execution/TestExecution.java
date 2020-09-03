package com.dns.resttestbuilder.model.execution;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.ConfigurableApplicationContext;

import com.dns.resttestbuilder.entity.Result;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.StepKind;
import com.dns.resttestbuilder.entity.Test;
import com.dns.resttestbuilder.entity.TestResult;
import com.dns.resttestbuilder.entity.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.StressConditions;
import com.dns.resttestbuilder.model.execution.steps.EditFieldStep;
import com.dns.resttestbuilder.model.execution.steps.MapFieldsStep;
import com.dns.resttestbuilder.model.execution.steps.SendRequestStep;
import com.dns.resttestbuilder.model.execution.steps.StressExecution;
import com.dns.resttestbuilder.model.execution.steps.Times;

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
		List<Step> steps = t.getSteps();
		MainRequestStepModel mainRequestStepModel = tr.getMainRequestStepModel();
		buildMap();
		HashMap<Long, HashMap<Long, HashMap<Long, String>>> sendParallStepNumberVSOutJSON = new HashMap<>();
		HashMap<Long, HashMap<Long, HashMap<Long, HashMap<Long, String>>>> sendParallStepNumberVSInNumberVSInJSON = new HashMap<>();
		processSteps(steps, mainRequestStepModel, sendParallStepNumberVSOutJSON,
				sendParallStepNumberVSInNumberVSInJSON);
		executeStessTest(mainRequestStepModel, sendParallStepNumberVSOutJSON, sendParallStepNumberVSInNumberVSInJSON);
	}

	private void buildMap() {
		stepKindVsProcessStepMap.put(StepKind.EDIT_FIELD, context.getBean(EditFieldStep.class)::processStep);
		stepKindVsProcessStepMap.put(StepKind.MAP_FIELD, context.getBean(MapFieldsStep.class)::processStep);
		stepKindVsProcessStepMap.put(StepKind.SEND_REQUEST, context.getBean(SendRequestStep.class)::processStep);
	}

	private void processSteps(List<Step> steps, MainRequestStepModel mainRequestStepModel,
			HashMap<Long, HashMap<Long, HashMap<Long, String>>> sendParallStepNumberVSOutJSON,
			HashMap<Long, HashMap<Long, HashMap<Long, HashMap<Long, String>>>> sendParallStepNumberVSInNumberVSInJSON) {
		StressConditions sc = mainRequestStepModel.getStressConditions();
		Long parallelRequest = sc.getNumberOfParallelRequest();
		Long repetitions = sc.getNumberOfTest();
		for (long i = 0; i < repetitions; i++) {
			HashMap<Long, HashMap<Long, HashMap<Long, String>>> spIn = new HashMap<>();
			HashMap<Long, HashMap<Long, String>> spOut = new HashMap<>();
			sendParallStepNumberVSOutJSON.put(i, spOut);
			sendParallStepNumberVSInNumberVSInJSON.put(i, spIn);
			for (long j = 0; j < parallelRequest; j++) {
				HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON = new HashMap<>();
				HashMap<Long, String> stepNumberVSOutJSON = new HashMap<>();
				spOut.put(j, stepNumberVSOutJSON);
				spIn.put(j, stepNumberVSInNumberVSInJSON);
				processSteps(steps, stepNumberVSOutJSON, stepNumberVSInNumberVSInJSON);
			}
		}
	}

	private void processSteps(List<Step> steps, HashMap<Long, String> stepNumberVSOutJSON,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON) {
		for (int i = 0; i < steps.size() - 1; i++) {
			Step step = steps.get(i);
			stepKindVsProcessStepMap.get(step.getStepKind()).processStep(step, stepNumberVSInNumberVSInJSON,
					stepNumberVSOutJSON);
		}
	}

	private void executeStessTest(MainRequestStepModel mainRequestStepModel,
			HashMap<Long, HashMap<Long, HashMap<Long, String>>> sendParallStepNumberVSOutJSON,
			HashMap<Long, HashMap<Long, HashMap<Long, HashMap<Long, String>>>> sendParallStepNumberVSInNumberVSInJSON) {
		StressConditions sc = mainRequestStepModel.getStressConditions();
		Long parallelRequest = sc.getNumberOfParallelRequest();
		Long repetitions = sc.getNumberOfTest();
		Long requestDelay = sc.getDelayBetweenParallelRequest();
		Long parallelDelay = sc.getDelayBetweenParallelTest();
		Times tt = newTimes(parallelRequest*repetitions);
		for (long i = 0; i < repetitions; i++) {
			Times pt = newTimes(parallelRequest);
			HashMap<Long, HashMap<Long, String>> spOut = sendParallStepNumberVSOutJSON.get(i);
			HashMap<Long, HashMap<Long, HashMap<Long, String>>> spIn = sendParallStepNumberVSInNumberVSInJSON.get(i);
			for (long j = 0; j < parallelRequest; j++) {
				HashMap<Long, String> stepNumberVSOutJSON = spOut.get(j);
				HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON = spIn.get(j);
				Result result = newResult(i, j);
				stressAsyncExecutor.execute(new StressExecution(tt, pt, tr.getId(), result, mainRequestStepModel,
						stepNumberVSOutJSON, stepNumberVSInNumberVSInJSON, context));
				trySleep(requestDelay);
			}
			trySleep(parallelDelay);
		}
	}

	private Result newResult(long i, long j) {
		Result result = new Result();
		result.setUserID(tr.getUserID());
		result.getMeta().setSendNumber(i);
		result.getMeta().setParallelNumber(j);
		return result;
	}

	private Times newTimes(Long parallelRequest) {
		Times pt = new Times();
		pt.setExtectedResponses(parallelRequest);
		return pt;
	}

	private void trySleep(long delayBetweenRequest) {
		try {
			Thread.sleep(delayBetweenRequest);
		} catch (InterruptedException e) {
			log.error("Error while trying to sleep, caused by ", e);
		}
	}

}
