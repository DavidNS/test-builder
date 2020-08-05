package com.dns.resttestbuilder.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Profile {

	@Id
	@GeneratedValue
	Long id;
	
	String name;
	
	Long selectWorkspaceID;
	
	@OneToMany(fetch = FetchType.EAGER)
	List<Workspace> workspaces;
	

}
