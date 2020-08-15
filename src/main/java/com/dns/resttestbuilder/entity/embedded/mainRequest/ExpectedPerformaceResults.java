package com.dns.resttestbuilder.entity.embedded.mainRequest;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ExpectedPerformaceResults {

	Long expectedRequestTime;
	
	Long expectedPararellTime;
	
	Long expectedTotalTime;
	
}
