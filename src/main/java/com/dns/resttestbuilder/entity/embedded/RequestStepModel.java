package com.dns.resttestbuilder.entity.embedded;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStepModel {

	String url;
	

	@ElementCollection(fetch = FetchType.EAGER)
	Map<String, String> urlParamKeyVSValue;
	
	String method;
	
	String inJson;
	
}
