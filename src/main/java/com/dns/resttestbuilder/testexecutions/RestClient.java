package com.dns.resttestbuilder.testexecutions;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.Method;
import com.dns.resttestbuilder.testexecutions.execution.Headers;
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




}
