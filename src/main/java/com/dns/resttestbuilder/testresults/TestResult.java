package com.dns.resttestbuilder.testresults;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.dns.resttestbuilder.results.Result;
import com.dns.resttestbuilder.steps.embeddedstep.MainRequestStepModel;

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
	
	String userID;

	@Embedded
	MainRequestStepModel mainRequestStepModel;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	List<Result> results;
	
	private Long timeOK=0L;
	
	private Long timeKO=0L;
	
	private Long formatOK=0L;
	
	private Long formatKO=0L;
}
