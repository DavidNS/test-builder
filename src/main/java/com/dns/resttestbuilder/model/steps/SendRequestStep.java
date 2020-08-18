package com.dns.resttestbuilder.model.steps;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.entity.Step;
import com.dns.resttestbuilder.entity.embedded.RequestStepModel;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendRequestStep {

	@Autowired
	DefaultData defaultData;
	
	@Autowired
	RestClient restClient;

	public void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		Long stepNumber = step.getStepOrder();
		RequestStepModel requestStepModel = new Gson().fromJson(step.getStepModel(), RequestStepModel.class);
		String endpoint = requestStepModel.getUrl();
		String method = requestStepModel.getMethod();
		Map<String, String> paramVsCombination = requestStepModel.getUrlParamKeyVSCombination();
		String requestBody = defaultData
				.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, requestStepModel.getInJson())
				.toString();
		HashMap<Long, String> inHash = new HashMap<>();
		inHash.put(1L, requestBody);
		stepNumberVSInNumberVSInJSON.put(stepNumber, inHash);
		String enpointUdpated = restClient.generateCombinedEndpoint(endpoint, paramVsCombination, stepNumberVSInNumberVSInJSON,
				stepNumberVSOutJSON);
		String responseBody = tryCall(enpointUdpated, method, requestBody);
		stepNumberVSOutJSON.put(stepNumber, responseBody);
	}

	private String tryCall(String endpoint, String method, String stringBody) {
		String result = "NOT-RECEIVED";
		try {
			Call call = restClient.createCall(stringBody, endpoint, method);
			Response response = call.execute();
			result = response.body().string();
		} catch (Exception e) {
			log.error("Error al ejecutar lammar a endpoint: {} method: {} body: {}", endpoint, method, stringBody, e);
		}
		return result;
	}

}
