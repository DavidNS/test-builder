package com.dns.resttestbuilder.steps.validation.step;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.RequestStepModel;
import com.dns.resttestbuilder.steps.validation.AStepValidation;

@Component
public class ResquesStepValidation extends AStepValidation<RequestStepModel>{

	@Override
	public void handle(Step step, RequestStepModel requestStepModel, HashMap<Long, Integer> stepNumberVsInJson) {
		Long stepOrder = step.getStepOrder();
		handleInJSON(step, requestStepModel.getInJson(), stepNumberVsInJson);
		stepNumberVsInJson.put(stepOrder, 1);
	}

}
