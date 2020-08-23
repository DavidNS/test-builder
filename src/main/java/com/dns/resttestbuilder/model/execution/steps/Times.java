package com.dns.resttestbuilder.model.execution.steps;

import java.util.ArrayList;
import java.util.Date;

import com.dns.resttestbuilder.entity.Result;

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
