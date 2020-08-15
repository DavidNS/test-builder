package com.dns.resttestbuilder.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "reserved-names")
public class ReservedNames {
	
	private String arrayIdentifier;
	
	private String keyIdentifier;
	
	private String inputIdentifier;
	
	private String outputIdentifier;
	
	private String inOutSeparator;
}
