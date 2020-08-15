package com.dns.resttestbuilder.entity.embedded;

import javax.persistence.Embeddable;

import com.dns.resttestbuilder.entity.embedded.mainRequest.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embedded.mainRequest.StressConditions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainRequestStepModel {
	
	RequestStepModel requestStepModel;
	
	StressConditions stressConditions;
	
	ExpectedPerformaceResults expectedPerformaceResults;
}
