package com.dns.resttestbuilder.entity.embedded.mainRequest;

import java.util.Date;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Result {
	
	Long sendNumber;
	
	Long parallelNumber;
	
	String mainRequest;

	String mainResponse;

	Date requestDate;

	Date responseDate;

	Date pararellTestEndDate;

	Date testEndDate;

	Boolean responseReceived;

	Boolean pararellTestEnd;

	Boolean testEnd;

}
