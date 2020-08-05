package com.dns.resttestbuilder.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import lombok.Data;

@Data
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "async-configuration")
public class AsyncConfiguration {
	
	private int testElementPoolSize;
	
	private int testElementMaxQueue; //TODO Should be the limit in the textInputFields
	
	private long keepAliveTimeTestElement;
	
	private int userInputPoolSize;
	
	private int userInputMaxQueue;
	
	private int keepAliveUserInput;
	

	@Bean
	@Scope("singleton")
	public ThreadPoolExecutor getUserInputAsyncExecutor() {
		return new ThreadPoolExecutor(userInputPoolSize, userInputMaxQueue, keepAliveUserInput, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(userInputMaxQueue));
	}
	
	@Bean
	@Scope("singleton")
	public ThreadPoolExecutor getTestElementExecutor() {
		return new ThreadPoolExecutor(testElementPoolSize, testElementMaxQueue, keepAliveTimeTestElement, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(testElementMaxQueue));
	}


	
}
