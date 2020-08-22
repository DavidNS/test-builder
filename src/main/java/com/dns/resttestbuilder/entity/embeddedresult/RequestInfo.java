package com.dns.resttestbuilder.entity.embeddedresult;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dns.resttestbuilder.entity.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {
	
	@Enumerated(EnumType.STRING)
	Method method;
	
	String mainUrl;
	
	String mainRequest;

	String mainResponse;
}
