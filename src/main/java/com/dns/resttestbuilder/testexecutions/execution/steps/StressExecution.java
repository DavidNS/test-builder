package com.dns.resttestbuilder.testexecutions.execution.steps;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;

import com.dns.resttestbuilder.Method;
import com.dns.resttestbuilder.results.Result;
import com.dns.resttestbuilder.results.embeddedresult.RequestInfo;
import com.dns.resttestbuilder.steps.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.steps.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.testexecutions.ReservedNamesParser;
import com.dns.resttestbuilder.testexecutions.RestClient;
import com.dns.resttestbuilder.testexecutions.TestExecutorController;
import com.dns.resttestbuilder.testexecutions.execution.Headers;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class StressExecution implements Runnable {

	private static final int HTTP_STATUS_STRESS_ERROR = 999;

	Times tt;
	Times pt;

	Long testResultID;
	Result result;
	MainRequestStepModel mainRequestStepModel;
	HashMap<Long, String> stepNumberVSOutJSON;
	HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON;
	ConfigurableApplicationContext context;

	@Override
	public void run() {
		RestClient restClient = context.getBean(RestClient.class);
		ReservedNamesParser reservedNamesParser = context.getBean(ReservedNamesParser.class);
		TestExecutorController testExecutorController = context.getBean(TestExecutorController.class);
		String endpoint = mainRequestStepModel.getRequestStepModel().getUrl();
		Method method = mainRequestStepModel.getRequestStepModel().getMethod();
		String mainRequestBody = mainRequestStepModel.getRequestStepModel().getInJson();
		String stringBody = reservedNamesParser
				.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, mainRequestBody).toString();
		Headers headers = new Headers(mainRequestStepModel.getRequestStepModel().getAddHeaders(),
				mainRequestStepModel.getRequestStepModel().getDeleteHeaders());

		String threadName = "StressExecutionExecution: " + result.getMeta().getSendNumber() + " parallelOrder: "
				+ result.getMeta().getParallelNumber();
		Map<String, String> paramVSCombination = mainRequestStepModel.getRequestStepModel()
				.getUrlParamKeyVSCombination();
		Thread.currentThread().setName(threadName);
		String updatedEndpoint = reservedNamesParser.generateCombinedEndpoint(endpoint, paramVSCombination,
				stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		tryCall(restClient, updatedEndpoint, method, stringBody, headers, threadName);
		testExecutorController.updateTestResults(testResultID, result, tt, pt, stepNumberVSInNumberVSInJSON,
				stepNumberVSOutJSON);
	}

	private void tryCall(RestClient restClient, String endpoint, Method method, String stringBody, Headers headers,
			String threadName) {
		try {
			result.getRequestInfo().setUrl(endpoint);
			result.getRequestInfo().setRequest(stringBody);
			result.getRequestInfo().setMethod(method);
			Call call = createCall(restClient, endpoint, method, stringBody, headers);
			result.getDates().setRequestDate(new Date());
			Response response = call.execute();
			result.getDates().setResponseDate(new Date());
			updateResponseStatus(response.body().string(), response.code(), true);
		} catch (Exception e) {
			result.getDates().setResponseDate(new Date());
			JsonObject js = new JsonObject();
			js.add("stress-execution-error",
					new JsonPrimitive("Not recived response from endpoint, Caused by: " + e.getMessage()));
			updateResponseStatus(js.toString(), HTTP_STATUS_STRESS_ERROR, false);
			log.error("Error al ejecutar el hilo: {} endpoint: {} method: {} body: {}", threadName, endpoint, method,
					stringBody, e);
		}
	}

	private Call createCall(RestClient restClient, String endpoint, Method method, String stringBody, Headers headers) {
		ExpectedPerformaceResults expectedPerformaceResults = mainRequestStepModel.getExpectedPerformaceResults();
		if (expectedPerformaceResults.getForceTimeoutByMaxExpectedTime()) {
			Long maxRT = expectedPerformaceResults.getExpectedSingleTime();
			Long maxPT = expectedPerformaceResults.getExpectedPararellTime();
			Long maxTT = expectedPerformaceResults.getExpectedTotalTime();
			Long max = Math.max(maxTT, Math.max(maxRT, maxPT));
			return restClient.createCall(stringBody, endpoint, method, headers, max);
		} else {
			return restClient.createCall(stringBody, endpoint, method, headers, 0L);
		}
	}

	private void updateResponseStatus(String responseBody, int httpStatus, boolean received) {
		RequestInfo requestInfo = result.getRequestInfo();
		requestInfo.setResponseCode(httpStatus);
		requestInfo.setResponse(responseBody);
		requestInfo.setResponseReceived(received);
	}
}