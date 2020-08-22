package com.dns.resttestbuilder.validation.step;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.controller.dto.StepRest;
import com.dns.resttestbuilder.entity.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.RequestStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ResponseSuccessKind;
import com.dns.resttestbuilder.exception.NullPropertyException;
import com.dns.resttestbuilder.validation.AStepValidation;

@Component
public class MainRequestStepValidaton extends AStepValidation<MainRequestStepModel> {

	@Override
	public void handle(StepRest<MainRequestStepModel> step, HashMap<Long, Integer> stepNumberVsInJson) {
		Long stepOrder = step.getStepOrder();
		MainRequestStepModel mainRequestStepModel = step.getStepModel();
		ExpectedPerformaceResults expectedPerformaceResults = mainRequestStepModel.getExpectedPerformaceResults();
		RequestStepModel requestStepModel = mainRequestStepModel.getRequestStepModel();
		ResponseSuccessKind responseSuccessKind = expectedPerformaceResults.getResponseSuccessKind();
		if (needsModel(responseSuccessKind) && expectedPerformaceResults.getPlainKeyVSExpectedOutValue()==null) {
			throw new NullPropertyException(expectedPerformaceResults, "plainKeyVSExpectedOutValue, if needs to be compared against model.");
		}
		validatorCustom.handleInJSON(step, requestStepModel.getInJson(), stepNumberVsInJson);
		stepNumberVsInJson.put(stepOrder, 1);
	}

	private boolean needsModel(ResponseSuccessKind responseSuccessKind) {
		return !(responseSuccessKind.equals(ResponseSuccessKind.NONE)
				|| responseSuccessKind.equals(ResponseSuccessKind.RESPONSE_RECEIVED)
				|| responseSuccessKind.equals(ResponseSuccessKind.RESPONSE_SUCCESS));
	}
	
}
