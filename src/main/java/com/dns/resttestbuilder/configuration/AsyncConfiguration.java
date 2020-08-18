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
	
	private int executionStressPoolSize;
	
	private int executionStressMaxQueue;
	
	private int keepAliveExecutionStress;
	

	@Bean
	@Scope("singleton")
	public ThreadPoolExecutor getTestAsyncExecutor() {
		return new ThreadPoolExecutor(testElementPoolSize, testElementMaxQueue, keepAliveTimeTestElement,  TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(testElementMaxQueue));
	}
	
	@Bean
	@Scope("singleton")
	public ThreadPoolExecutor getStressAsyncExecutor() {
		return new ThreadPoolExecutor(executionStressPoolSize, executionStressMaxQueue, keepAliveExecutionStress, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(executionStressMaxQueue));
	}


	
}
