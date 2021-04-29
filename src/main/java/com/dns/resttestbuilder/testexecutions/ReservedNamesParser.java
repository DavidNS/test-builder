package com.dns.resttestbuilder.testexecutions;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

@Component
public class ReservedNamesParser {

	@Autowired
	ReservedNames reservedNames;

	public JsonElement getInputJsonElement(HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, String inJson) {
		JsonElement jsonElement = null;
		String regexpInJSON = reservedNames.getStepInRegexp();
		String regexpOutJSON = reservedNames.getStepOutRegex();
		Pattern inPattern = Pattern.compile(regexpInJSON);
		Pattern outPattern = Pattern.compile(regexpOutJSON);
		Matcher matcherIn = inPattern.matcher(inJson);
		Matcher matcherOut = outPattern.matcher(inJson);
		if (matcherIn.matches()) {
			String[] identifiers = inJson.split(reservedNames.getIdentifierSeparator());
			String stepID = identifiers[0].replaceFirst(reservedNames.getStepIdentifier(), "");
			String inID = identifiers[1].replaceFirst(reservedNames.getInputIdentifier(), "");
			jsonElement = JsonParser
					.parseString(stepNumberVSInNumberVSInJSON.get(Long.parseLong(stepID)).get(Long.parseLong(inID)));
		} else if (matcherOut.matches()) {
			String[] identifiers = inJson.split(reservedNames.getIdentifierSeparator());
			String stepID = identifiers[0].replaceFirst(reservedNames.getStepIdentifier(), "");
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
			if (idElement.contains(reservedNames.getArrayBeginIdentifier())) {
				String memberName = idElement.substring(0, idElement.indexOf(reservedNames.getArrayBeginIdentifier()));
				String arrayIndex = idElement.substring(idElement.indexOf(reservedNames.getArrayBeginIdentifier(),
						idElement.indexOf(reservedNames.getArrayEndIdentifier())));
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
			String finalValue = processCombinations(children.getAsString(), storedElements, stepNumberVSInNumberVSInJSON,
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
	
	public String processCombinations(String initialCombination, HashMap<String, JsonElement> storedElements,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String[] combinations = initialCombination.split(reservedNames.getMapCombinationSeparator());
		StringBuilder result = new StringBuilder();
		for (String combination : combinations) {
			String[] identifiers = combination.split(reservedNames.getIdentifierSeparator());
			if (identifiers.length > 2) {
				tryProcessCombination(storedElements, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, result, combination,
						identifiers);
			} else {
				result.append(combination);
			}
		}
		return result.toString();
	}
	
	private void mapOutJsonArray(JsonElement parent,JsonElement children ,HashMap<String, JsonElement> storedElements,HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		if(children.isJsonPrimitive()) {
			String finalValue = processCombinations(children.getAsString(), storedElements, stepNumberVSInNumberVSInJSON,
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
			if (idElement.contains(reservedNames.getArrayBeginIdentifier())) {
				String memberName = idElement.substring(0, idElement.indexOf(reservedNames.getArrayBeginIdentifier()));
				String arrayIndex = idElement.substring(idElement.indexOf(reservedNames.getArrayBeginIdentifier(),
						idElement.indexOf(reservedNames.getArrayEndIdentifier())));
				children = children.getAsJsonObject().getAsJsonArray(memberName).get(Integer.parseInt(arrayIndex)).getAsJsonPrimitive();
			}else {
				children = children.getAsJsonObject().get(idElement).getAsJsonPrimitive();
			}
		}
		return children;
	}



	private void tryProcessCombination(HashMap<String, JsonElement> storedElements,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON, StringBuilder result, String combination, String[] identifiers) {
		try {
			String stepID = identifiers[0];
			String jsonID = identifiers[1];
			String stepAndJson = stepID + reservedNames.getIdentifierSeparatorNotEscaped() + jsonID;
			JsonElement element = storedElements.get(stepAndJson);
			if (element == null) {
				element = getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON,
						stepAndJson);
				storedElements.put(stepAndJson, element);
			}
			String combinationResult = processCombination(element, identifiers);
			result.append(combinationResult);
		} catch (Exception e) {
			result.append(combination);
		}
	}
	
	private String processCombination(JsonElement children, String[] keyTree) {
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
}
