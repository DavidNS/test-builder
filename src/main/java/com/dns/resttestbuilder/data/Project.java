package com.dns.resttestbuilder.data;

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
public class Project {

	@Id
	@GeneratedValue
	Long id;
	
	String name;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Test> tests;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Endpoint> endpoints;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<JSONModel> jsonModels;
}
