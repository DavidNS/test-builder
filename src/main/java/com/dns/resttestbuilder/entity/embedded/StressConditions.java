package com.dns.resttestbuilder.entity.embedded;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StressConditions {
	
	Long numberOfParallelRequest;
	
	Long delayBetweenParallelRequest;
	
	Long numberOfTest;
	
	Long delayBetweenParallelTest;
}
