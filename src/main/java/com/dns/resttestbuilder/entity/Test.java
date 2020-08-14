package com.dns.resttestbuilder.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	
	Long userID;
	
	String name;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<TestResult> testResult;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Step> steps;
	
	
}
