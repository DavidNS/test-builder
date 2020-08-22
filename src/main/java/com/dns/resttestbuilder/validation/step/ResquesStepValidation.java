package com.dns.resttestbuilder.validation.step;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.controller.dto.StepRest;
import com.dns.resttestbuilder.entity.embeddedstep.RequestStepModel;
import com.dns.resttestbuilder.validation.AStepValidation;

@Component
public class ResquesStepValidation extends AStepValidation<RequestStepModel>{

	@Override
	public void handle(StepRest<RequestStepModel> step, HashMap<Long, Integer> stepNumberVsInJson) {
		Long stepOrder = step.getStepOrder();
		validatorCustom.handleInJSON(step, step.getStepModel().getInJson(), stepNumberVsInJson);
		stepNumberVsInJson.put(stepOrder, 1);
	}

}
