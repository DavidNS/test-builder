package com.dns.resttestbuilder.steps.validation.step;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.exception.NullPropertyException;
import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.steps.embeddedstep.RequestStepModel;
import com.dns.resttestbuilder.steps.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.steps.embeddedstep.mainRequest.ResponseSuccessKind;
import com.dns.resttestbuilder.steps.validation.AStepValidation;

@Component
public class MainRequestStepValidaton extends AStepValidation<MainRequestStepModel> {

	@Override
	public void handle(Step step,	MainRequestStepModel mainRequestStepModel, HashMap<Long, Integer> stepNumberVsInJson) {
		Long stepOrder = step.getStepOrder();
		ExpectedPerformaceResults expectedPerformaceResults = mainRequestStepModel.getExpectedPerformaceResults();
		RequestStepModel requestStepModel = mainRequestStepModel.getRequestStepModel();
		ResponseSuccessKind responseSuccessKind = expectedPerformaceResults.getResponseSuccessKind();
		handleOutput(expectedPerformaceResults, responseSuccessKind);
		handleCode(expectedPerformaceResults, responseSuccessKind);
		handleInJSON(step, requestStepModel.getInJson(), stepNumberVsInJson);
		stepNumberVsInJson.put(stepOrder, 1);
	}

	private void handleCode(ExpectedPerformaceResults expectedPerformaceResults,
			ResponseSuccessKind responseSuccessKind) {
		if(needsCode(responseSuccessKind)&& expectedPerformaceResults.getExpectedHttpStatus()==null ) {
			throw new NullPropertyException(expectedPerformaceResults, "expectedHttpStatus, if the property responseSuccessKind needs to chech the http code.");
		}
	}

	private void handleOutput(ExpectedPerformaceResults expectedPerformaceResults,
			ResponseSuccessKind responseSuccessKind) {
		if (responseSuccessKind.equals(ResponseSuccessKind.AS_EXPECTED) && expectedPerformaceResults.getOutput()==null) {
			throw new NullPropertyException(expectedPerformaceResults, "output, if the property responseSuccessKind needs to be compared against model.");
		}
	}
	
	private boolean needsCode(ResponseSuccessKind responseSuccessKind) {
		return ( responseSuccessKind.equals(ResponseSuccessKind.SUCCESS)
				|| responseSuccessKind.equals(ResponseSuccessKind.AS_EXPECTED));
	}
	
}
