package com.dns.resttestbuilder.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.dns.resttestbuilder.entity.embeddedresult.Dates;
import com.dns.resttestbuilder.entity.embeddedresult.Differences;
import com.dns.resttestbuilder.entity.embeddedresult.Evaluation;
import com.dns.resttestbuilder.entity.embeddedresult.Meta;
import com.dns.resttestbuilder.entity.embeddedresult.RequestInfo;

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
	
	Long userID;
	
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
