package com.dns.resttestbuilder.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {
	
	@Id
	@GeneratedValue
	Long id;
	
	String shortName;
	
	String url;
	
	@Enumerated(EnumType.STRING)
	Method method;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "IN_MODEL_ID")
	JSONModel inModel;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "OUT_MODEL_ID")
	JSONModel outModel;
	
	
}
