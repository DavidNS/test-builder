package com.dns.resttestbuilder.model.steps;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;

import com.dns.resttestbuilder.configuration.DefaultData;
import com.dns.resttestbuilder.controller.TestExecutorController;
import com.dns.resttestbuilder.entity.embedded.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embedded.mainRequest.Result;
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


	Long testResultID;
	Result result;
	MainRequestStepModel mainRequestStepModel;
	HashMap<Long, String> stepNumberVSOutJSON;
	HashMap<Long, HashMap<Long, String>> stepNumberVSInNumberVSInJSON;
	ConfigurableApplicationContext context;

	@Override
	public void run() {
		RestClient restClient=context.getBean(RestClient.class);
		DefaultData dd=context.getBean(DefaultData.class);
		TestExecutorController testExecutorController=context.getBean(TestExecutorController.class);
		String endpoint = mainRequestStepModel.getRequestStepModel().getUrl();
		String method = mainRequestStepModel.getRequestStepModel().getMethod();
		String mainRequestBody = mainRequestStepModel.getRequestStepModel().getInJson();
		String stringBody=dd.getInputJsonElement(stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON, mainRequestBody).toString();
		String threadName = "StressExecutionExecution: " + result.getSendNumber() + " parallelOrder: " + result.getParallelNumber();
		Map<String, String> paramVSCombination=mainRequestStepModel.getRequestStepModel().getUrlParamKeyVSCombination();
		Thread.currentThread().setName(threadName);
		String updatedEndpoint=restClient.generateCombinedEndpoint(endpoint, paramVSCombination, stepNumberVSInNumberVSInJSON, stepNumberVSOutJSON);
		tryCall(restClient,updatedEndpoint, method, stringBody, threadName);
		testExecutorController.updateTestResults(testResultID, result);
	}

	private void tryCall(RestClient restClient,String endpoint, String method, String stringBody, String threadName) {
		try {
			Call call = restClient.createCall(stringBody, endpoint, method);
			result.setRequestDate(new Date());
			Response response = call.execute();
			result.setResponseDate(new Date());
			updateResponseStatus(response.body().string(), true);
		} catch (Exception e) {
			result.setResponseDate(new Date());
			updateResponseStatus("Not recived response from endpoint, Caused by: " + e.getLocalizedMessage(), false);
			log.error("Error al ejecutar el hilo: {} endpoint: {} method: {} body: {}", threadName, endpoint, method,
					stringBody, e);
		}
	}

	private void updateResponseStatus(String responseBody, boolean received) {
		result.setMainResponse(responseBody);
		result.setResponseReceived(received);
	}
}