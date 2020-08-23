package com.dns.resttestbuilder.validation.step;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.embeddedstep.EditFieldStepModel;
import com.dns.resttestbuilder.exception.NotValidKeyArrayException;
import com.dns.resttestbuilder.exception.NotValidMethodException;
import com.dns.resttestbuilder.model.execution.steps.EditScripts;
import com.dns.resttestbuilder.validation.AStepValidation;



@Component
public class EditFieldStepValidation extends AStepValidation<EditFieldStepModel>{


	@Autowired
	ReservedNames reservedNames;
	
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
			String[] keyArrays = entries.split(reservedNames.getIdentifierSeparator());
			for (String keyArray : keyArrays) {
				if (keyArray.contains(reservedNames.getIdentifierSeparator())) {
					String arrayIndex = keyArray.substring(keyArray.indexOf(reservedNames.getArrayBeginIdentifier(),
							keyArray.indexOf(reservedNames.getArrayEndIdentifier())));
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
		Method[] methods = EditScripts.class.getMethods();
		HashMap<String, Integer> publicStringMethodNames = removeNotValidMethods(methods);
		for (String methodComb : values) {
			String[] ids = methodComb.split(reservedNames.getIdentifierSeparator());
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

	private boolean isValidSignaure(Method method) {
		boolean valid = method.getReturnType().equals(String.class) && Modifier.isPublic(method.getModifiers());
		Parameter[] params = method.getParameters();
		for (int i = 0; i < params.length && valid; i++) {
			Parameter p = params[i];
			if (!p.getType().equals(String.class)) {
				valid = false;
			}
		}
		return valid;
	}

	private HashMap<String, Integer> removeNotValidMethods(Method[] methods) {
		HashMap<String, Integer> result = new HashMap<>();
		for (Method method : methods) {
			if (isValidSignaure(method)) {
				result.put(method.getName(), method.getParameterCount() - 1);
			}
		}
		return result;
	}

}
