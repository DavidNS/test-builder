package com.dns.resttestbuilder.validation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.controller.dto.StepRest;
import com.dns.resttestbuilder.exception.NotValidInFormatException;
import com.dns.resttestbuilder.exception.NotValidKeyArrayException;
import com.dns.resttestbuilder.exception.NotValidMethodException;
import com.dns.resttestbuilder.exception.NotValidatedException;
import com.dns.resttestbuilder.model.steps.EditScripts;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

@Component
public class ValidatorCustom {

	@Autowired
	ReservedNames reservedNames;

	@Autowired
	Validator validator;

	public void handleInJSON(StepRest<?> step, String inJson, HashMap<Long, Integer> stepNumberVsInJson) {
		String regexpInJSON = reservedNames.getStepInRegexp();
		String regexpOutJSON = reservedNames.getStepOutRegex();
		Pattern inPattern = Pattern.compile(regexpInJSON);
		Pattern outPattern = Pattern.compile(regexpOutJSON);
		Matcher matcherIn = inPattern.matcher(inJson);
		Matcher matcherOut = outPattern.matcher(inJson);
		Long stepOrder = step.getStepOrder();
		if (stepOrder.longValue() > 0 && matcherIn.matches()) {
			String[] ids = inJson.split(reservedNames.getIdentifierSeparator());
			handleStepLowerThanCurrent(step, inJson, ids, regexpInJSON);
			handleStepInLowerThanTotal(step, inJson, ids, regexpInJSON, stepNumberVsInJson);
		} else if (stepOrder.longValue() > 0 && matcherOut.matches()) {
			String[] ids = inJson.split(reservedNames.getIdentifierSeparator());
			handleStepLowerThanCurrent(step, inJson, ids, regexpOutJSON);
		} else {
			handleJsonFormat(step, inJson, regexpInJSON, regexpOutJSON);
		}
	}

	private void handleJsonFormat(StepRest<?> step, String inJson, String regexpInJSON, String regexpOutJSON) {
		try {
			JsonParser.parseString(inJson);
		} catch (Exception e) {
			throw new NotValidInFormatException(step, inJson,
					"The step is number 0 and has not not valid as JSON format or Not found regexp for input: "
							+ regexpInJSON + " or output: " + regexpOutJSON);
		}

	}

	private void handleStepInLowerThanTotal(StepRest<?> step, String inJson, String[] ids, String regexpInJSON,
			HashMap<Long, Integer> stepNumberVsInJson) {
		String stepID = ids[0].replaceFirst(reservedNames.getStepIdentifier(), "");
		String inID = ids[1].replaceFirst(reservedNames.getInputIdentifier(), "");
		Integer inIDL = Integer.parseInt(inID);
		Integer maxID = stepNumberVsInJson.get(Long.parseLong(stepID));
		if (maxID.intValue() <= inIDL.intValue()) {
			throw new NotValidInFormatException(step, inJson,
					" Found reserved-name combination: " + regexpInJSON + " for input, the input number: " + inID
							+ " must be lower the step-vs-total: " + stepID + "-vs-" + maxID);
		}
	}

	private void handleStepLowerThanCurrent(StepRest<?> step, String inJson, String[] ids, String regexpOutJSON) {
		String stepID = ids[0].replaceFirst(reservedNames.getStepIdentifier(), "");
		Long longStepID = Long.parseLong(stepID);
		if (step.getStepOrder().longValue() <= longStepID.longValue()) {
			throw new NotValidInFormatException(step, inJson,
					" Found reserved-name combination:" + regexpOutJSON + " for input, the step: " + stepID
							+ " must be lower than current step " + step.getStepOrder().longValue());
		}
	}

	public void handleValidKeyFormat(Collection<String> set, StepRest<?> step) {
		for (var entries : set) {
			String[] keyArrays = entries.split(reservedNames.getIdentifierSeparator());
			for (String keyArray : keyArrays) {
				if (keyArray.startsWith(reservedNames.getArrayIdentifier())) {
					String arrayID = keyArray.replaceFirst(reservedNames.getArrayIdentifier(), "");
					handleArrayNumber(step, entries, keyArray, arrayID);
				} else if (!keyArray.startsWith(reservedNames.getKeyIdentifier())) {
					throw new NotValidKeyArrayException(step, entries, keyArray,
							" Each new plainKey needs to be separated by " + reservedNames.getIdentifierSeparator()
									+ " and starts with " + reservedNames.getArrayIdentifier() + " or "
									+ reservedNames.getKeyIdentifier());
				}
			}
		}
	}

	private void handleArrayNumber(StepRest<?> step, String fullMap, String keyArray, String arrayID) {
		try {
			Long.parseLong(arrayID);
		} catch (Exception e) {
			throw new NotValidKeyArrayException(step, fullMap, keyArray + arrayID, " Expected a number for array");
		}
	}

	public void handleValidMethds(Collection<String> values, StepRest<?> step) {
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

	public <T> void validateObject(T o) throws RuntimeException, IllegalAccessException {
		HashMap<String, BindingResult> keyVSerrors = new HashMap<>();
		BindingResult errors = new BeanPropertyBindingResult(o, o.getClass().getName());
		validator.validate(o, errors);
		addErrors(o.getClass().getSimpleName(), keyVSerrors, errors);
		validateChildObject(o, keyVSerrors, o.getClass().getSimpleName());
		if (keyVSerrors.size() > 0) {
			throw new NotValidatedException(o, keyVSerrors);
		}
	}

	private <T> void validateChildObject(T o, HashMap<String, BindingResult> keyVSerrors, String simpleName)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean prevState = field.canAccess(o);
			field.setAccessible(true);
			Object child = field.get(o);
			if (child != null) {
				String complexName = simpleName + "." + child.getClass().getSimpleName();
				if (!BeanUtils.isSimpleValueType(child.getClass()) && !child.getClass().equals(ArrayList.class)
						&& !child.getClass().equals(LinkedTreeMap.class)) {
					BindingResult errors = new BeanPropertyBindingResult(child, child.getClass().getName());
					validator.validate(child, errors);
					addErrors(complexName, keyVSerrors, errors);
					validateChildObject(child, keyVSerrors, complexName);
				}
			}
			field.setAccessible(prevState);
		}

	}

	private <T> void addErrors(String key, HashMap<String, BindingResult> keyVSerrors, BindingResult errors) {
		if (errors.hasErrors()) {
			keyVSerrors.put(key, errors);
		}
	}

}
