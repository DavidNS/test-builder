package com.dns.resttestbuilder.model.steps;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;

import com.dns.resttestbuilder.controller.TestExecutorController;
import com.dns.resttestbuilder.entity.Method;
import com.dns.resttestbuilder.entity.Result;
import com.dns.resttestbuilder.entity.embeddedstep.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.model.JsonInParser;
import com.dns.resttestbuilder.model.RestClient;
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
		JsonInParser jsParser = context.getBean(JsonInParser.class);
		TestExecutorController testExecutorController = context.getBean(TestExecutorController.class);
		String endpoint = mainRequestStepModel.getRequestStepModel().getUrl();
		Method method = mainRequestStepModel.getRequestStepModel().getMethod();
		String mainRequestBody = mainRequestStepModel.getRequestStepModel().getInJson();
		String stringBody = jsParser.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, mainRequestBody)
				.toString();
		String threadName = "StressExecutionExecution: " + result.getMeta().getSendNumber() + " parallelOrder: "
				+ result.getMeta().getParallelNumber();
		Map<String, String> paramVSCombination = mainRequestStepModel.getRequestStepModel()
				.getUrlParamKeyVSCombination();
		Thread.currentThread().setName(threadName);
		String updatedEndpoint = restClient.generateCombinedEndpoint(endpoint, paramVSCombination,
				stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		tryCall(restClient, updatedEndpoint, method, stringBody, threadName);
		testExecutorController.updateTestResults(testResultID, result, tt, pt);
	}

	private void tryCall(RestClient restClient, String endpoint, Method method, String stringBody, String threadName) {
		try {
			result.getRequestInfo().setMainUrl(endpoint);
			result.getRequestInfo().setMainRequest(stringBody);
			result.getRequestInfo().setMethod(method);
			Call call = createCall(restClient, endpoint, method, stringBody);
			result.getDates().setRequestDate(new Date());
			Response response = call.execute();
			result.getDates().setResponseDate(new Date());
			updateResponseStatus(response.body().string(), true);
		} catch (Exception e) {
			result.getDates().setResponseDate(new Date());
			updateResponseStatus("Not recived response from endpoint, Caused by: " + e.getMessage(), false);
			log.error("Error al ejecutar el hilo: {} endpoint: {} method: {} body: {}", threadName, endpoint, method,
					stringBody, e);
		}
	}

	private Call createCall(RestClient restClient, String endpoint, Method method, String stringBody) {
		ExpectedPerformaceResults expectedPerformaceResults = mainRequestStepModel.getExpectedPerformaceResults();
		if (expectedPerformaceResults.getForceTimeoutByMaxExpectedTime()) {
			Long maxRT = expectedPerformaceResults.getExpectedSingleTime();
			Long maxPT = expectedPerformaceResults.getExpectedPararellTime();
			Long maxTT = expectedPerformaceResults.getExpectedTotalTime();
			Long max = Math.max(maxTT, Math.max(maxRT, maxPT));
			return restClient.createCall(stringBody, endpoint, method, max);
		} else {
			return restClient.createCall(stringBody, endpoint, method, 0L);
		}
	}

	private void updateResponseStatus(String responseBody, boolean received) {
		result.getRequestInfo().setMainResponse(responseBody);
		result.getEvaluation().setResponseReceived(received);
	}
}