package com.dns.resttestbuilder.model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	ReservedNamesParser reservedNamesParser;

	private static final String MEDIA_TYPE = "application/json;charset=" + StandardCharsets.UTF_8.displayName();

	public Call createCall(String stringBody, String endpoint, Method method, Long socketTimeout) {
		Request request = createRequest(stringBody, endpoint, method);
		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(socketTimeout, TimeUnit.MILLISECONDS);
		client.setReadTimeout(socketTimeout, TimeUnit.MILLISECONDS);
		return client.newCall(request);
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

	private Request createRequest(String stringBody, String endpoint, Method method) {
		if (Method.GET.equals(method)) {
			return new Request.Builder().url(HttpUrl.parse(endpoint)).build();
		} else if (Method.DELETE.equals(method)) {
			return new Request.Builder().url(HttpUrl.parse(endpoint)).delete().build();
		} else {
			RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE), stringBody);
			return new Request.Builder().url(HttpUrl.parse(endpoint)).method(method.name(), body).build();
		}
	}

	private void processCombinationEntry(Entry<String, String> entry, HashMap<String, JsonElement> storedElements,
			Map<String, String> entryVSFinalValue, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		String combinationResult = reservedNamesParser.processCombinations(entry.getValue(), storedElements, stepNumberVSInNumberVSInJSON,
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
		String[] split1 = url.split(reservedNames.getUrlBeginParam());
		ArrayList<String> enpointSplits = new ArrayList<>();
		for (String beginSplit : split1) {
			String[] totalSplit = beginSplit.split(reservedNames.getUrlEndParam());
			enpointSplits.addAll(Arrays.asList(totalSplit));
		}
		return enpointSplits.toArray(String[]::new);
	}
}
