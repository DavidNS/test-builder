package com.dns.resttestbuilder.entity.embeddedstep.mainRequest;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.dns.resttestbuilder.entity.JSONObjectConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ExpectedPerformaceResults {

	@NotNull(message = "Expected single time is mandatory")
	@Min(value = 0,message = "Min value is 1")
	@Max(value = Long.MAX_VALUE,message = "Max value is "+Long.MAX_VALUE)
	Long expectedSingleTime;
	
	@NotNull(message = "Expected parall time is mandatory")
	@Min(value = 0,message = "Min value is 1")
	@Max(value = Long.MAX_VALUE,message = "Max value is "+Long.MAX_VALUE)
	Long expectedPararellTime;
	
	@NotNull(message = "Expected total time is mandatory")
	@Min(value = 0,message = "Min value is 1")
	@Max(value = Long.MAX_VALUE,message = "Max value is "+Long.MAX_VALUE)
	Long expectedTotalTime;
	
	@NotNull(message = "Expected total time is mandatory. (Default false)")
	Boolean forceTimeoutByMaxExpectedTime=false;

	@Embedded
	List<TimeSuccessKind> timeSuccessKind;
	
	@NotNull(message = "Valid response success kind is mandatory (Default NONE): NONE,RECEIVED,SUCCESS,AS_EXPECTED")
	@Enumerated(EnumType.STRING)
	ResponseSuccessKind responseSuccessKind=ResponseSuccessKind.NONE;
	
	@Convert(converter = JSONObjectConverter.class)
	@Lob
	Object output;
	
	Integer expectedHttpStatus;
}
