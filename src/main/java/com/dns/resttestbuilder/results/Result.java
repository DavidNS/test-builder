package com.dns.resttestbuilder.results;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.dns.resttestbuilder.results.embeddedresult.Dates;
import com.dns.resttestbuilder.results.embeddedresult.Differences;
import com.dns.resttestbuilder.results.embeddedresult.Evaluation;
import com.dns.resttestbuilder.results.embeddedresult.Meta;
import com.dns.resttestbuilder.results.embeddedresult.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
	
	@Id
	@GeneratedValue
	Long id;
	
	String userID;
	
	@Embedded
	Meta meta=new Meta();
	
	@Embedded
	RequestInfo requestInfo=new RequestInfo();
	
	@Embedded
	Dates dates=new Dates();
	
	@Embedded
	Evaluation evaluation=new Evaluation();

	
	@Embedded
	Differences differences=new Differences();
}
