package com.dns.resttestbuilder.model.steps;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.entity.Method;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

@Component
public class RestClient {
	
	@Autowired
	ReservedNames reservedNames;

	@Autowired
	DefaultData defaultData;
	
	private static final String MEDIA_TYPE = "application/json;charset=" + StandardCharsets.UTF_8.displayName();
	
	public Call createCall(String stringBody, String endpoint, String method) {
		long soketTimeout = 5000L;
		Request request = createRequest(stringBody, endpoint, method);
		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(soketTimeout, TimeUnit.MILLISECONDS);
		client.setReadTimeout(soketTimeout, TimeUnit.MILLISECONDS);
		return client.newCall(request);
	}
	
	public String generateCombinedEndpoint(String endpoint, Map<String, String> paramVsCombination,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String[] beginSplit = endpoint.split(reservedNames.getUrlBeginParam());
		String[] totalSplit = generateEnpointParamSplit(beginSplit);
		Map<String, String> entryVSFinalValue = new HashMap<String, String>();
		HashMap<String, JsonElement> storedElements = new HashMap<>();
		for (var entry : paramVsCombination.entrySet()) {
			processCombinationEntry(entry, storedElements, entryVSFinalValue, stepNumberVSInNumberVSInJSON,
					stepNumberVSOutJSON);
		}
		return generateFinalUrl(totalSplit, entryVSFinalValue, endpoint);
	}

	private Request createRequest(String stringBody, String endpoint, String method) {
		if (Method.GET.name().equalsIgnoreCase(method)) {
			return new Request.Builder().url(HttpUrl.parse(endpoint)).build();
		} else if (Method.DELETE.name().equalsIgnoreCase(method)) {
			return new Request.Builder().url(HttpUrl.parse(endpoint)).delete().build();
		} else {
			RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE), stringBody);
			return new Request.Builder().url(HttpUrl.parse(endpoint)).method(method, body).build();
		}
	}



	private void processCombinationEntry(Entry<String, String> entry, HashMap<String, JsonElement> storedElements,
			Map<String, String> entryVSFinalValue, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String paramName = entry.getKey();
		String initialCombination = entry.getValue();
		String[] combinations = initialCombination.split(reservedNames.getMapCombinationSeparator());
		if (combinations.length > 0) {
			String combinationResult=processCombinations(combinations, storedElements, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
			entryVSFinalValue.put(paramName, combinationResult);
		}else {
			entryVSFinalValue.put(paramName, initialCombination);
		}
	
	}

	private String processCombinations(String[] combinations, HashMap<String, JsonElement> storedElements,
			HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String result = "";
		for (String combination : combinations) {
			String[] identifiers = combination.split(reservedNames.getIdentifierSeparator());
			if (identifiers.length > 2) {
				try {
					String stepID = identifiers[0];
					String jsonID = identifiers[1];
					String stepAndJson=stepID+reservedNames.getIdentifierSeparator()+ jsonID;
					JsonElement element=storedElements.get(stepAndJson);
					if(element==null) {
						element=defaultData.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, stepAndJson);
						storedElements.put(stepAndJson, element);
					}
					String combinationResult = processCombination(element, identifiers);
					result = result + combinationResult;
				} catch (Exception e) {
					result = result + combination;
				}
			} else {
				result = result + combination;
			}

		}
		return result;
	}

	private String generateFinalUrl(String[] totalSplit, Map<String, String> entryVSFinalValue,
			String defaultEndpoint) {
		StringBuilder stringBuilder=new StringBuilder();
		boolean isVar=false;
		for (String string : totalSplit) {
			if(isVar) {
				for (var keyNewVal : entryVSFinalValue.entrySet()) {
					if(keyNewVal.getKey().equals(string)) {
						stringBuilder.append(keyNewVal.getValue());
					}
				}
			}else {
				stringBuilder.append(string);
			}
			if(string.isEmpty()) {
				isVar=!isVar;
			}
		}
		return stringBuilder.toString();
	}

	private String[] generateEnpointParamSplit(String[] split1) {
		ArrayList<String> enpointSplits = new ArrayList<>();
		for (String beginSplit : split1) {
			String[] totalSplit = beginSplit.split(reservedNames.getUrlEndParam());
			enpointSplits.addAll(Arrays.asList(totalSplit));
		}
		return enpointSplits.toArray(String[]::new);
	}

	private String processCombination(JsonElement children, String[] keyTree) {
		for (int i = 2; i < keyTree.length; i++) {
			String key = keyTree[i];
			if (defaultData.isOtherItem(keyTree, i)) {
				children = defaultData.getNextChildren(children, key);
			} else {
				children = defaultData.getLastChildren(children, key);
			}

		}
		return children.getAsString();
	}
	

}
