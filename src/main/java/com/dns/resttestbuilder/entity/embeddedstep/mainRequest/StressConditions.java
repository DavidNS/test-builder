package com.dns.resttestbuilder.entity.embeddedstep.mainRequest;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StressConditions {
	
	@NotNull(message = "Number of parallell test is mandatory")
	@Min(value = 0, message = "Min value is 0")
	Long numberOfParallelRequest;
	
	@NotNull(message = "Delay between parallell request is mandatory")
	@Min(value = 0, message = "Min value is 0")
	Long delayBetweenParallelRequest;
	
	@NotNull(message = "Number of test is mandatory")
	@Min(value = 0, message = "Min value is 0")
	Long numberOfTest;
	
	@NotNull(message = "Delay between parallell test is mandatory")
	@Min(value = 0, message = "Min value is 0")
	Long delayBetweenParallelTest;
}
