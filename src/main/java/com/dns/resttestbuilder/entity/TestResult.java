package com.dns.resttestbuilder.entity;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.dns.resttestbuilder.entity.embedded.Result;
import com.dns.resttestbuilder.entity.stepModel.MainRequestStepModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResult {
	
	@Id
	@GeneratedValue
	Long id;
	
	Long userID;

	@Embedded
	MainRequestStepModel mainRequestStepModel;
	
	@ElementCollection(fetch = FetchType.EAGER)
	List<Result> results;
	
	private boolean testSuccessful;
}
