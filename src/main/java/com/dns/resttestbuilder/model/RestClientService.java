package com.dns.resttestbuilder.model;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.entity.Method;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

@Component
public class RestClientService {

	private static final String LOCALHOST_8080 = "http://localhost:8080";
	
	private static final String MEDIA_TYPE = "application/json;charset=" + StandardCharsets.UTF_8.displayName();

//	public <T> void startAsynchronusRequest(ThreadPoolExecutor executor, RequestData<T> requestData,
//			AsyncHandlers<T> asyncHandlers) {
//		executor.execute(new StressExecution<T>(requestData, asyncHandlers));
//	}

//	public <T, R> R doSyncRequest(RequestData<T> requestData) throws Exception {
//		return responseToObject(
//				doRequest(createCall(requestData.getEndpoint(), requestData.getMethod(), requestData.getBodyObj())),
//				requestData.getResponseType());
//	}

	public <T> Call createCall(String endpoint, Method method, T bodyObj) {
		Request request = createRequest(bodyObj, endpoint, method);
		OkHttpClient client = new OkHttpClient();
		return client.newCall(request);
	}

	public Response doRequest(Call call) throws Exception {
		return call.execute();
	}

	public <R> R responseToObject(Response response, Type classType) throws Exception {
		ResponseBody rb=response.body();
		if(rb!=null) {
			String bodyString=rb.string();
			if(classType!=null&&bodyString!=null&& !bodyString.isEmpty()) {
				return new Gson().fromJson(bodyString, classType);
			}
		}

		return null;
	}

	private <T> Request createRequest(T bodyObj, String endpoint, Method method) {
		if (Method.GET.equals(method)) {
			return new Request.Builder().url(HttpUrl.parse(endpoint)).build();
		} else if(Method.DELETE.equals(method)) {
			return new Request.Builder().url(HttpUrl.parse(endpoint)).delete().build();
		} else {
			RequestBody body = RequestBody.create(MediaType.parse(MEDIA_TYPE), new Gson().toJson(bodyObj));
			return new Request.Builder().url(HttpUrl.parse(endpoint)).method(method.name(), body).build();
		}
	}

}
