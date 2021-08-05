package com.dns.resttestbuilder.steps.validation.step;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.exception.NotValidKeyArrayException;
import com.dns.resttestbuilder.exception.NotValidMethodException;
import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.EditFieldStepModel;
import com.dns.resttestbuilder.steps.validation.AStepValidation;
import com.dns.resttestbuilder.testexecutions.execution.steps.EditScripts;



@Component
public class EditFieldStepValidation extends AStepValidation<EditFieldStepModel>{
	
	@Override
	protected void handle(Step step, EditFieldStepModel editFieldStepModel, HashMap<Long, Integer> stepNumberVsInJson) {
		handleInJSON(step, editFieldStepModel.getInJson(), stepNumberVsInJson);
		Map<String, String> keyVsMethod = editFieldStepModel.getPlainKeyVsMehtod();
		handleValidKeyFormat(keyVsMethod.keySet(), step);
		handleValidMethds(keyVsMethod.values(), step);
		stepNumberVsInJson.put(step.getStepOrder(), 1);
		
	}
	
	private void handleValidKeyFormat(Collection<String> set, Step step) {
		for (var entries : set) {
			String[] keyArrays = entries.split(ReservedNames.IDENTIFIER_SEPARATOR);
			for (String keyArray : keyArrays) {
				if (keyArray.contains(ReservedNames.IDENTIFIER_SEPARATOR)) {
					String arrayIndex = keyArray.substring(keyArray.indexOf(ReservedNames.ARRAY_BEGIN_IDENTIFIER,
							keyArray.indexOf(ReservedNames.ARRAY_END_IDENTIFIER)));
					handleArrayNumber(step, entries, keyArray, arrayIndex);
				}
			}
		}
	}

	private void handleArrayNumber(Step step, String fullMap, String keyArray, String arrayID) {
		try {
			Long.parseLong(arrayID);
		} catch (Exception e) {
			throw new NotValidKeyArrayException(step, fullMap, keyArray + arrayID, " Expected a number for array");
		}
	}

	public void handleValidMethds(Collection<String> values, Step step) {
		HashMap<String, Integer> publicStringMethodNames = EditScripts.getValidMethods();
		for (String methodComb : values) {
			String[] ids = methodComb.split(ReservedNames.IDENTIFIER_SEPARATOR);
			int paramComb = ids.length - 1;
			String methodName = ids[0];
			Integer params = publicStringMethodNames.get(methodName);
			if (params == null) {
				throw new NotValidMethodException(step, methodName, " Method name is not valid ",
						publicStringMethodNames.toString());
			}
			if (params.intValue() != paramComb) {
				throw new NotValidMethodException(step, methodName,
						" Has " + paramComb + " params and requires " + params.intValue(),
						publicStringMethodNames.toString());
			}

		}
	}

}
