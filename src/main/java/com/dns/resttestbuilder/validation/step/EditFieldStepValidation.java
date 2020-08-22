package com.dns.resttestbuilder.validation.step;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.controller.dto.StepRest;
import com.dns.resttestbuilder.entity.embeddedstep.EditFieldStepModel;
import com.dns.resttestbuilder.validation.AStepValidation;



@Component
public class EditFieldStepValidation extends AStepValidation<EditFieldStepModel>{

	@Override
	protected void handle(StepRest<EditFieldStepModel> step, HashMap<Long, Integer> stepNumberVsInJson) {
		EditFieldStepModel editFieldStepModel = step.getStepModel();
		validatorCustom.handleInJSON(step, editFieldStepModel.getInJson(), stepNumberVsInJson);
		Map<String, String> keyVsMethod = editFieldStepModel.getPlainKeyVsMehtod();
		validatorCustom.handleValidKeyFormat(keyVsMethod.keySet(), step);
		validatorCustom.handleValidMethds(keyVsMethod.values(), step);
		stepNumberVsInJson.put(step.getStepOrder(), 1);
		
	}

}
