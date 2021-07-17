package com.dns.resttestbuilder.testexecutions.execution.steps;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.MapFieldStepModel;
import com.dns.resttestbuilder.testexecutions.JsonObjectParser;
import com.dns.resttestbuilder.testexecutions.ReservedNamesParser;
import com.google.gson.JsonElement;

@Component
public class MapFieldsStep {

	@Autowired
	JsonObjectParser jsonObjectParser;

	@Autowired
	ReservedNamesParser reservedNamesParser;

	public void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		Long stepNumber = step.getStepOrder();
		MapFieldStepModel mapFieldStepModel = jsonObjectParser.objectToModel(step.getStepModel(),
				MapFieldStepModel.class, step::setStepModel);

		List<String> inJSONsFromModel = mapFieldStepModel.getInJsons();
		HashMap<Long, String> inNumberVSinJsons = generateInJsons(inJSONsFromModel, stepNumberVSInNumberVSInJSON,
				stepNumberVSOutJSON);
		stepNumberVSInNumberVSInJSON.put(stepNumber, inNumberVSinJsons);
		JsonElement outModel = jsonObjectParser.objectToModel(mapFieldStepModel.getOutJson(), JsonElement.class,
				mapFieldStepModel::setOutJson);
		reservedNamesParser.mapOutJson(outModel, new HashMap<>(), stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		stepNumberVSOutJSON.put(stepNumber, outModel.toString());
	}

	private HashMap<Long, String> generateInJsons(List<String> inJSONsFromModel,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		HashMap<Long, String> inNumberVSinJsons = new HashMap<>();
		long i = 0;
		for (var jsonElement : inJSONsFromModel) {
			JsonElement element=reservedNamesParser
					.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, jsonElement);
			inNumberVSinJsons.put(i, element.toString());
			i++;
		}

		return inNumberVSinJsons;
	}
}