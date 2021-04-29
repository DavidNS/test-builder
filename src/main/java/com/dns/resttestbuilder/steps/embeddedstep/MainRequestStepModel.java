package com.dns.resttestbuilder.steps.embeddedstep;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.dns.resttestbuilder.steps.embeddedstep.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.steps.embeddedstep.mainRequest.StressConditions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainRequestStepModel  {
	
	@NotNull(message = "RequestStepModel is mandatory")
	RequestStepModel requestStepModel;
	
	@NotNull(message = "StressConditions is mandatory")
	StressConditions stressConditions;
	
	@NotNull(message = "ExpectedPerformaceResults is mandatory")
	ExpectedPerformaceResults expectedPerformaceResults;
}
