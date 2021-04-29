package com.dns.resttestbuilder.steps.validation;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.exception.NotValidInFormatException;
import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.testexecutions.JsonObjectParser;
import com.google.gson.JsonParser;


public abstract class AStepValidation<T> {
	
	@Autowired
	protected GenericValidator genericValidator;
	
	
	@Autowired
	protected ReservedNames reservedNames;
	
	
	@Autowired
	protected JsonObjectParser parser;
	
	Class<T> classCast;
	
	@SuppressWarnings("unchecked")
	public AStepValidation() {
		classCast=(Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), AStepValidation.class);
	}
	
	public void handle(Long userID, Step step, HashMap<Long, Integer> stepNumberVsInJson) throws IllegalAccessException, RuntimeException {
		T stepRestModel=parser.objectToModel(step.getStepModel(),classCast,step::setStepModel);
		genericValidator.validateObject(stepRestModel);
		handle(step,stepRestModel, stepNumberVsInJson);
	};
	
	protected abstract void handle(Step step, T stepModel, HashMap<Long, Integer> stepNumberVsInJson);


	public void handleInJSON(Step step, String inJson, HashMap<Long, Integer> stepNumberVsInJson) {
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

	private void handleJsonFormat(Step step, String inJson, String regexpInJSON, String regexpOutJSON) {
		try {
			JsonParser.parseString(inJson);
		} catch (Exception e) {
			throw new NotValidInFormatException(step, inJson,
					"The step is number 0 and has not not valid as JSON format or Not found regexp for input: "
							+ regexpInJSON + " or output: " + regexpOutJSON);
		}

	}

	private void handleStepInLowerThanTotal(Step step, String inJson, String[] ids, String regexpInJSON,
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

	private void handleStepLowerThanCurrent(Step step, String inJson, String[] ids, String regexpOutJSON) {
		String stepID = ids[0].replaceFirst(reservedNames.getStepIdentifier(), "");
		Long longStepID = Long.parseLong(stepID);
		if (step.getStepOrder().longValue() <= longStepID.longValue()) {
			throw new NotValidInFormatException(step, inJson,
					" Found reserved-name combination:" + regexpOutJSON + " for input, the step: " + stepID
							+ " must be lower than current step " + step.getStepOrder().longValue());
		}
	}

	
}
