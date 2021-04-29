package com.dns.resttestbuilder.testexecutions.execution.steps;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.MapFieldStepModel;
import com.dns.resttestbuilder.testexecutions.JsonObjectParser;
import com.dns.resttestbuilder.testexecutions.ReservedNamesParser;
import com.google.gson.JsonElement;

@Component
public class MapFieldsStep {

	@Autowired
	ReservedNames reservedNames;

	
	@Autowired
	JsonObjectParser jsonObjectParser;
	
	@Autowired
	ReservedNamesParser jsonInParser;

	public void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		Long stepNumber = step.getStepOrder();
		MapFieldStepModel mapFieldStepModel =  jsonObjectParser.objectToModel(step.getStepModel(), MapFieldStepModel.class,step::setStepModel) ;
		
		List<String> inJSONsFromModel = mapFieldStepModel.getInJsons();
		HashMap<Long, String> inNumberVSinJsons = generateInJsonNumbers(inJSONsFromModel);
		stepNumberVSInNumberVSInJSON.put(stepNumber, inNumberVSinJsons);
		
		JsonElement outModel= jsonObjectParser.objectToModel(mapFieldStepModel.getOutJson(), JsonElement.class,mapFieldStepModel::setOutJson) ;
		jsonInParser.mapOutJson(outModel, new HashMap<>(),stepNumberVSInNumberVSInJSON,stepNumberVSOutJSON);
		stepNumberVSOutJSON.put(stepNumber, outModel.toString());
	}
	

	private HashMap<Long, String> generateInJsonNumbers(List<String> inJSONsFromModel) {
		HashMap<Long, String> inNumberVSinJsons = new HashMap<>();
		long i = 0;
		for (var jsonElement : inJSONsFromModel) {
			inNumberVSinJsons.put(i, jsonElement);
			i++;
		}
		return inNumberVSinJsons;
	}
}