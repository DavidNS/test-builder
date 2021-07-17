package com.dns.resttestbuilder.testexecutions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.Method;
import com.dns.resttestbuilder.configuration.ReservedNames;
import com.dns.resttestbuilder.testexecutions.execution.Headers;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;

@Component
public class RestClient {

	@Autowired
	ReservedNamesParser reservedNamesParser;

	private static final String MEDIA_TYPE = "application/json;charset=" + StandardCharsets.UTF_8.displayName();

	public Call createCall(String stringBody, String endpoint, Method method, Headers headers, Long socketTimeout) {
		Request request = createRequest(stringBody, endpoint, method, headers);
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

	private Request createRequest(String stringBody, String endpoint, Method method, Headers headers) {
		Builder builder=new Request.Builder().url(HttpUrl.parse(endpoint));
		deleteHeaders(builder,headers.getDeleteHeadders());
		addHeaders(builder,headers.getAddHeadders());
		if (Method.GET.equals(method)) {
			return builder.build();
		} else if (Method.DELETE.equals(method)) {
			return builder.delete().build();
		} else {
			RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE), stringBody);
			return builder.method(method.name(), body).build();
		}
	}

	private void addHeaders(Builder builder, Map<String, String> addHeadders) {
		if(addHeadders!=null) {
			for (Map.Entry<String, String> keyVsVal : addHeadders.entrySet()) {
				builder.addHeader(keyVsVal.getKey(), keyVsVal.getValue());
			}
		}
	}

	private void deleteHeaders(Builder builder, List<String> deleteHeadders) {
		if(deleteHeadders!=null) {
			for (String headderName : deleteHeadders) {
				builder.removeHeader(headderName);
			}
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
		String[] split1 = url.split(ReservedNames.URL_BEGIN_PARAM);
		ArrayList<String> enpointSplits = new ArrayList<>();
		for (String beginSplit : split1) {
			String[] totalSplit = beginSplit.split(ReservedNames.URL_END_PARAM);
			enpointSplits.addAll(Arrays.asList(totalSplit));
		}
		return enpointSplits.toArray(String[]::new);
	}
}
