package com.dns.resttestbuilder.testexecutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

@Component
public class ReservedNamesParser {

	public JsonElement getInputJsonElement(HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, String inJson) {
		JsonElement jsonElement = null;
		String regexpInJSON = ReservedNames.STEP_IN_REGEXP;
		String regexpOutJSON = ReservedNames.STEP_OUT_REGEXP;
		Pattern inPattern = Pattern.compile(regexpInJSON);
		Pattern outPattern = Pattern.compile(regexpOutJSON);
		Matcher matcherIn = inPattern.matcher(inJson);
		Matcher matcherOut = outPattern.matcher(inJson);
		if (matcherIn.matches()) {
			String[] identifiers = inJson.split(ReservedNames.IDENTIFIER_SEPARATOR);
			String stepID = identifiers[0].replaceFirst(ReservedNames.STEP_IDENTIFIER, "");
			String inID = identifiers[1].replaceFirst(ReservedNames.INPUT_IDENTIFIER, "");
			jsonElement = JsonParser
					.parseString(stepNumberVSInNumberVSInJSON.get(Long.parseLong(stepID)).get(Long.parseLong(inID)));
		} else if (matcherOut.matches()) {
			String[] identifiers = inJson.split(ReservedNames.IDENTIFIER_SEPARATOR);
			String stepID = identifiers[0].replaceFirst(ReservedNames.STEP_IDENTIFIER, "");
			jsonElement = JsonParser.parseString(stepNumberVSOutJSON.get(Long.parseLong(stepID)));
		} else {
			jsonElement = JsonParser.parseString(inJson);
		}
		return jsonElement;
	}

	public JsonElement getNextChildren(JsonElement children, String idElement) {
		if (children.isJsonArray()) {
			String arrayIndex = idElement.substring(1, idElement.length());
			children = children.getAsJsonArray().get(Integer.parseInt(arrayIndex));
		} else if (children.isJsonObject()) {
			if (idElement.contains(ReservedNames.ARRAY_BEGIN_IDENTIFIER)) {
				String memberName = idElement.substring(0, idElement.indexOf(ReservedNames.ARRAY_BEGIN_IDENTIFIER));
				String arrayIndex = idElement.substring(idElement.indexOf(ReservedNames.ARRAY_BEGIN_IDENTIFIER,
						idElement.indexOf(ReservedNames.ARRAY_END_IDENTIFIER)));
				children = children.getAsJsonObject().getAsJsonArray(memberName).get(Integer.parseInt(arrayIndex));
			} else {
				children = children.getAsJsonObject().get(idElement);
			}
		}
		return children;
	}
	
	public void mapOutJson(JsonElement parent,HashMap<String, JsonElement> storedElements ,HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		if(parent.isJsonArray()) {
			parent.getAsJsonArray().forEach((e)->{
				mapOutJsonArray(parent, e, storedElements,stepNumberVSInNumberVSInJSON,stepNumberVSOutJSON);
			});
		}else {
			parent.getAsJsonObject().entrySet().forEach((e)->{
				mapOutJsonObject(parent, e.getValue(), e.getKey(), storedElements,stepNumberVSInNumberVSInJSON,stepNumberVSOutJSON);
			});
		}
	}
	
	private void mapOutJsonObject(JsonElement parent,JsonElement children, String childrenKey,HashMap<String, JsonElement> storedElements,HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		if(children.isJsonPrimitive()) {
			String finalValue = processMapCombinations(children.getAsString(), storedElements, stepNumberVSInNumberVSInJSON,
					stepNumberVSOutJSON);
			JsonObject objectParent=parent.getAsJsonObject();
			objectParent.remove(childrenKey);
			objectParent.add(childrenKey, new JsonPrimitive(finalValue));
		}else {
			mapOutJson(children, storedElements, stepNumberVSInNumberVSInJSON,
					stepNumberVSOutJSON);
		}
	}
	
	public boolean isOtherItem(String[] elements, int i) {
		return i != elements.length - 1;
	}
	
	public String processMapCombinations(String initialCombination, HashMap<String, JsonElement> storedElements,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String[] combinations = initialCombination.split(ReservedNames.MAP_COMBINATION_SEPARATOR);
		StringBuilder result = new StringBuilder();
		for (String combination : combinations) {
			String[] identifiers = combination.split(ReservedNames.IDENTIFIER_SEPARATOR);
			tryProcessMapCombination(storedElements, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, result, combination,
						identifiers);
			
		}
		return result.toString();
	}
	
