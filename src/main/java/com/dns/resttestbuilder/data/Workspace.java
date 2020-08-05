package com.dns.resttestbuilder.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workspace {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	String name;
	
	
	Long selectProjectID;

	@OneToMany(fetch = FetchType.EAGER)
	List<Project> projects;

	@OneToMany
	List<MappedValue> environments;

	@OneToMany
	List<MappedValue> globalVars;
}
