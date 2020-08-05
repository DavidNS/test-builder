package com.dns.resttestbuilder.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSONModel {
	
	@Id
	@GeneratedValue
	Long id;
	
	String name;
	
	String stringJSON;
}
