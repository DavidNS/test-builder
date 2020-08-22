package com.dns.resttestbuilder.entity.embeddedstep.mainRequest;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ExpectedPerformaceResults {

	@NotNull(message = "Expected single time is mandatory")
	@Min(value = 0,message = "Min value is 0")
	Long expectedSingleTime;
	
	@NotNull(message = "Expected parall time is mandatory")
	@Min(value = 0,message = "Min value is 0")
	Long expectedPararellTime;
	
	@NotNull(message = "Expected total time is mandatory")
	@Min(value = 0,message = "Min value is 0")
	Long expectedTotalTime;
	
	Boolean forceTimeoutByMaxExpectedTime=false;

	@Embedded
	List<TimeSuccessKind> timeSuccessKind;
	
	@NotNull(message = "Valid response success kind is mandatory: NONE,RESPONSE_RECEIVED,RESPONSE_SUCCESS,MAPPED_AS_EXPECTED,EXACT_AS_EXPECTED")
	@Enumerated(EnumType.STRING)
	ResponseSuccessKind responseSuccessKind=ResponseSuccessKind.NONE;
	
	HashMap<String, String> plainKeyVSExpectedOutValue;
	
}
