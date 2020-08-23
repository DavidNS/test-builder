package com.dns.resttestbuilder.validation.step;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.embeddedstep.RequestStepModel;
import com.dns.resttestbuilder.validation.AStepValidation;

@Component
public class ResquesStepValidation extends AStepValidation<RequestStepModel>{

	@Override
	public void handle(Step step, RequestStepModel requestStepModel, HashMap<Long, Integer> stepNumberVsInJson) {
		Long stepOrder = step.getStepOrder();
		handleInJSON(step, requestStepModel.getInJson(), stepNumberVsInJson);
		stepNumberVsInJson.put(stepOrder, 1);
	}

}
