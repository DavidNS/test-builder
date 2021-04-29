package com.dns.resttestbuilder.testexecutions.execution.steps;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.Method;
import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.steps.embeddedstep.RequestStepModel;
import com.dns.resttestbuilder.testexecutions.JsonObjectParser;
import com.dns.resttestbuilder.testexecutions.ReservedNamesParser;
import com.dns.resttestbuilder.testexecutions.RestClient;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendRequestStep {

	
	@Autowired
	JsonObjectParser jsonObjectParser;
	
	@Autowired
	ReservedNamesParser jsonInParser;

	@Autowired
	RestClient restClient;

	public void processStep(Step step, HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON,
			HashMap<Long, String> stepNumberVSOutJSON) {
		Long stepNumber = step.getStepOrder();
		RequestStepModel requestStepModel =  jsonObjectParser.objectToModel(step.getStepModel(), RequestStepModel.class,step::setStepModel) ;
		String endpoint = requestStepModel.getUrl();
		Method method = requestStepModel.getMethod();
		Map<String, String> paramVsCombination = requestStepModel.getUrlParamKeyVSCombination();
		String requestBody = jsonInParser
				.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, requestStepModel.getInJson())
				.toString();
		HashMap<Long, String> inHash = new HashMap<>();
		inHash.put(1L, requestBody);
		stepNumberVSInNumberVSInJSON.put(stepNumber, inHash);
		String enpointUdpated = restClient.generateCombinedEndpoint(endpoint, paramVsCombination,
				stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		String responseBody = tryCall(step, enpointUdpated, method, requestBody);
		stepNumberVSOutJSON.put(stepNumber, responseBody);
	}

	private String tryCall(Step step, String endpoint, Method method, String stringBody) {
		String result = "ERROR";
		try {
			Call call = restClient.createCall(stringBody, endpoint, method,0L);
			Response response = call.execute();
			result = response.body().string();
		} catch (Exception e) {
			log.error("Error al llamar al endpoint: {} method: {} body: {}", endpoint, method, stringBody, e);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("error", "Exception message: " + e.getMessage() + " when calling endpoint: "
					+ endpoint + " method: " + method + " body: " + stringBody);
			result = jsonObject.toString();
		}
		return result;
	}

}
