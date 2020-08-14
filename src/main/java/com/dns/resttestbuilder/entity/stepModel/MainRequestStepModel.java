package com.dns.resttestbuilder.entity.stepModel;

import javax.persistence.Embeddable;

import com.dns.resttestbuilder.entity.embedded.ExpectedPerformaceResults;
import com.dns.resttestbuilder.entity.embedded.StressConditions;

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
