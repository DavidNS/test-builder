package com.dns.resttestbuilder.data;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	
	String name;
	
	@Column(unique = true)
	String fxmlName;
	
	Long stepOrder;
	
	@OneToMany
	List<JSONModel> jsonInputs;

	HashMap<String, String> userFXMLChoices;
	
	@OneToOne
	JSONModel jsonExpectedOutput;
}
