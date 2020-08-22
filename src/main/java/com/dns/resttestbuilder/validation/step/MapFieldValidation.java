package com.dns.resttestbuilder.validation.step;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.controller.dto.StepRest;
import com.dns.resttestbuilder.entity.embeddedstep.MapFieldStepModel;
import com.dns.resttestbuilder.validation.AStepValidation;

@Component
public class MapFieldValidation extends AStepValidation<MapFieldStepModel>{

	@Override
	public void handle(StepRest<MapFieldStepModel> step, HashMap<Long, Integer> stepNumberVsInJson) {
		Long stepOrder = step.getStepOrder();
		MapFieldStepModel mapFieldStepModel =   step.getStepModel();
		List<String> inJSON = mapFieldStepModel.getInJson();
		for (String in : inJSON) {
			validatorCustom.handleInJSON(step, in, stepNumberVsInJson);
		}
		stepNumberVsInJson.put(stepOrder, inJSON.size());
		
	}

}
