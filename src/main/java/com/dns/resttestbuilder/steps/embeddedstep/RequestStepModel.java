package com.dns.resttestbuilder.steps.embeddedstep;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dns.resttestbuilder.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStepModel {

	@NotBlank(message = "Endpoint is required to send a request")
	String url;
	
	@ElementCollection(fetch = FetchType.EAGER)
	Map<String, String> urlParamKeyVSCombination;

	
	@ElementCollection(fetch = FetchType.LAZY)
	List<String> deleteHeaders;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@Column( length = 100000 )
	Map<String, String> addHeaders;
	
	@NotNull(message = "Method is required to send a request")
	@Enumerated(EnumType.STRING)
	Method method;
	
	@NotBlank(message = "Input JSON or reference to another input is required")
	String inJson;
	
}
