package com.dns.resttestbuilder.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step {
	
	
	@Id
	@GeneratedValue
	Long id;
	
	Long userID;
	
	String name;
	
	@Enumerated(EnumType.STRING)
	StepKind stepKind;
	
	Long stepOrder;

	@Lob
	String stepModel;

}
