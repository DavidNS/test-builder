package com.dns.resttestbuilder.tests;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.dns.resttestbuilder.steps.Step;
import com.dns.resttestbuilder.testresults.TestResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
	
	@Id
	@GeneratedValue
	Long id;
	
	String userID;
	
	String name;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<TestResult> testResults;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Step> steps;
	
	
}
