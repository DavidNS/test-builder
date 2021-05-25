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

	private static final String SEPARATOR = "\\d+";

	private String arrayBeginIdentifier;

	private String arrayEndIdentifier;
	
	private String stepIdentifier;

	private String inputIdentifier;

	private String outputIdentifier;

	private String identifierSeparator;
	
	private String identifierSeparatorNotEscaped;

	private String mapCombinationSeparator;

	private String urlBeginParam;

	private String urlEndParam;

	
	public String getStepInRegexp() {
		return stepIdentifier + SEPARATOR + identifierSeparator + inputIdentifier + SEPARATOR;
	}
	
	public String getStepOutRegex() {
		return stepIdentifier + SEPARATOR + identifierSeparator + outputIdentifier;
	}



}
