package com.dns.resttestbuilder.model;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.ReservedNames;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Component
public class JsonInParser {

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
			String arrayIndex = idElement.split(reservedNames.getArrayIdentifier())[1];
			children = children.getAsJsonArray().get(Integer.parseInt(arrayIndex)).getAsJsonArray();
		} else if (children.isJsonObject()) {
			String keyName = idElement.split(reservedNames.getKeyIdentifier())[1];
			children = children.getAsJsonObject().getAsJsonObject(keyName);
		}
		return children;
	}

	public JsonElement getLastChildren(JsonElement children, String idElement) {
		if (children.isJsonArray()) {
			String arrayIndex = idElement.split(reservedNames.getArrayIdentifier())[1];
			children = children.getAsJsonArray().get(Integer.parseInt(arrayIndex)).getAsJsonPrimitive();
		} else if (children.isJsonObject()) {
			String keyName = idElement.split(reservedNames.getKeyIdentifier())[1];
			children = children.getAsJsonObject().get(keyName).getAsJsonPrimitive();
		}
		return children;
	}

	public boolean isOtherItem(String[] elements, int i) {
		return i != elements.length - 1;
	}
}
