package com.dns.resttestbuilder.model.steps;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.dns.resttestbuilder.controller.TestExecutorController;
import com.dns.resttestbuilder.entity.Method;
import com.dns.resttestbuilder.entity.embedded.MainRequestStepModel;
import com.dns.resttestbuilder.entity.embedded.mainRequest.Result;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
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

	private static final String MEDIA_TYPE = "application/json;charset=" + StandardCharsets.UTF_8.displayName();

	Long sendNumber;
	Long parallelNumber;
	Long testResultID;
	Result result;
	MainRequestStepModel mainRequestStepModel;
	TestExecutorController testExecutorController;

	@Override
	public void run() {
		result.setSendNumber(sendNumber);
		result.setParallelNumber(parallelNumber);
		String endpoint = result.getMainURL();
		String method = mainRequestStepModel.getRequestStepModel().getMethod();
		String stringBody = result.getMainRequest();
		String threadName = "StressExecutionExecution: " + sendNumber + " parallelOrder: " + parallelNumber;
		Thread.currentThread().setName(threadName);
		tryCall(endpoint, method, stringBody, threadName);
		testExecutorController.updateTestResults(testResultID, result);
	}

	private void tryCall(String endpoint, String method, String stringBody, String threadName) {
		try {
			Call call = createCall(stringBody, endpoint, method);
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

	private Call createCall(String stringBody, String endpoint, String method) {
		long soketTimeout = 5000L;
		Request request = createRequest(stringBody, endpoint, method);
		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(soketTimeout, TimeUnit.MILLISECONDS);
		client.setReadTimeout(soketTimeout, TimeUnit.MILLISECONDS);
		return client.newCall(request);
	}

	private void updateResponseStatus(String responseBody, boolean received) {
		result.setMainResponse(responseBody);
		result.setResponseReceived(received);
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
}