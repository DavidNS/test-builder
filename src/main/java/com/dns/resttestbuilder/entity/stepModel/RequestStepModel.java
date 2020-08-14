package com.dns.resttestbuilder.entity.stepModel;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStepModel {

	String url;
	
	String method;
	
	String inJson;
	
}
