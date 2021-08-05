package com.dns.resttestbuilder.projects;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.dns.resttestbuilder.tests.Test;

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
	
	String userID;
	
	String name;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Test> tests;
	
}
