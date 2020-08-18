package com.dns.resttestbuilder.model.steps;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.embedded.MapFieldStepModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component
public class MapFieldsStep {

	@Autowired
	ReservedNames reservedNames;

	@Autowired
	DefaultData defaultData;

	public void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		Long stepNumber = step.getStepOrder();
		MapFieldStepModel mapFieldStepModel = new Gson().fromJson(step.getStepModel(), MapFieldStepModel.class);
		List<String> inJSONsFromModel = mapFieldStepModel.getInJson();
		HashMap<Long, String> inNumberVSinJsons = generateInJsonNumbers(inJSONsFromModel);
		stepNumberVSInNumberVSInJSON.put(stepNumber, inNumberVSinJsons);
		
		Map<String, String> outPlainKeyVSMergeCombination = mapFieldStepModel.getOutPlainKeyVsMapCombination();
		List<JsonElement> inJSON = generateInJson(inJSONsFromModel, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		String outJSON = mapOutJson(outPlainKeyVSMergeCombination, inJSON);
		stepNumberVSOutJSON.put(stepNumber, outJSON);
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

	private String mapOutJson(Map<String, String> outPlainKeyVSMergeCombination, List<JsonElement> inJSON) {
		Map<String, JsonElement> plainKeysVsElements = new HashMap<>();
		JsonElement root=getRoot(outPlainKeyVSMergeCombination,plainKeysVsElements);
		for (var plainKeyVsMergeCombination : outPlainKeyVSMergeCombination.entrySet()) {
			StringBuilder plainKeyBuilder = new StringBuilder();
			String outPlainKey = plainKeyVsMergeCombination.getKey();
			String mapCombination = plainKeyVsMergeCombination.getValue();
			String finalValue = processCombinations(mapCombination, inJSON);
			String[] idElements = outPlainKey.split(reservedNames.getIdentifierSeparator());
			try {
				iterateOverElements(plainKeyBuilder, plainKeysVsElements, idElements, finalValue, root);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return plainKeysVsElements.get("/").toString();
	}

	private JsonElement getRoot(Map<String, String> outPlainKeyVSMergeCombination, Map<String, JsonElement> plainKeysVsElements) {
		String firstValue=outPlainKeyVSMergeCombination.keySet().iterator().next().split(reservedNames.getIdentifierSeparator())[0];
		if(firstValue.startsWith(reservedNames.getKeyIdentifier())) {
			JsonElement root=new JsonObject();
			plainKeysVsElements.put("/", root);
			plainKeysVsElements.put(firstValue, root);
			return root;
		}else {
			JsonElement root=new JsonArray();
			plainKeysVsElements.put("/", root);
			plainKeysVsElements.put(firstValue, root);
			return root;
		}
	}

	private void iterateOverElements(StringBuilder plainKeyBuilder, Map<String, JsonElement> plainKeysVsElements,String[] idElements, String finalValue, JsonElement children) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < idElements.length; i++) {
			String idElement = idElements[i];
			plainKeyBuilder.append(idElement);
			String plainKey=plainKeyBuilder.toString();
			if (defaultData.isOtherItem(idElements, i)) {
				JsonElement storedElement=plainKeysVsElements.get(plainKey);
				if(storedElement!=null) {
					children=storedElement;
				}else {
					children = addNextChildren(plainKeysVsElements,plainKey, children, idElement, idElements[i-1]);
				}
			} else {
				updateValue(finalValue, children, idElement);
			}
		}
	}
	
	private void updateValue(String finalValue, JsonElement children, String idElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (children.isJsonObject()) {
			String keyName = idElement.split(reservedNames.getKeyIdentifier())[1];
			children.getAsJsonObject().add(keyName, new JsonPrimitive(finalValue));
		} else {
			String arrayIndex = idElement.split(reservedNames.getArrayIdentifier())[1];
			children.getAsJsonArray().set(Integer.parseInt(arrayIndex), new JsonPrimitive(finalValue));
		}
	}
	
	public JsonElement addNextChildren(Map<String, JsonElement> plainKeysVsElements, String plainKey, JsonElement children, String idElement,  String previousIdElement) {
		JsonElement newChildren=getNewChildren(idElement);
		plainKeysVsElements.put(plainKey, newChildren);
		if(children.isJsonObject()) {
			String keyName = previousIdElement.split(reservedNames.getKeyIdentifier())[1];
			children.getAsJsonObject().add(keyName, newChildren);
		}else if(children.isJsonArray()) {
			String arrayIndex = previousIdElement.split(reservedNames.getArrayIdentifier())[1];
			children.getAsJsonArray().set(Integer.parseInt(arrayIndex), newChildren);
		}
		return children;
	}
	
	private JsonElement getNewChildren(String idElement) {
		if(idElement.contains(reservedNames.getKeyIdentifier())) {
			return new JsonObject();
		}else {
			return new JsonArray();
		}
	}

	private String processCombinations(String mapCombination, List<JsonElement> inJSON) {
		String[] combinations = mapCombination.split(reservedNames.getMapCombinationSeparator());
		if (combinations.length > 0) {
			return processCombinations(combinations, inJSON);
		} else {
			return mapCombination;
		}
	}

	private String processCombinations(String[] combinations, List<JsonElement> inJSON) {
		String result = "";
		for (String initialCombination : combinations) {
			String[] identifiers = initialCombination.split(reservedNames.getIdentifierSeparator());
			if (identifiers.length > 1) {
				try {
					String jsonNumber = identifiers[0].replaceFirst(reservedNames.getInputIdentifier(), "");
					JsonElement in=inJSON.get(Integer.parseInt(jsonNumber));
					String combinationResult = processCombination(in, identifiers);
					result = result + combinationResult;
				} catch (Exception e) {
					result = result + initialCombination;
				}
			} else {
				result = result + initialCombination;
			}

		}
		return result;
	}

	private String processCombination(JsonElement children, String[] keyTree) {
		for (int i = 1; i < keyTree.length; i++) {
			String key = keyTree[i];
			if(defaultData.isOtherItem(keyTree, i)) {
				children = defaultData.getNextChildren(children, key);
			}else {
				children=defaultData.getLastChildren(children, key);
			}
	
		}
		return children.getAsString();
	}

	private List<JsonElement> generateInJson(List<String> inJSONsFromModel,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		List<JsonElement> inJsons = new ArrayList<>();
		for (var jsonObject : inJSONsFromModel) {
			JsonElement inJson = defaultData.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,
					jsonObject);
			inJsons.add(inJson);
		}
		return inJsons;
	}
}