	private void mapOutJsonArray(JsonElement parent,JsonElement children ,HashMap<String, JsonElement> storedElements,HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		if(children.isJsonPrimitive()) {
			String finalValue = processMapCombinations(children.getAsString(), storedElements, stepNumberVSInNumberVSInJSON,
					stepNumberVSOutJSON);
			JsonArray arrayParent=parent.getAsJsonArray();
			arrayParent.remove(children);
			arrayParent.add(new JsonPrimitive(finalValue));
		}else {
			mapOutJson(children,  storedElements, stepNumberVSInNumberVSInJSON,
					stepNumberVSOutJSON);
		}
	}
	
	private JsonElement getLastChildren(JsonElement children, String idElement) {
		if (children.isJsonArray()) {
			String arrayIndex = idElement.substring(1, idElement.length());
			children = children.getAsJsonArray().get(Integer.parseInt(arrayIndex)).getAsJsonPrimitive();
		} else if (children.isJsonObject()) {
			if (idElement.contains(ReservedNames.ARRAY_BEGIN_IDENTIFIER)) {
				String memberName = idElement.substring(0, idElement.indexOf(ReservedNames.ARRAY_BEGIN_IDENTIFIER));
				String arrayIndex = idElement.substring(idElement.indexOf(ReservedNames.ARRAY_BEGIN_IDENTIFIER,
						idElement.indexOf(ReservedNames.ARRAY_END_IDENTIFIER)));
				children = children.getAsJsonObject().getAsJsonArray(memberName).get(Integer.parseInt(arrayIndex)).getAsJsonPrimitive();
			}else {
				children = children.getAsJsonObject().get(idElement).getAsJsonPrimitive();
			}
		}
		return children;
	}



	private void tryProcessMapCombination(HashMap<String, JsonElement> storedElements,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, StringBuilder result, String combination, String[] identifiers) {
		try {
			String stepID = identifiers[0];
			String jsonID = identifiers[1];
			
			String stepAndJson = stepID + ReservedNames.IDENTIFIER_SEPARATOR_NOT_ESCAPED + jsonID;
			JsonElement element = storedElements.get(stepAndJson);
			if (element == null) {
				element = getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,
						stepAndJson);
				storedElements.put(stepAndJson, element);
			}
			String combinationResult = processMapCombination(element, identifiers);
			result.append(combinationResult);
		} catch (Exception e) {
			result.append(combination);
		}
	}
	
	private String processMapCombination(JsonElement children, String[] keyTree) {
		for (int i = 2; i < keyTree.length; i++) {
			String key = keyTree[i];
			if (isOtherItem(keyTree, i)) {
				children = getNextChildren(children, key);
			} else {
				children = getLastChildren(children, key);
			}

		}
		return children.getAsString();
	}
	
	public String generateCombinedEndpoint(String endpoint, Map<String, String> paramVsCombination,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String[] totalSplit = generateEnpointParamSplit(endpoint);
		Map<String, String> entryVSFinalValue = new HashMap<String, String>();
		HashMap<String, JsonElement> storedElements = new HashMap<>();
		if (paramVsCombination != null) {
			for (var entry : paramVsCombination.entrySet()) {
				processCombinationEntry(entry, storedElements, entryVSFinalValue, stepNumberVSInNumberVSInJSON,
						stepNumberVSOutJSON);
			}
		}
		return generateFinalUrl(totalSplit, entryVSFinalValue, endpoint);
	}
	
	private void processCombinationEntry(Entry<String, String> entry, HashMap<String, JsonElement> storedElements,
			Map<String, String> entryVSFinalValue, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String combinationResult = processMapCombinations(entry.getValue(), storedElements, stepNumberVSInNumberVSInJSON,
				stepNumberVSOutJSON);
		entryVSFinalValue.put( entry.getKey(), combinationResult);

	}

	

	private String generateFinalUrl(String[] totalSplit, Map<String, String> entryVSFinalValue,
			String defaultEndpoint) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean isVar = false;
		for (String string : totalSplit) {
			if (isVar) {
				for (var keyNewVal : entryVSFinalValue.entrySet()) {
					if (keyNewVal.getKey().equals(string)) {
						stringBuilder.append(keyNewVal.getValue());
					}
				}
			} else {
				stringBuilder.append(string);
			}
			if (string.isEmpty()) {
				isVar = !isVar;
			}
		}
		return stringBuilder.toString();
	}
	

	public String[] generateEnpointParamSplit(String url) {
		String[] split1 = url.split(ReservedNames.URL_BEGIN_PARAM);
		ArrayList<String> enpointSplits = new ArrayList<>();
		for (String beginSplit : split1) {
			String[] totalSplit = beginSplit.split(ReservedNames.URL_END_PARAM);
			enpointSplits.addAll(Arrays.asList(totalSplit));
		}
		return enpointSplits.toArray(String[]::new);
	}
}
