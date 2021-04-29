package com.dns.resttestbuilder.steps.validation.step;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.MapFieldStepModel;
import com.dns.resttestbuilder.steps.validation.AStepValidation;

@Component
public class MapFieldValidation extends AStepValidation<MapFieldStepModel>{

	@Override
	public void handle(Step step,	MapFieldStepModel mapFieldStepModel , HashMap<Long, Integer> stepNumberVsInJson) {
		Long stepOrder = step.getStepOrder();
		List<String> inJSON = mapFieldStepModel.getInJsons();
		for (String in : inJSON) {
			handleInJSON(step, in, stepNumberVsInJson);
		}
		stepNumberVsInJson.put(stepOrder, inJSON.size());
		
	}

}
