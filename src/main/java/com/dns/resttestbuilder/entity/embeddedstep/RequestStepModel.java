package com.dns.resttestbuilder.entity.embeddedstep;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dns.resttestbuilder.entity.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStepModel {

	@NotBlank(message = "Enponint is required to send a request")
	String url;
	

	@ElementCollection(fetch = FetchType.EAGER)
	Map<String, String> urlParamKeyVSCombination;
	

	@NotNull(message = "Method is required to send a request")
	@Enumerated(EnumType.STRING)
	Method method;
	
	@NotBlank(message = "Input JSON or reference to another input is required")
	String inJson;
	
}
