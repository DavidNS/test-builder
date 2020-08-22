package com.dns.resttestbuilder.validation;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import com.dns.resttestbuilder.controller.dto.StepRest;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.model.JsonStepParser;


public abstract class AStepValidation<T> {
	
	@Autowired
	protected ValidatorCustom validatorCustom;
	
	@Autowired
	protected JsonStepParser parser;
	
	Class<T> classCast;
	
	@SuppressWarnings("unchecked")
	public AStepValidation() {
		classCast=(Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), AStepValidation.class);
	}
	
	public void handle(Long userID, Step step, HashMap<Long, Integer> stepNumberVsInJson) throws IllegalAccessException, RuntimeException {
		StepRest<T> stepRestModel=parser.dbObjectToModel(userID,step,classCast);
		validatorCustom.validateObject(stepRestModel);
		handle(stepRestModel, stepNumberVsInJson);
	};
	
	protected abstract void handle(StepRest<T> step, HashMap<Long, Integer> stepNumberVsInJson);
}
