package com.dns.resttestbuilder.testexecutions.execution.steps;

import java.util.ArrayList;
import java.util.Date;

import com.dns.resttestbuilder.results.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Times {
	
	ArrayList<Result> totalResult = new ArrayList<>();;
	
	Long extectedResponses;

	Date minRequestDate;
	
	Date maxResponseDate;
	
}
